package com.hackthon.here.services;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.hackthon.here.interfaces.PositionUpdatedListener;
import com.here.android.mpa.common.ApplicationContext;
import com.here.android.mpa.common.GeoPosition;
import com.here.android.mpa.common.LocationDataSourceHERE;
import com.here.android.mpa.common.MapEngine;
import com.here.android.mpa.common.PositioningManager;

import java.io.File;
import java.lang.ref.WeakReference;

public class PositioningService implements PositioningManager.OnPositionChangedListener{
    AppCompatActivity m_activity;
    LocationDataSourceHERE m_hereDataSource;
    public static double latitude,longitude;

    private PositionUpdatedListener positionUpdatedListener;

   int count=0;

   public void getPos(AppCompatActivity m_activity) {
        this.m_activity=m_activity;
        initMapEngine();
    }

    public void setPositionUpdatedListener(PositionUpdatedListener positionUpdatedListener){
        this.positionUpdatedListener = positionUpdatedListener;
    }



    private void initMapEngine() {
            boolean success = com.here.android.mpa.common.MapSettings.setIsolatedDiskCacheRootPath(
                    m_activity.getExternalFilesDir(null) + File.separator + ".here-maps",
                    "com.here2k19.projects.smartlastmileuser.MapService");
            if (!success) {
            } else {
                MapEngine.getInstance().init(new ApplicationContext(m_activity), error -> {
                    Log.e("MapINit", "Hello Map Engine initialized with error code:" + error);
                    m_hereDataSource = LocationDataSourceHERE.getInstance();
                    if (m_hereDataSource != null) {
                        PositioningManager pm = PositioningManager.getInstance();
                        //pm.start(PositioningManager.LocationMethod.GPS_NETWORK);
                        pm.setDataSource(m_hereDataSource);
                        pm.addListener(new WeakReference<>(PositioningService.this));
                        if (pm.start(PositioningManager.LocationMethod.GPS_NETWORK)) {
                            Log.e("poso", "position update started");
                        } else {

                        }
                    }

                });
                }
        }

    @Override
    public void onPositionUpdated(PositioningManager.LocationMethod locationMethod, GeoPosition geoPosition, boolean b) {
        if(count==0) {
            latitude = geoPosition.getCoordinate().getLatitude();
            longitude = geoPosition.getCoordinate().getLongitude();
            if (positionUpdatedListener!=null){
                positionUpdatedListener.getUpdate(latitude,longitude);
            }
        }
    }

    @Override
    public void onPositionFixChanged(PositioningManager.LocationMethod locationMethod, PositioningManager.LocationStatus locationStatus) {

    }
}


