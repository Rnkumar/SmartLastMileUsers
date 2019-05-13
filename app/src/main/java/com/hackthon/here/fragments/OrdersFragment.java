package com.hackthon.here.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hackthon.here.R;
import com.hackthon.here.Utils;
import com.hackthon.here.models.HistoryModel;
import com.hackthon.here.models.OrdersModel;
import com.hackthon.here.models.SubOrdersModel;
import com.hackthon.here.viewholders.HistoryViewHolder;
import com.hackthon.here.viewholders.OrdersViewHolder;
import com.hackthon.here.viewholders.SubOrdersViewHolder;

import java.util.HashMap;
import java.util.Map;

import okhttp3.internal.Util;


public class OrdersFragment extends Fragment {

    private LayoutInflater inflater;
    private ViewGroup container;
    private FirebaseUser currentUser;
    public OrdersFragment() {
        // Required empty public constructor
    }

    public static OrdersFragment newInstance(String param1, String param2) {
        OrdersFragment fragment = new OrdersFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.inflater = inflater;
        this.container = container;
        return inflater.inflate(R.layout.fragment_orders, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FirebaseAuth auth = FirebaseAuth.getInstance();

        currentUser = auth.getCurrentUser();
        RecyclerView recyclerView = view.findViewById(R.id.orders_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        FloatingActionButton addOrder = view.findViewById(R.id.addOrderBtn);
        addOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItemDialog(inflater,container);
            }
        });


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(Utils.getUserKey()).child(currentUser.getUid()).child(Utils.getOrdersKey());
        FirebaseRecyclerOptions<SubOrdersModel> options =
                new FirebaseRecyclerOptions.Builder<SubOrdersModel>()
                        .setQuery(reference, SubOrdersModel.class)
                        .build();

        FirebaseRecyclerAdapter adapter = new FirebaseRecyclerAdapter<SubOrdersModel, SubOrdersViewHolder>(options) {
            @Override
            public SubOrdersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_order, parent, false);

                return new SubOrdersViewHolder(view);
            }

            @Override
            public void onDataChanged() {
                super.onDataChanged();
                Log.e("Changed","DataChanged");
            }

            @Override
            public int getItemCount() {
                Log.e("count",super.getItemCount()+"");
                return super.getItemCount();
            }

            @Override
            protected void onBindViewHolder(SubOrdersViewHolder holder, int position, SubOrdersModel model) {
                holder.setData(model);
            }
        };

        recyclerView.setAdapter(adapter);
    }

    public void addItemDialog(LayoutInflater inflater,ViewGroup parent){
        final boolean delivered=false, published=false;
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        View v = inflater.inflate(R.layout.popup_add_item,parent,false);
        dialog.setView(v);

        final TextInputEditText itemNameEditText = v.findViewById(R.id.item_name);
        final TextInputEditText itemQuantityEditText = v.findViewById(R.id.item_quantity);
        final TextInputEditText addressEditText = v.findViewById(R.id.address);
        final TextInputEditText mobileEditText = v.findViewById(R.id.mobile);


        dialog.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String itemName = itemNameEditText.getText().toString();
                int quantity = Integer.parseInt(itemQuantityEditText.getText().toString());
                String address = addressEditText.getText().toString();
                String mobile = mobileEditText.getText().toString();
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("deliveries").child("ToBeDelivered").push();

                SubOrdersModel subOrdersModel = new SubOrdersModel();
                subOrdersModel.setAddress(address);
                subOrdersModel.setDelivered(delivered);
                subOrdersModel.setPublished(published);
                subOrdersModel.setItemName(itemName);
                subOrdersModel.setMobile(mobile);
                subOrdersModel.setQuantity(quantity);
                subOrdersModel.setUserId(currentUser.getUid());
//                Map<String,Object> values = new HashMap<>();
//                values.put("Address",address);
//                values.put("delivered",delivered);
//                values.put("published",published);
//                values.put("ItemName",itemName);
//                values.put("Quantity",quantity);
//                values.put("mobile",mobile);

                DatabaseReference userref = FirebaseDatabase.getInstance().getReference(Utils.getUserKey()).child(currentUser.getUid()).child(Utils.getOrdersKey());
                userref.setValue(subOrdersModel);

                //values.put("userId",currentUser.getUid());

                reference.setValue(subOrdersModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(getActivity(), "added", Toast.LENGTH_SHORT).show();
                        }else{
                            Log.e("Error",task.getException().getMessage());
                        }
                    }
                });

                dialog.dismiss();
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Toast.makeText(getActivity(), "Cancelled Order", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
    }
}
