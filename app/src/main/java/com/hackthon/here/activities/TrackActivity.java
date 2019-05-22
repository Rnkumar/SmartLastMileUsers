package com.hackthon.here.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hackthon.here.R;
import com.hackthon.here.Utils;
import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.common.OnEngineInitListener;
import com.here.android.mpa.mapping.Map;
import com.here.android.mpa.mapping.MapMarker;
import com.here.android.mpa.mapping.MapRoute;
import com.here.android.mpa.mapping.SupportMapFragment;
import com.here.android.mpa.routing.Route;
import com.here.odnp.util.Log;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

public class TrackActivity extends AppCompatActivity {

    private final static int REQUEST_CODE_ASK_PERMISSIONS = 1;
    private static final String[] RUNTIME_PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.ACCESS_NETWORK_STATE
    };

    String driverId;

    private SupportMapFragment m_mapFragment;
    private Map m_map;
    private String itemName, driverName, driverMobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track);

        Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.track_driver));

        if (getIntent().getExtras() != null) {
            driverId = getIntent().getExtras().getString("driverId");
            itemName = getIntent().getExtras().getString("itemName");
            driverName = getIntent().getExtras().getString("driverName");
            driverMobile = getIntent().getExtras().getString("driverMobile");
        }

        TextView itemNameTextView = findViewById(R.id.item_name);
        TextView driverNameTextView = findViewById(R.id.drivername);

        TextView writeFeedback  = findViewById(R.id.feedback_driver_btn);
        writeFeedback.setOnClickListener(v -> {
                feedbackDialog();
        });

        itemNameTextView.setText(itemName);
        driverNameTextView.setText(driverName);

        FloatingActionButton callButton = findViewById(R.id.call_driver_btn);
        callButton.setOnClickListener(v -> {
            if (ActivityCompat.checkSelfPermission(TrackActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.CALL_PHONE},2002);
                }
                return;
            }
            call();
        });

        if (hasPermissions(this, RUNTIME_PERMISSIONS)) {
            setupMapFragmentView();
        } else {
            ActivityCompat
                    .requestPermissions(this, RUNTIME_PERMISSIONS, REQUEST_CODE_ASK_PERMISSIONS);
        }
    }

    /**
     * Only when the app's target SDK is 23 or higher, it requests each dangerous permissions it
     * needs when the app is running.
     */
    private static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission)
                        != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.home,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS: {
                for (int index = 0; index < permissions.length; index++) {
                    if (grantResults[index] != PackageManager.PERMISSION_GRANTED) {
                        if (!ActivityCompat
                                .shouldShowRequestPermissionRationale(this, permissions[index]))
                            Toast.makeText(this, "Required permission " + permissions[index]
                                            + " not granted. "
                                            + "Please go to settings and turn on for sample app",
                                    Toast.LENGTH_LONG).show();
                        else {
                            Toast.makeText(this, "Required permission " + permissions[index]
                                    + " not granted", Toast.LENGTH_LONG).show();
                        }
                    }
                }

                setupMapFragmentView();
                break;
            }
            case 2002:
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "Required Call Permissions", Toast.LENGTH_SHORT).show();
                }else{
                    call();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @SuppressLint("MissingPermission")
    private void call() {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + driverMobile));//change the number.
        startActivity(callIntent);
    }

    private void setupMapFragmentView() {
       initMapFragment();
    }

    private SupportMapFragment getMapFragment() {
        return (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapfragment);
    }

    private void initMapFragment() {
        /* Locate the mapFragment UI element */
        m_mapFragment = getMapFragment();

        // Set path of isolated disk cache
        String diskCacheRoot = Environment.getExternalStorageDirectory().getPath()
                + File.separator + ".isolated-here-maps";
        // Retrieve intent name from manifest
        String intentName = "com.here2k19.projects.smartlastmileuser.MapService";
        boolean success = com.here.android.mpa.common.MapSettings.setIsolatedDiskCacheRootPath(diskCacheRoot, intentName);
        if (!success) {

        } else {
            if (m_mapFragment != null) {
                m_mapFragment.init(new OnEngineInitListener() {
                    @Override
                    public void onEngineInitializationCompleted(OnEngineInitListener.Error error) {
                        if (error == Error.NONE) {
                            m_map = m_mapFragment.getMap();
                            m_mapFragment.getPositionIndicator().setVisible(true);
                            m_map.setTrafficInfoVisible(true);
                            m_map.setUseSystemLanguage();
                            m_map.setLandmarksVisible(true);

                            MapMarker marker = new MapMarker();
                            marker.setCoordinate(new GeoCoordinate(49.259149, -123.008555, 0.0));
                            m_map.addMapObject(marker);

                            m_map.setCenter(new GeoCoordinate(49.259149, -123.008555, 0.0),
                                    Map.Animation.NONE);
                            m_map.setZoomLevel(10);

                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Utils.getDriverKey()).child(driverId).child(Utils.getDriverLocationKey());
                            databaseReference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    HashMap<String,Double> location = (HashMap<String,Double>)dataSnapshot.getValue();
                                    GeoCoordinate geoCoordinate = new GeoCoordinate(location.get("latitude"),location.get("longitude"));
                                    marker.setCoordinate(geoCoordinate);
                                    m_map.setCenter(geoCoordinate,
                                            Map.Animation.NONE);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    Log.e("Error",databaseError.getMessage());
                                }
                            });
                            loadSerializedData(driverId);
                        } else {
                            Log.e(this.getClass().toString(), "onEngineInitializationCompleted: " +
                                    "ERROR=" + error.getDetails(), error.getThrowable());
                        }
                    }
                });
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()){
//            case R.id.updatedriverlocation:
//                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Utils.getDriverKey()).child("sdjfhdskjfhh").child(Utils.getDriverLocationKey());
//                databaseReference.setValue(new GeoCoordinate(12.3454,-121.333));
//                break;
//        }
        return super.onOptionsItemSelected(item);
    }

    public void feedbackDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Feedback");
        LinearLayout layout  = new LinearLayout(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layout.setLayoutParams(layoutParams);
        layout.setGravity(Gravity.CENTER);
        EditText feedbackEditText = new EditText(this);
        feedbackEditText.setEms(10);
        layout.addView(feedbackEditText);
        builder.setView(layout);
        builder.setCancelable(false);

        builder.setPositiveButton("Submit", (dialog, which) -> {
            dialog.dismiss();
            String comment = feedbackEditText.getText().toString();
            SharedPreferences sp = getSharedPreferences(Utils.getSharedPreferenceName(),Context.MODE_PRIVATE);
            String name = sp.getString(Utils.getNameKey(),"");
            //String mobile = sp.getString(Utils.getMobileKey(),"");
            java.util.Map<String,String> map = new HashMap<>();
            map.put("Name",name);
            map.put("comment",comment);
            map.put("date",Utils.formatDate(new Date()));
            map.put("drivername",driverName);
            map.put("location","Chennai");

            FirebaseDatabase.getInstance().getReference("feedbacks").push().setValue(map);
        }).setNegativeButton("Cancel", (dialog, which) -> {
            dialog.dismiss();
        });

        builder.show();
    }


    public void loadSerializedData(String driverId){
        FirebaseDatabase.getInstance().getReference().child(Utils.getDriverKey()).child(driverId).child(Utils.getDriverRouteKey()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Log.e("Data","DAta");

                String values;
                if(dataSnapshot.getValue() != null) {
                    String value = String.valueOf(dataSnapshot.getValue());
                    values = value;
                }else{
                    Log.e("Value","Failed");
                    return;
                }
                Log.e("Value: ",values);
                try {
                    byte[] data = Base64.decode(values, Base64.DEFAULT);
                    Route.DeserializationCallback dCallback = deserializationResult -> {
                        Route route = deserializationResult.route;
                        runOnUiThread(() -> {
                            MapRoute mapRoute = new MapRoute(route);
                            m_map.addMapObject(mapRoute);
                        });

                    };
                    Route.deserializeAsync(data,dCallback);
                }catch(Exception e){
                    Log.e("Error",e.getMessage());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("error",databaseError.getMessage());
            }
        });
    }

//    class AsyncTasks extends AsyncTask<String,Void,Void>{
//
//        @Override
//        protected Void doInBackground(String... strings) {
//            loadSerializedData(strings[0]);
//            return null;
//        }
//    }

}
