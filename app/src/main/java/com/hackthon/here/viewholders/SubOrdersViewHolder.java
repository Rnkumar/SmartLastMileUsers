package com.hackthon.here.viewholders;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hackthon.here.R;
import com.hackthon.here.Utils;
import com.hackthon.here.activities.TrackActivity;
import com.hackthon.here.models.ReturnsModel;
import com.hackthon.here.models.SubOrdersModel;

import java.util.Date;
import java.util.Objects;

public class SubOrdersViewHolder extends RecyclerView.ViewHolder {

    private TextView orderId, quantity, name, dateOfDelivery,deliveryLocation;
    private Button trackBtn, returnBtn;

    public SubOrdersViewHolder(@NonNull View itemView) {
        super(itemView);
        init(itemView);
    }

    private void init(View view){
        orderId = view.findViewById(R.id.order_item_id);
        quantity = view.findViewById(R.id.return_item_quantity);
        name = view.findViewById(R.id.return_item_name);
        dateOfDelivery = view.findViewById(R.id.return_delivery_date);
        deliveryLocation = view.findViewById(R.id.return_item_delivery_location);
        trackBtn = view.findViewById(R.id.button_track);
        returnBtn = view.findViewById(R.id.button_return);
    }

    public void setData(SubOrdersModel ordersModel,Context context){
        orderId.setText(String.format("%s%s", "OrderId: ", ordersModel.getOrderId()));
        quantity.setText(String.format("%s%s","Quantity: ", String.valueOf(ordersModel.getQuantity())));
        deliveryLocation.setText(String.format("%s%s", "Address: ", ordersModel.getAddress()));
        dateOfDelivery.setText(Utils.formatDate(ordersModel.getDate()));
        name.setText(String.format("%s%s","ItemName: ", ordersModel.getItemName()));

        if (ordersModel.isDelivered()){
            returnBtn.setEnabled(true);
        }

        if (ordersModel.isPublished()){
            trackBtn.setEnabled(true);
        }

        trackBtn.setOnClickListener(v -> {
            if(ordersModel.isPublished()) {
                Intent trackIntent = new Intent(context, TrackActivity.class);
                trackIntent.putExtra("driverId", ordersModel.getDriverId());
                trackIntent.putExtra("itemName", ordersModel.getItemName());
                trackIntent.putExtra("driverName", ordersModel.getDriverName());
                trackIntent.putExtra("driverMobile", ordersModel.getDriverMobile());
                context.startActivity(trackIntent);
            }else{
                Toast.makeText(context, "Not published for delivery yet!", Toast.LENGTH_SHORT).show();
            }
        });
        returnBtn.setOnClickListener(v -> {
            if(ordersModel.isDelivered()) {
                getAlertDialog(context, ordersModel.getOrderId(), ordersModel.getItemName());
            }else{
                Toast.makeText(context, "Not Delivered yet!", Toast.LENGTH_SHORT).show();
            }
        });
        //deliveryLocation.setText(ordersModel.getDeliveryLocation());
    }

    private void getAlertDialog(Context context,String id, String name){
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle("Return Confirmation");
        dialog.setMessage("Are you sure to return this item("+name+")?");
        dialog.setPositiveButton("Yes", (dialog12, which) -> {
            populateToReturnsAndDeleteFromOrders(id,"Nothing",new Date());
            dialog12.dismiss();
        }).setNegativeButton("No", (dialog1, which) -> dialog1.dismiss());
        dialog.setCancelable(false);
        dialog.show();
    }


    private void populateToReturnsAndDeleteFromOrders(String id, String reason, Date date){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference(Utils.getUserKey()).child(currentUser.getUid()).child(Utils.getOrdersKey()).child(id);
        DatabaseReference returnsRef = FirebaseDatabase.getInstance().getReference(Utils.getUserKey()).child(currentUser.getUid()).child(Utils.getReturnsKey()).child(id);
        DatabaseReference deliveriesRef = FirebaseDatabase.getInstance().getReference(Utils.getOrdersKey()).child(Utils.getReturnsKey());
        orderRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                SubOrdersModel subOrdersModel = dataSnapshot.getValue(SubOrdersModel.class);
                ReturnsModel returnsModel = new ReturnsModel();
                returnsModel.setAddress(Objects.requireNonNull(subOrdersModel).getAddress());
                returnsModel.setDate(subOrdersModel.getDate());
                returnsModel.setDelivered(subOrdersModel.isDelivered());
                returnsModel.setPublished(subOrdersModel.isPublished());
                returnsModel.setOrderId(subOrdersModel.getOrderId());
                returnsModel.setItemName(subOrdersModel.getItemName());
                returnsModel.setLocation(subOrdersModel.getLocation());
                returnsModel.setQuantity(subOrdersModel.getQuantity());
                returnsModel.setMobile(subOrdersModel.getMobile());
                returnsModel.setUserId(subOrdersModel.getUserId());
                returnsModel.setReturned(true);
                returnsModel.setReason(reason);
                returnsModel.setReturnDate(date);
                returnsRef.setValue(returnsModel);

                deliveriesRef.child(returnsModel.getOrderId()).setValue(subOrdersModel);

                orderRef.removeValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Error",databaseError.getMessage());
            }
        });
    }

}
