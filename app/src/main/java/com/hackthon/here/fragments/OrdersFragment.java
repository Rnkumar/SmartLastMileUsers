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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
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
import com.hackthon.here.services.AutoCompleteService;
import com.hackthon.here.services.GeoCodingService;
import com.hackthon.here.viewholders.SubOrdersViewHolder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class OrdersFragment extends Fragment {

    private LayoutInflater inflater;
    private ViewGroup container;
    private FirebaseUser currentUser;
    private FirebaseRecyclerAdapter adapter;
    private TextView noOrderTextView;

    public OrdersFragment() {

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
                holder.setData(model,getActivity());
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
        Spinner spinner = v.findViewById(R.id.spinner);
        List<String> list = new ArrayList<>();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(v.getContext(), R.layout.support_simple_spinner_dropdown_item, list);
        spinner.setAdapter(arrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e("data",spinner.getSelectedItem()+"");
                String selectedItem = String.valueOf(spinner.getSelectedItem());
                addressEditText.setText(selectedItem);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        AutoCompleteService autoCompleteService = new AutoCompleteService(v.getContext(), jsonArray -> {
            list.clear();
            Log.e("Data",jsonArray.toString());
            for(int i=0;i<jsonArray.length();i++){
                try {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    list.add(jsonObject.getString("label"));
                    arrayAdapter.notifyDataSetChanged();
                    spinner.performClick();
                } catch (JSONException e) {
                    Log.e("Error:",e.getMessage());
                }
            }
        });

        addressEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.e("input",count+"");
                if(s.length() > 0 && s.charAt(s.length()-1) == ' ' ){
                    autoCompleteService.getAutoComplete(s.toString(),getString(R.string.app_id),getString(R.string.app_code));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


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


            GeoCodingService geoCodingService = new GeoCodingService(getActivity(), (latitude, longitude) -> {
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


                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("deliveries").child("ToBeDelivered").child(/*Utils.formatDate(new Date())*/"15-05-2019").child(key);
                subOrdersModel.setLocation(latitude+","+longitude);
                userref.setValue(subOrdersModel);
                reference.setValue(subOrdersModel);
                Toast.makeText(getActivity(), "Added", Toast.LENGTH_SHORT).show();
            });

            geoCodingService.geoCodeAddress(address,getActivity().getString(R.string.app_id),getActivity().getString(R.string.app_code));

            String[] nameArr = {"Radio", "WalkieTalkie", "PlayDoll", "Bike"};
            String[] mobileArr = {"9494949494", "9898989898", "9797979797", "9969696969"};
            String[] addressArr = {"No.2, native street", "No.125, Ambedkar street", "No.10, dravid street", "No.4, Dhoni street"};
            String[] locations = {"13.047917,77.620978", "13.054939, 77.632518", "13.064722, 77.634321", "13.042231, 77.625050"};

            for(int i=0;i<4;i++){
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("deliveries").child("ToBeDelivered").child("15-05-2019").child(i+"");
                SubOrdersModel model = new SubOrdersModel();
                model.setAddress(addressArr[i]);
                model.setDelivered(delivered);
                model.setPublished(published);
                model.setLocation(locations[i]);
                model.setItemName(nameArr[i]);
                model.setMobile(mobileArr[i]);
                model.setQuantity(quantity);
                model.setUserId("ccd");
                model.setOrderId(i+"");
                model.setDate(new Date());
                ref.setValue(model);
            }

//            reference.setValue(subOrdersModel).addOnCompleteListener(task -> {
//                if (task.isSuccessful()){
//                    Toast.makeText(getActivity(), "added", Toast.LENGTH_SHORT).show();
//                }else{
//                    Log.e("Error",task.getException().getMessage());
//                }
//            });
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
