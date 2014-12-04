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
import com.cool4code.doncampoapp.services.VolleySingleton;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class Volley extends ActionBarActivity {
    private String WS_ACTION_DELETE = "http://placita.azurewebsites.net/api/Stocks/1162";
    private String token = "eNJe4_5BAgUhYWNT-gsgcBInXd8IYyCn8HAPmjSeVdn9-DYKemrUarUYuk-JXWTsql9EWq74hbCfwisPfYXScD7MlNhtyUC2ykO4vvohx_BaPV_T0CCHnbWAv-egyp0WLI5EOP1TosktGXHE0rlG_dufp05alty6Rzn7HvY5XAj-__k0bQqfuUGOZDifm3oKEbCedAFeKLqxfP_u0vEijCIrOib7UN4zuaTCbeKk9a4HU-FvY9f4NQoe-AtKyXF2O6BP9R6m_bzJ5DX3DzLyMTcAPpX4GdXec2DWWEOu2ijzYIbCnyFI7Hk380VjUDtad_P8TQDdMp5p-oqcuruvPKnPCmPtg8oEgMZuLk2edDai3eGo2b7OiwVBiQE3sORHLTRHLyzY8hnR9ViatRw80GR_4xOe2Axz6JtJUvDBeF62F2qhQN2ifK7qNsNX1FbTrc_P-sSPPbDsrOzRwITEP3qV8fvwnX6QMEjKMH7s6bM";
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volley);

        Button fire = (Button) findViewById(R.id.fire);
        fire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VolleyDelete(token, WS_ACTION_DELETE);
            }
        });

    }

    public void VolleyDelete(String token_user, String URL){
        final ProgressDialog pDialog = new ProgressDialog(this);
        final String token_auth = token_user;
        pDialog.setMessage("Loading...");
        pDialog.show();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.DELETE, URL, null,
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
