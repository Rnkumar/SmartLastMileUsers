package com.hackthon.here.viewholders;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.hackthon.here.R;
import com.hackthon.here.sqlitedb.NotificationData;

public class NotificationViewHolder extends RecyclerView.ViewHolder {

    private TextView driverName, driverMobile, notificationData, notificationDate;

    public NotificationViewHolder(@NonNull View itemView) {
        super(itemView);
        driverMobile = itemView.findViewById(R.id.item_notification_driver_mobile);
        driverName = itemView.findViewById(R.id.item_notification_driver_name);
        notificationData = itemView.findViewById(R.id.item_notification_data);
        notificationDate = itemView.findViewById(R.id.item_notification_date);
    }

    public void setData(NotificationData notificationData) {
        driverMobile.setText(notificationData.getMobile());
        driverName.setText(notificationData.getDriverName());
        this.notificationData.setText(notificationData.getMessage());
        notificationDate.setText(notificationData.getLocalDateTime());
    }
}
