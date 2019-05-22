package com.hackthon.here.services;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.hackthon.here.interfaces.AutoCompleteListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AutoCompleteService {
    private Context context;
    private AutoCompleteListener autoCompleteListener;

    public AutoCompleteService(Context context, AutoCompleteListener autoCompleteListener) {
        this.context = context;
        this.autoCompleteListener = autoCompleteListener;
    }

    public void getAutoComplete(String searchText, String appId, String appCode) {
        String  url = "http://autocomplete.geocoder.api.here.com/6.2/suggest.json?query="+searchText+"&app_id="+appId+"&app_code="+appCode;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url,new JSONObject(), response -> {
            Log.e("Reso",response.toString());
            if(autoCompleteListener!=null){
                try {
                    JSONArray suggestionsArr = response.getJSONArray("suggestions");
                    autoCompleteListener.getSuggestions(suggestionsArr);
                } catch (JSONException e) {
                    Log.e("Error",e.getMessage());
                    autoCompleteListener.getSuggestions(new JSONArray());
                }

            }
        }, error -> {
            if(autoCompleteListener!=null){
                autoCompleteListener.getSuggestions(new JSONArray());
            }
            Log.e("Error",error.getMessage());
        });

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(jsonObjectRequest);

    }
}
