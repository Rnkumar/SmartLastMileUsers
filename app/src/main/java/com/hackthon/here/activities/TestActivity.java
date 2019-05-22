package com.hackthon.here.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;

import com.hackthon.here.R;
import com.hackthon.here.services.AutoCompleteService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TestActivity extends AppCompatActivity {

    ArrayAdapter<String> arrayAdapter;
    List<String> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        EditText autoCompleteTextView = findViewById(R.id.auto);
        Spinner spinner = findViewById(R.id.spinner);
        list = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,list);
        spinner.setAdapter(arrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e("data",spinner.getSelectedItem()+"");
                String selectedItem = String.valueOf(spinner.getSelectedItem());
                autoCompleteTextView.setText(selectedItem);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        AutoCompleteService autoCompleteService = new AutoCompleteService(this, jsonArray -> {
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

        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.e("input",s.charAt(s.length()-1)+"");
                if(s.charAt(s.length()-1) == ' ' && count > 0){
                    autoCompleteService.getAutoComplete(s.toString(),getString(R.string.app_id),getString(R.string.app_code));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void getData(View view) {

    }
}
