package com.hackthon.here.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hackthon.here.R;
import com.hackthon.here.sqlitedb.NotificationData;
import com.hackthon.here.viewholders.NotificationViewHolder;

import java.util.List;

public class NotificationsListAdapter extends RecyclerView.Adapter<NotificationViewHolder> {

    private List<NotificationData> notificationDataList;

    public NotificationsListAdapter(List<NotificationData> notificationDataList) {
        this.notificationDataList = notificationDataList;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_notification,viewGroup,false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder notificationViewHolder, int i) {
        notificationViewHolder.setData(notificationDataList.get(i));
    }

    @Override
    public int getItemCount() {
        return notificationDataList.size();
    }

}
