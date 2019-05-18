package com.hackthon.here.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.hackthon.here.R;
import com.hackthon.here.Utils;
import com.hackthon.here.models.ReturnsModel;
import com.hackthon.here.models.SubOrdersModel;
import com.hackthon.here.viewholders.ReturnsViewHolder;
import com.hackthon.here.viewholders.SubOrdersViewHolder;

public class ReturnListFragment extends Fragment {


    TextView noReturnsTextView;
    FirebaseRecyclerAdapter adapter;

    public ReturnListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_return_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FirebaseAuth auth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = auth.getCurrentUser();
        RecyclerView recyclerView = view.findViewById(R.id.returnList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        noReturnsTextView = view.findViewById(R.id.noreturnstext);

        Query reference = FirebaseDatabase.getInstance().getReference(Utils.getUserKey()).child(currentUser.getUid()).child(Utils.getReturnsKey()).orderByPriority();
        FirebaseRecyclerOptions<ReturnsModel> options =
                new FirebaseRecyclerOptions.Builder<ReturnsModel>()
                        .setQuery(reference, ReturnsModel.class)
                        .build();


        adapter = new FirebaseRecyclerAdapter<ReturnsModel, ReturnsViewHolder>(options) {
            @Override
            public ReturnsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_returns, parent, false);

                return new ReturnsViewHolder(view);
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
            protected void onBindViewHolder(ReturnsViewHolder holder, int position, ReturnsModel model) {
                holder.setData(model);
            }
        };

        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }

    public void shuffleDisplay(boolean b){
        if (b){
            if (noReturnsTextView.getVisibility() == View.GONE){
                noReturnsTextView.setVisibility(View.VISIBLE);
            }
        }else{
            if (noReturnsTextView.getVisibility() == View.VISIBLE){
                noReturnsTextView.setVisibility(View.GONE);
            }
        }
    }
}
