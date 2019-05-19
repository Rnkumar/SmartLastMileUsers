package com.hackthon.here.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hackthon.here.R;
import com.hackthon.here.Utils;
import com.mukesh.OnOtpCompletionListener;
import com.mukesh.OtpView;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity{

    private static final String TAG = "Login Activity";
    private TextInputEditText loginPhoneNumberEditText,nameEditText;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private TextView resultText;
    private AlertDialog otpDialog;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setTitle(getString(R.string.login));
        loginPhoneNumberEditText= findViewById(R.id.login_mobile_number);
        nameEditText = findViewById(R.id.login_name);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() != null){
            finish();
            startActivity(new Intent(this,HomeActivity.class));
        }
    }

    public void getOtp(View view) {
        if ( loginPhoneNumberEditText.getText() != null ) {
            String phoneNumber = loginPhoneNumberEditText.getText().toString().trim();
            if (!TextUtils.isEmpty(phoneNumber) || phoneNumber.length() >= 10 ) {
                progressDialog.show();
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        phoneNumber,
                        60,
                        TimeUnit.SECONDS,
                        this,
                        new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                                signInWithPhoneAuthCredential(phoneAuthCredential);
                            }

                            @Override
                            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(s, forceResendingToken);
                                mVerificationId = s;
                                mResendToken = forceResendingToken;
                                progressDialog.dismiss();
                                getOtpEntryPopUp();
                            }

                            @Override
                            public void onVerificationFailed(FirebaseException e) {
                                progressDialog.dismiss();
                                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                                    Toast.makeText(LoginActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                                } else if (e instanceof FirebaseTooManyRequestsException) {
                                    Toast.makeText(LoginActivity.this, "Firebase Quota Reached", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(LoginActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            } else {
                Toast.makeText(this, "Enter a valid mobile number", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "Enter Mobile Number", Toast.LENGTH_SHORT).show();
        }
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential phoneAuthCredential) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithCredential(phoneAuthCredential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            if(otpDialog!=null)
                            otpDialog.dismiss();
                            Log.e(TAG, "signInWithCredential:success");
                            FirebaseUser user = task.getResult().getUser();
                            DatabaseReference userref = FirebaseDatabase.getInstance().getReference(Utils.getUserKey()).child(user.getUid()).child(Utils.getProfileKey());
                            Map<String,String> values = new HashMap<>();
                            values.put(Utils.getMobileKey(),loginPhoneNumberEditText.getText().toString());
                            userref.setValue(values);
                            SharedPreferences preferences = getSharedPreferences(Utils.getSharedPreferenceName(), Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString(Utils.getNameKey(),nameEditText.getText().toString());
                            editor.putString(Utils.getMobileKey(),loginPhoneNumberEditText.getText().toString());
                            editor.apply();
                            finish();
                            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        } else {
                            progressDialog.dismiss();
                            Log.e(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                resultText.setError("Invalid code.");
                            }
                        }
                    }
                });
    }

    public void getOtpEntryPopUp(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);

        View view = getLayoutInflater().inflate(R.layout.popup_enter_otp,null);

        resultText = view.findViewById(R.id.result_text);

        OtpView otpView = view.findViewById(R.id.otp_view);
        otpView.setOtpCompletionListener(otp -> {
            Toast.makeText(LoginActivity.this, "OTP:"+otp, Toast.LENGTH_SHORT).show();
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, otp);
            signInWithPhoneAuthCredential(credential);
        });

        builder.setView(view);
        otpDialog = builder.create();
        otpDialog.show();
    }

}
