package com.hackthon.here.fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hackthon.here.R;
import com.hackthon.here.Utils;
import com.hackthon.here.models.ProfileModel;
import com.here.android.mpa.common.ApplicationContext;
import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.common.GeoPosition;
import com.here.android.mpa.common.LocationDataSourceHERE;
import com.here.android.mpa.common.MapEngine;
import com.here.android.mpa.common.OnEngineInitListener;
import com.here.android.mpa.common.PositioningManager;
import com.here.android.positioning.StatusListener;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment implements PositioningManager.OnPositionChangedListener{

    private String mobile, address, email, userName;

    private TextView latLng, bonusPoints;

    private SharedPreferences sharedPreferences;

    private PositioningManager mPositioningManager;

    // HERE location data source instance
    private LocationDataSourceHERE mHereLocation;


    private GeoCoordinate geoCoordinate;
    private FirebaseUser user;


    private TextInputEditText mobileEditText,addressEditText, usernameEditText, emailEditText;

    public ProfileFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        usernameEditText = view.findViewById(R.id.username);
        emailEditText = view.findViewById(R.id.email);
        mobileEditText = view.findViewById(R.id.mobile);
        addressEditText = view.findViewById(R.id.address);
        bonusPoints = view.findViewById(R.id.bonuspoints);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        FirebaseDatabase.getInstance().getReference(Utils.getUserKey()).child(user.getUid()).child(Utils.getProfileKey())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ProfileModel profileModel = dataSnapshot.getValue(ProfileModel.class);
                        usernameEditText.setText(profileModel.getName());
                        emailEditText.setText(profileModel.getEmail());
                        mobileEditText.setText(profileModel.getMobile());
                        addressEditText.setText(profileModel.getAddress());
                        bonusPoints.setText(String.format("%d points", profileModel.getBonuspoints()));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e("eror",databaseError.getMessage());
                    }
                });


        sharedPreferences = getActivity().getSharedPreferences(Utils.getSharedPreferenceName(), Context.MODE_PRIVATE);

        Button update = view.findViewById(R.id.update);
        //Button getLocation = view.findViewById(R.id.getLocation);
