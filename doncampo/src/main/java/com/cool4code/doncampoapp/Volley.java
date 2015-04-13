package com.cool4code.doncampoapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.cool4code.doncampoapp.helpers.DatabaseHandler;
import com.cool4code.doncampoapp.services.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class Volley extends ActionBarActivity {
    private String WS_ACTION_DELETE = "http://serviciosmadr.minagricultura.gov.co/MiPlacita/PlacitaWS/api/Stocks/1162";
    Context context = this;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volley);

        String table_name = "auth";
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        token = db.getAuth(table_name);

        Button fire = (Button) findViewById(R.id.fire);
        fire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VolleyDelete(token, WS_ACTION_DELETE);
                Log.d("token", "tk ->"+token);
            }
        });

    }

    public void VolleyDelete(String token_user, String URL){
        final ProgressDialog pDialog = new ProgressDialog(this);
        final String token_auth = token_user;
        pDialog.setMessage("Loading...");
        pDialog.show();

        JSONObject obj = new JSONObject();
        try {
            obj.put("id", "1");
            obj.put("name", "ok");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.DELETE, URL, obj,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("App", response.toString());
                        pDialog.hide();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("App", "Error: " + error.getMessage());
                // hide the progress dialog
                pDialog.hide();
            }
        }){
            /**
             * Passing some request headers
             **/
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer "+token_auth);
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq);
    }
}
