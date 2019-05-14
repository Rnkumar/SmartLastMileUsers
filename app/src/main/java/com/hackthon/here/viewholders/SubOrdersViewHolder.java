package com.hackthon.here.viewholders;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hackthon.here.R;
import com.hackthon.here.Utils;
import com.hackthon.here.models.SubOrdersModel;

public class SubOrdersViewHolder extends RecyclerView.ViewHolder {

    private TextView orderId, quantity, name, dateOfDelivery,deliveryLocation;
    Button trackBtn, returnBtn;

    public SubOrdersViewHolder(@NonNull View itemView) {
        super(itemView);
        init(itemView);
    }

    private void init(View view){
        orderId = view.findViewById(R.id.order_item_id);
        quantity = view.findViewById(R.id.order_item_quantity);
        name = view.findViewById(R.id.order_item_name);
        dateOfDelivery = view.findViewById(R.id.order_delivery_date);
        deliveryLocation = view.findViewById(R.id.order_item_delivery_location);
        trackBtn = view.findViewById(R.id.button_track);
        returnBtn = view.findViewById(R.id.button_return);
    }

    public void setData(SubOrdersModel ordersModel){
        orderId.setText(String.format("%s%s", orderId.getText().toString(), ordersModel.getItemName()));
        quantity.setText(String.format("%s%s", quantity.getText().toString(), String.valueOf(ordersModel.getQuantity())));
        deliveryLocation.setText(String.format("%s%s", deliveryLocation.getText(), ordersModel.getAddress()));
        dateOfDelivery.setText(Utils.formatDate(ordersModel.getDate()));
        name.setText(String.format("%s%s", name.getText().toString(), ordersModel.getItemName()));

        if (ordersModel.isDelivered()){
            returnBtn.setEnabled(true);
        }

        if (ordersModel.isPublished()){
            trackBtn.setEnabled(true);
        }

        trackBtn.setOnClickListener(v -> {

        });
        returnBtn.setOnClickListener(v -> {

        });
        //deliveryLocation.setText(ordersModel.getDeliveryLocation());
    }
}
