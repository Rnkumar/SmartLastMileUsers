package com.hackthon.here.viewholders;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hackthon.here.R;
import com.hackthon.here.Utils;
import com.hackthon.here.models.ReturnsModel;
import com.hackthon.here.models.SubOrdersModel;

public class ReturnsViewHolder extends RecyclerView.ViewHolder {

    private TextView orderId, quantity, name, dateOfDelivery,deliveryLocation, reason;

    public ReturnsViewHolder(@NonNull View itemView) {
        super(itemView);
        init(itemView);
    }

    private void init(View view){
        orderId = view.findViewById(R.id.return_item_id);
        quantity = view.findViewById(R.id.return_item_quantity);
        name = view.findViewById(R.id.return_item_name);
        dateOfDelivery = view.findViewById(R.id.return_delivery_date);
        deliveryLocation = view.findViewById(R.id.return_item_delivery_location);
        reason = view.findViewById(R.id.return_reason);
    }

    public void setData(ReturnsModel returnsModel){
        orderId.setText(String.format("%s%s", orderId.getText().toString(), returnsModel.getItemName()));
        quantity.setText(String.format("%s%s", quantity.getText().toString(), String.valueOf(returnsModel.getQuantity())));
        deliveryLocation.setText(String.format("%s%s", deliveryLocation.getText(), returnsModel.getAddress()));
        dateOfDelivery.setText(Utils.formatDate(returnsModel.getReturnDate()));
        name.setText(String.format("%s%s", name.getText().toString(), returnsModel.getItemName()));
        reason.setText(String.format("%s%s", reason.getText().toString(), returnsModel.getReason()));
    }

}
