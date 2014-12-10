package com.cool4code.doncampoapp;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.cool4code.doncampoapp.helpers.DatabaseHandler;
import com.cool4code.doncampoapp.services.VolleySingleton;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ClientHistoryDetails extends ActionBarActivity {
    Context context = this;
    String  token;
    private String WS_ACTION_DELETE = "http://placita.azurewebsites.net/api/Orders/";

    ArrayList<String> detailsMarketArray;
    TextView Product;
    TextView Unit;
    TextView Qty;
    TextView Price;
    TextView Expires;
    TextView Address;
    TextView Farmer;
    TextView Email;
    TextView Phone;

    String   Idorderstr;
    Integer  idorder;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_details);

        ActionBar bar = getActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ff4444")));
        int titleId;
        int textColor = getResources().getColor(android.R.color.white);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            titleId = getResources().getIdentifier("action_bar_title", "id", "android");
            TextView abTitle = (TextView) findViewById(titleId);
            abTitle.setTextColor(textColor);
        } else {
            TextView abTitle = (TextView) getWindow().findViewById(android.R.id.title);
            abTitle.setTextColor(textColor);
        }

        Button deletePurchase = (Button) findViewById(R.id.eliminar_compra);

        Bundle extras = getIntent().getExtras();
        detailsMarketArray = extras.getStringArrayList("DetailsArray");
        Log.d("//Extra", "//Extra" + detailsMarketArray);

        Idorderstr = detailsMarketArray.get(0);
        idorder = Integer.parseInt(Idorderstr);
        Log.d("//ID", "->" + idorder);
        Product = (TextView) findViewById(R.id.details_right_product);
        Unit = (TextView) findViewById(R.id.details_right_unit);
        Qty = (TextView) findViewById(R.id.details_right_qty);
        Price = (TextView) findViewById(R.id.details_right_price);
        Expires = (TextView) findViewById(R.id.details_right_expires);
        Address = (TextView) findViewById(R.id.details_right_location);
        Farmer = (TextView) findViewById(R.id.details_right_farmer);
        Email = (TextView) findViewById(R.id.details_right_email);
        Phone = (TextView) findViewById(R.id.details_right_phone);

        Product.setText(detailsMarketArray.get(1));
        Unit.setText(detailsMarketArray.get(2));
        Qty.setText(detailsMarketArray.get(3));
        Price.setText(detailsMarketArray.get(4));
        String expires = detailsMarketArray.get(5);
        Expires.setText(expires.substring(0, 10));
        Address.setText(detailsMarketArray.get(6));
        Farmer.setText(detailsMarketArray.get(7));
        Email.setText(detailsMarketArray.get(8));
        Phone.setText(detailsMarketArray.get(9));

        deletePurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String table_name = "auth";
                DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                token = db.getAuth(table_name);
                showDeleteDialog();
            }
        });
    }

    //Confirmar eliminacion
    private void showDeleteDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("¿Está seguro de eliminar esta compra?")
                .setCancelable(false)
                .setPositiveButton("Aceptar",
                        new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int id){
                                VolleyDelete(token, WS_ACTION_DELETE, idorder);
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancelar",
                new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        dialog.cancel();
                        Toast.makeText(context, "Acción cancelada.", Toast.LENGTH_SHORT).show();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }


    public void VolleyDelete(String token_user, String URL, int id_order){
        String URL_COMPLETE = URL + id_order;
        final ProgressDialog pDialog = new ProgressDialog(this);
        final String token_auth = token_user;
        pDialog.setMessage("Eliminando compra...");
        pDialog.setCancelable(false);
        pDialog.show();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.DELETE, URL_COMPLETE, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("App", response.toString());
                        pDialog.hide();
                        Toast.makeText(context, "Compra eliminada.", Toast.LENGTH_SHORT).show();
                        Intent goToHome = new Intent(ClientHistoryDetails.this, ClientPaysHistory.class);
                        startActivity(goToHome);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("App", "Error: " + error.getMessage());
                pDialog.hide();
                Toast.makeText(context, "No se completo la solicitud. Intente nuevamente.", Toast.LENGTH_SHORT).show();
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
