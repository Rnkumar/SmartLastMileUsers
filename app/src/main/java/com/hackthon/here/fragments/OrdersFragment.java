package com.hackthon.here.fragments;

import android.app.AlertDialog;
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
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.hackthon.here.R;
import com.hackthon.here.Utils;
import com.hackthon.here.models.SubOrdersModel;
import com.hackthon.here.viewholders.SubOrdersViewHolder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class OrdersFragment extends Fragment {

    private LayoutInflater inflater;
    private ViewGroup container;
    private FirebaseUser currentUser;
    private FirebaseRecyclerAdapter adapter;
    private TextView noOrderTextView;

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

        noOrderTextView = view.findViewById(R.id.no_orders_text);

        FloatingActionButton addOrder = view.findViewById(R.id.addOrderBtn);
        addOrder.setOnClickListener(v -> addItemDialog(inflater,container));


        Query reference = FirebaseDatabase.getInstance().getReference(Utils.getUserKey()).child(currentUser.getUid()).child(Utils.getOrdersKey()).orderByPriority();
        FirebaseRecyclerOptions<SubOrdersModel> options =
                new FirebaseRecyclerOptions.Builder<SubOrdersModel>()
                        .setQuery(reference, SubOrdersModel.class)
                        .build();


        adapter = new FirebaseRecyclerAdapter<SubOrdersModel, SubOrdersViewHolder>(options) {
            @Override
            public SubOrdersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_order, parent, false);

                return new SubOrdersViewHolder(view);
            }

            @Override
            public void onDataChanged() {
                super.onDataChanged();
            }

            @Override
            public int getItemCount() {
                if(super.getItemCount()>0){
                    shuffleDisplay(false);
                }else{
                    shuffleDisplay(true);
                }
                return super.getItemCount();
            }

            @Override
            protected void onBindViewHolder(SubOrdersViewHolder holder, int position, SubOrdersModel model) {
                holder.setData(model);
            }
        };

        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (adapter!=null)
        adapter.stopListening();
    }

    public void addItemDialog(LayoutInflater inflater, ViewGroup parent){
        final boolean delivered=false, published=false;
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        View v = inflater.inflate(R.layout.popup_add_item,parent,false);
        dialog.setView(v);

        final TextInputEditText itemNameEditText = v.findViewById(R.id.item_name);
        final TextInputEditText itemQuantityEditText = v.findViewById(R.id.item_quantity);
        final TextInputEditText addressEditText = v.findViewById(R.id.address);
        final TextInputEditText mobileEditText = v.findViewById(R.id.mobile);


        dialog.setPositiveButton("Add", (dialog12, which) -> {

            String itemName = itemNameEditText.getText().toString();
            int quantity = Integer.parseInt(itemQuantityEditText.getText().toString());
            String address = addressEditText.getText().toString();
            String mobile = mobileEditText.getText().toString();

//                Map<String,Object> values = new HashMap<>();
//                values.put("Address",address);
//                values.put("delivered",delivered);
//                values.put("published",published);
//                values.put("ItemName",itemName);
//                values.put("Quantity",quantity);
//                values.put("mobile",mobile);

            DatabaseReference userref = FirebaseDatabase.getInstance().getReference(Utils.getUserKey()).child(currentUser.getUid()).child(Utils.getOrdersKey()).push();
            String key = userref.getKey();
            SubOrdersModel subOrdersModel = new SubOrdersModel();
            subOrdersModel.setAddress(address);
            subOrdersModel.setDelivered(delivered);
            subOrdersModel.setPublished(published);
            subOrdersModel.setItemName(itemName);
            subOrdersModel.setMobile(mobile);
            subOrdersModel.setQuantity(quantity);
            subOrdersModel.setUserId(currentUser.getUid());
            subOrdersModel.setOrderId(key);
            subOrdersModel.setDate(new Date());
            userref.setValue(subOrdersModel);

            //values.put("userId",currentUser.getUid());

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("deliveries").child("ToBeDelivered").child(Utils.formatDate(new Date())).child(key);
            reference.setValue(subOrdersModel);



            reference.setValue(subOrdersModel).addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    Toast.makeText(getActivity(), "added", Toast.LENGTH_SHORT).show();
                }else{
                    Log.e("Error",task.getException().getMessage());
                }
            });

            dialog12.dismiss();
        }).setNegativeButton("Cancel", (dialog1, which) -> {
            dialog1.dismiss();
            Toast.makeText(getActivity(), "Cancelled Order", Toast.LENGTH_SHORT).show();
        });
        dialog.show();
    }

    public void shuffleDisplay(boolean b){
        if (b){
            if (noOrderTextView.getVisibility() == View.GONE){
                noOrderTextView.setVisibility(View.VISIBLE);
            }
        }else{
            if (noOrderTextView.getVisibility() == View.VISIBLE){
                noOrderTextView.setVisibility(View.GONE);
            }
        }
    }


}
