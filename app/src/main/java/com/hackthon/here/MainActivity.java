package com.hackthon.here;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    String url="https://wse.api.here.com/2/findsequence.json\n" +
            "?start=Berlin-Main-Station;52.52282,13.37011\n" +
            "&destination1=East-Side-Gallery;52.50341,13.44429\n" +
            "&destination2=Olympiastadion;52.51293,13.24021\n" +
            "&end=HERE-Berlin-Campus;52.53066,13.38511\n" +
            "&mode=fastest;car\n" +
            "&app_id=LdYAfST5JuGgdYzTL7mk\n" +
            "&app_code=3SkmLh04d2Pv-kvsG5ZpDQ";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RequestQueue requestQueue= Volley.newRequestQueue(MainActivity.this);
      JsonObjectRequest jsonArrayRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
          @Override
          public void onResponse(JSONObject response) {
Log.e("response",response.toString());
          }
      }, new Response.ErrorListener() {
          @Override
          public void onErrorResponse(VolleyError error) {
              Log.e("err",error.getMessage());
          }
      });
        requestQueue.add(jsonArrayRequest);
    }
}
