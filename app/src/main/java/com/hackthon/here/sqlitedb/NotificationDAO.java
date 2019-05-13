package com.hackthon.here.sqlitedb;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface NotificationDAO {

    @Query("select * from notificationdata")
    List<NotificationData> getAll();

    @Insert
    void insertAll(NotificationData...notificationData);


}
