package com.hackthon.here.services;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.hackthon.here.interfaces.PositionUpdatedListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GeoCodingService {

    private Context context;
    private PositionUpdatedListener positionUpdatedListener;

    public GeoCodingService(Context context, PositionUpdatedListener positionUpdatedListener) {
        this.context = context;
        this.positionUpdatedListener = positionUpdatedListener;
    }

    public void geoCodeAddress(String searchText, String appId, String appCode) {
        String  url = "https://geocoder.api.here.com/6.2/geocode.json?app_id="+appId+"&app_code="+appCode+"&searchtext="+searchText;
       JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url,new JSONObject(), new Response.Listener<JSONObject>() {
           @Override
           public void onResponse(JSONObject response) {
               if(positionUpdatedListener!=null){
                   try {
                       JSONArray viewArr = response.getJSONObject("Response").getJSONArray("View");
                       JSONArray resultsArr = viewArr.getJSONObject(0).getJSONArray("Result");
                       JSONObject location = resultsArr.getJSONObject(0).getJSONObject("Location").getJSONObject("DisplayPosition");
                        double latitude = location.getDouble("Latitude");
                       double longitude = location.getDouble("Latitude");
                       positionUpdatedListener.getUpdate(latitude,longitude);
                   } catch (JSONException e) {
                       Log.e("Error",e.getMessage());
                       positionUpdatedListener.getUpdate(0,0);
                   }

               }
           }
       }, new Response.ErrorListener() {
           @Override
           public void onErrorResponse(VolleyError error) {
                if(positionUpdatedListener!=null){
                    positionUpdatedListener.getUpdate(0,0);
                }
               Log.e("Error",error.getMessage());
           }
       });

       RequestQueue requestQueue = Volley.newRequestQueue(context);
       requestQueue.add(jsonObjectRequest);

   }
}