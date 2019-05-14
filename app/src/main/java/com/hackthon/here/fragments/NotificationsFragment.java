package com.hackthon.here.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.hackthon.here.MainApplication;
import com.hackthon.here.R;
import com.hackthon.here.adapters.NotificationsListAdapter;
import com.hackthon.here.sqlitedb.NotificationData;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class NotificationsFragment extends Fragment {


    private List<NotificationData> list;
    private NotificationsListAdapter adapter;
    private TextView noNotificationsTextView;
    private Button refresh_btn;
    public NotificationsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notifications, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        refresh_btn = view.findViewById(R.id.notifications_refresh_btn);
        refresh_btn.setOnClickListener(v -> updateNotifications());
        noNotificationsTextView = view.findViewById(R.id.no_notifications_text);
        RecyclerView recyclerView =  view.findViewById(R.id.notifications_recycler_view);

        list = new ArrayList<>();
        adapter = new NotificationsListAdapter(list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        if (list.size()>0){
            makeVisible(false);
        }else{
            makeVisible(true);
        }
        updateNotifications();

    }

    private void makeVisible(boolean b) {
        if (b){
            if (noNotificationsTextView.getVisibility() == View.GONE){
                noNotificationsTextView.setVisibility(View.VISIBLE);
            }
        }else{
            if (noNotificationsTextView.getVisibility() == View.VISIBLE){
                noNotificationsTextView.setVisibility(View.GONE);
            }
        }
    }

    public void updateNotifications(){
        refresh_btn.setText(getActivity().getString(R.string.loading));
        refresh_btn.setEnabled(false);
        new Thread(() -> {
            List<NotificationData> data = ((MainApplication)getActivity().getApplicationContext()).getNotificationDataDb().notificationDAO().getAll();
            if(data!=null)
                list.addAll(data);
            getActivity().runOnUiThread(() -> {
                refresh_btn.setText(getActivity().getString(R.string.refreshText));
                refresh_btn.setEnabled(true);
                if (list.size()>0){
                    makeVisible(false);
                }else{
                    makeVisible(true);
                }
            });
        }).start();
    }

}
