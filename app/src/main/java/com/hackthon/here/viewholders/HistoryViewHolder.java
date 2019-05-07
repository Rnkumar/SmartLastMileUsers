package com.hackthon.here.viewholders;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.hackthon.here.R;
import com.hackthon.here.models.HistoryModel;

public class HistoryViewHolder extends RecyclerView.ViewHolder {

    private TextView itemName, deliveryDate, cost, location;

    public HistoryViewHolder(@NonNull View itemView) {
        super(itemView);
        initialize(itemView);
    }

    public void initialize(View view){
        itemName = view.findViewById(R.id.history_item_name);
        deliveryDate = view.findViewById(R.id.history_delivery_date);
        cost = view.findViewById(R.id.history_item_cost);
        location = view.findViewById(R.id.history_item_delivery_location);
    }

    public void setData(HistoryModel model){
        itemName.setText(model.getItemName());
        deliveryDate.setText(model.getDateOfDelivery());
        cost.setText(model.getCost());
        location.setText(model.getLocation());
    }
}
