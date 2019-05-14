package com.hackthon.here;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.hackthon.here.sqlitedb.MyNotificationDataDb;

public class MainApplication extends Application {

    private static MyNotificationDataDb notificationDataDb;

    @Override
    public void onCreate() {
        super.onCreate();
        notificationDataDb = Room.databaseBuilder(getApplicationContext(), MyNotificationDataDb.class, "Database").build();
    }

    public MyNotificationDataDb getNotificationDataDb() {
        return notificationDataDb;
    }
}
