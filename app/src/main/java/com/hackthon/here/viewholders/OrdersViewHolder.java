package com.hackthon.here.viewholders;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.hackthon.here.R;
import com.hackthon.here.models.OrdersModel;

public class OrdersViewHolder extends RecyclerView.ViewHolder {

    private TextView orderId, cost, name, dateOfDelivery,deliveryLocation;

    public OrdersViewHolder(@NonNull View itemView) {
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

    public void setData(OrdersModel ordersModel){
        orderId.setText(ordersModel.getOrderId());
        cost.setText(ordersModel.getCost());
        name.setText(ordersModel.getItemname());
        dateOfDelivery.setText(ordersModel.getDateofDelivery());
        deliveryLocation.setText(ordersModel.getDeliveryLocation());
    }

}