//        getLocation.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                boolean success = com.here.android.mpa.common.MapSettings.setIsolatedDiskCacheRootPath(
//                        getActivity().getExternalFilesDir(null) + File.separator + ".here-maps",
//                        "com.here2k19.projects.smartlastmileuser.MapService");
//
//                if(!success){
//
//                }else{
//                    MapEngine.getInstance().init(new ApplicationContext(getActivity()), new OnEngineInitListener() {
//
//                        @Override
//                        public void onEngineInitializationCompleted(Error error) {
//
//
//                                Toast.makeText(getActivity(), "Map Engine initialized with error code:" + error,
//                                        Toast.LENGTH_SHORT).show();
//
//                                mPositioningManager = PositioningManager.getInstance();
//
//                                mHereLocation = LocationDataSourceHERE.getInstance(new StatusListener() {
//                                    @Override
//                                    public void onOfflineModeChanged(boolean b) {
//                                        Log.e("Error","OfflienMode"+b);
//                                    }
//
//                                    @Override
//                                    public void onAirplaneModeEnabled() {
//                                        Log.e("Error","Airplane");
//                                    }
//
//                                    @Override
//                                    public void onWifiScansDisabled() {
//                                        Log.e("Error","Wifi");
//                                    }
//
//                                    @Override
//                                    public void onBluetoothDisabled() {
//                                        Log.e("Error","Blue");
//                                    }
//
//                                    @Override
//                                    public void onCellDisabled() {
//                                        Log.e("Error","cell");
//                                    }
//
//                                    @Override
//                                    public void onGnssLocationDisabled() {
//                                        Log.e("Error","gnss");
//                                    }
//
//                                    @Override
//                                    public void onNetworkLocationDisabled() {
//                                        Log.e("Error","network");
//                                    }
//
//                                    @Override
//                                    public void onServiceError(ServiceError serviceError) {
//                                        Log.e("Error",serviceError.toString());
//                                    }
//
//                                    @Override
//                                    public void onPositioningError(PositioningError positioningError) {
//                                        Log.e("Error",positioningError.toString());
//                                    }
//
//                                    @Override
//                                    public void onWifiIndoorPositioningNotAvailable() {
//                                        Log.e("Error","wifiindoor");
//                                    }
//
//                                    @Override
//                                    public void onWifiIndoorPositioningDegraded() {
//                                        Log.e("Error","degraded");
//                                    }
//                                });
//
//                                if(mHereLocation ==null){
//                                    Toast.makeText(getActivity(), "LocationDataSourceHERE.getInstance(): failed", Toast.LENGTH_LONG).show();
//                                    return;
//                                }
//                                mPositioningManager.setDataSource(mHereLocation);
//                                mPositioningManager.addListener(new WeakReference<PositioningManager.OnPositionChangedListener>(ProfileFragment.this));
//                                latLng.setText(getActivity().getString(R.string.gettinglocation));
//                                if (mPositioningManager.start(PositioningManager.LocationMethod.GPS_NETWORK)) {
//                                    latLng.setText(getActivity().getString(R.string.updating));
//                                } else {
//                                    Toast.makeText(getActivity(), "PositioningManager.start: failed, exiting", Toast.LENGTH_LONG).show();
//                                }
//                        }
//                    });
//                }
//            }
//        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                updateDB();
            }
        });


    }

    public void updateDB(){

        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Updating..");
        dialog.setCancelable(false);
        mobile = mobileEditText.getText()!=null?mobileEditText.getText().toString():"";
        address = addressEditText.getText()!=null?addressEditText.getText().toString():"";
        userName = usernameEditText.getText()!=null?usernameEditText.getText().toString():"";
        email = emailEditText.getText()!=null?emailEditText.getText().toString():"";

        final SharedPreferences.Editor editor = sharedPreferences.edit();
        //Map<String, Object> userDetails = new HashMap<>();
        if ( userName !=null && email !=null && address!=null && mobile !=null) {
            dialog.show();
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference(Utils.getUserKey());

//            userDetails.put("name", userName);
//            userDetails.put("email", email);
//            userDetails.put("mobile", mobile);
//            userDetails.put("address",address);

            ProfileModel profileModel = new ProfileModel();
            profileModel.setAddress(address);
            profileModel.setName(userName);
            profileModel.setMobile(mobile);
            profileModel.setEmail(email);
            profileModel.setLocation("xx");

            //userDetails.put("location",geoCoordinate);
            reference.child(user.getUid()).child(Utils.getProfileKey())
                    .setValue(profileModel)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            editor.putBoolean("check",true);
                            editor.putString(Utils.getMobileKey(),mobile);
                            editor.putString(Utils.getEmailKey(),email);
                            editor.putString(Utils.getAddressKey(),address);
                            editor.putString(Utils.getNameKey(),userName);
                            editor.apply();
                            dialog.dismiss();
                            Log.d("TAG", "Profile Updated");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            dialog.dismiss();
                            Toast.makeText(getContext(), "Update Failed", Toast.LENGTH_SHORT).show();
                            Log.w("TAG", "Error writing to database", e);
                        }
                    });
        }else{
            Toast.makeText(getActivity(), "Enter the details!", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onPositionUpdated(PositioningManager.LocationMethod locationMethod, GeoPosition geoPosition, boolean b) {
        final GeoCoordinate coordinate = geoPosition.getCoordinate();
        latLng.setText(String.format("%s,%s", coordinate.getLatitude(), coordinate.getLongitude()));
        Log.e("Coordinates",coordinate.getLatitude()+","+coordinate.getLongitude());
    }

    @Override
    public void onPositionFixChanged(PositioningManager.LocationMethod locationMethod, PositioningManager.LocationStatus locationStatus) {

    }
}
