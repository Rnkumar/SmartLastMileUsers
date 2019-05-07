package com.hackthon.here.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hackthon.here.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ReturnListFragment extends Fragment {


    public ReturnListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_return_list, container, false);
    }

}
