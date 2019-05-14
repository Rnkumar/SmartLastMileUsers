package com.hackthon.here.sqlitedb;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class NotificationData {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo
    private String driverName;

    @ColumnInfo
    private String message;

    @ColumnInfo
    private String localDateTime;

    @ColumnInfo
    private String mobile;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(String localDateTime) {
        this.localDateTime = localDateTime;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
