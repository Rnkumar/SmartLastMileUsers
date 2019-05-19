package com.hackthon.here;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.google.firebase.database.FirebaseDatabase;
import com.hackthon.here.sqlitedb.MyNotificationDataDb;

public class MainApplication extends Application {

    private static MyNotificationDataDb notificationDataDb;

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        notificationDataDb = Room.databaseBuilder(getApplicationContext(), MyNotificationDataDb.class, "Database").allowMainThreadQueries().build();

    }

    public MyNotificationDataDb getNotificationDataDb() {
        return notificationDataDb;
    }
}
