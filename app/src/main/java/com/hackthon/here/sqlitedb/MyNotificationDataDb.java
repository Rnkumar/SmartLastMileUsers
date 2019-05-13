package com.hackthon.here.sqlitedb;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {NotificationData.class}, version=1)
public abstract class MyNotificationDataDb extends RoomDatabase {
    public abstract NotificationDAO notificationDAO();
}
