package com.hackthon.here.viewholders;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.hackthon.here.R;
import com.hackthon.here.models.OrdersModel;
import com.hackthon.here.models.SubOrdersModel;

public class SubOrdersViewHolder extends RecyclerView.ViewHolder {
    private TextView orderId, cost, name, dateOfDelivery,deliveryLocation;

    public SubOrdersViewHolder(@NonNull View itemView) {
        super(itemView);
        init(itemView);
    }

    private void init(View view){
        orderId = view.findViewById(R.id.order_item_id);
        cost = view.findViewById(R.id.order_item_cost);
        name = view.findViewById(R.id.order_item_name);
        dateOfDelivery = view.findViewById(R.id.order_delivery_date);
        deliveryLocation = view.findViewById(R.id.order_item_delivery_location);
    }

    public void setData(SubOrdersModel ordersModel){
        orderId.setText(ordersModel.getItemName());
        cost.setText(ordersModel.getQuantity());
        name.setText(ordersModel.getAddress());
        String isDelivered=ordersModel.isDelivered()?"Delivered":"Pending";
        dateOfDelivery.setText(isDelivered);
        //deliveryLocation.setText(ordersModel.getDeliveryLocation());
    }
}
