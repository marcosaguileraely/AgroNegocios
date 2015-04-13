package com.cool4code.doncampoapp;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.cool4code.doncampoapp.helpers.AdapterMyStock;
import com.cool4code.doncampoapp.helpers.DatabaseHandler;
import com.cool4code.doncampoapp.helpers.MyStockModel;
import com.cool4code.doncampoapp.helpers.WebService;
import com.cool4code.doncampoapp.services.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FarmerStock extends ActionBarActivity implements OnItemClickListener {
    private String URL_WS = "http://serviciosmadr.minagricultura.gov.co/MiPlacita/PlacitaWS/";
    private String WS_ACTION_UNITS = "api/MyStocks/0";
    private String WS_ACTION_DELETE = "http://serviciosmadr.minagricultura.gov.co/MiPlacita/PlacitaWS/api/Stocks/";

    final Context   context = this;
    ProgressDialog  mProgressDialog;
    AdapterMyStock  adapter;
    Dialog          popDialog;
    ListView        lview;
    Button          nuevo_stock;
    TextView        mensaje_vista;

    long    idstock;
    String  token;
    int     count;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_stock);

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8){
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        ActionBar bar = getActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0099cc")));
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

        mensaje_vista   = (TextView) findViewById(R.id.tips_clave);
        nuevo_stock     = (Button) findViewById(R.id.new_stock);
        lview           = (ListView) findViewById(R.id.stockListView);

        lview.setOnItemClickListener(this);

        nuevo_stock.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent goToNewStock = new Intent(FarmerStock.this, NewStockForm.class);
                startActivity(goToNewStock);
            }
        });
        new getMyStock().execute();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //Toast.makeText(this, "Has seleccionado ", Toast.LENGTH_SHORT).show();
        idstock = adapter.getItemId(position);
        ArrayList<String> detailsMyStock = adapter.getAllData(position);
        Log.d("//Stock", "// Stock" + detailsMyStock.toString());
        String idStock = detailsMyStock.get(0);
        Log.d("//id ", "// id " + idStock + " :=: "+idstock);

        popDialog = new Dialog(context);
        popDialog.setContentView(R.layout.activity_actions_elements);
        popDialog.setTitle("Acciones");
        popDialog.getWindow().setLayout(800, 550);
        Button delete_stock= (Button) popDialog.findViewById(R.id.delete_stock);
        Button details_stock= (Button) popDialog.findViewById(R.id.details_stock);
        delete_stock.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDeleteDialog();
            }
        });
        popDialog.show();
    }

    //Obtener mis inventarios
    private class getMyStock extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(context);
            mProgressDialog.setTitle("Agronegocios");
            mProgressDialog.setMessage("¡Listando inventario!. Espere...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            String table_name = "auth";
            DatabaseHandler db = new DatabaseHandler(getApplicationContext());
            token = db.getAuth(table_name);

            WebService getMyStock = new WebService(URL_WS, WS_ACTION_UNITS);
            String stringResponse = getMyStock.GetMyStock(token);
            final JSONArray myStockArray = getMyStock.parseJsonText(stringResponse);
            Log.d(" -> response ", " String : " + myStockArray);
            //generateData(myStockArray);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter = new AdapterMyStock(context, generateData(myStockArray));
                    lview.setAdapter(adapter);
                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            mProgressDialog.hide();
            if(count == 0){
                Toast.makeText(FarmerStock.this, "No tienes inventario aún.", Toast.LENGTH_SHORT).show();
                mensaje_vista.setText("¿No tienes inventario aún?. \nEmpieza creando uno.");
            }
            else{
                Toast.makeText(FarmerStock.this, "Inventario cargado exitosamente.", Toast.LENGTH_SHORT).show();
                mensaje_vista.setText("Este es tu inventario, revisalo siempre y mantenlo al día. \nSeleccione un inventario para eliminar.");
            }
        }
    }

    //Listado de inventarios
    public ArrayList<MyStockModel> generateData(JSONArray stockArray){
        int objectId, stockId, product_id, Qty, unit_id, pricePerUnit, user_phone;
        String product_name, unit_name, expiresAt, user_email, user_name, created = null, user_identification;
        double geo_lat, geo_long;

        ArrayList<MyStockModel> items = new ArrayList<MyStockModel>();
        JSONArray jsonArray = stockArray;
        count = jsonArray.length();
        Log.d("lenght", "===>" + jsonArray.length());
        try{
            for(int i=0 ; i<= jsonArray.length()-1; i++){
                JSONObject obj = jsonArray.getJSONObject(i);
                JSONObject objProduct = obj.getJSONObject("Product");
                JSONObject objUnit = obj.getJSONObject("Unit");
                JSONObject objGeo = obj.getJSONObject("GeoPoint");
                JSONObject objEmail = obj.getJSONObject("User");
                JSONObject objUser = objEmail.getJSONObject("User");

                objectId     = i;
                stockId      = obj.getInt("Id");
                pricePerUnit = obj.getInt("PricePerUnit");
                Qty          = obj.getInt("Qty");
                expiresAt    = obj.getString("ExpiresAt");

                product_id   = objProduct.getInt("Id");
                product_name = objProduct.getString("Name");

                unit_id      = objUnit.getInt("Id");
                unit_name    = objUnit.getString("Name");

                geo_lat      = objGeo.getDouble("Latitude");
                geo_long     = objGeo.getDouble("Longitude");

                user_email   = objEmail.getString("Email");
                user_name    = objUser.getString("Name");
                user_identification = objUser.getString("Identification");
                user_phone   = objUser.getInt("Phone");

                Log.d(" //i ", " //i :" + objectId + " stockId : " + stockId + " Product id : " + product_id + " Product name : " + product_name + " Unit name : " + unit_name + " User name : " +user_name );
                items.add(new MyStockModel(objectId, stockId, product_id, Qty, unit_id, pricePerUnit, user_identification, user_phone, product_name, unit_name, expiresAt, user_email, user_name, created, geo_lat, geo_long));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return items;
    }

    //Confirmar eliminacion
    private void showDeleteDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("¿Está seguro de eliminar este inventario?")
                .setCancelable(false)
                .setPositiveButton("Aceptar",
                        new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int id){
                                VolleyDelete(token, WS_ACTION_DELETE, idstock);
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancelar",
                new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        dialog.cancel();
                        popDialog.hide();
                        Toast.makeText(context, "Acción cancelada", Toast.LENGTH_SHORT).show();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    @Override
    public void onBackPressed() {
        Intent goToHome = new Intent(FarmerStock.this, FarmerHome.class);
        startActivity(goToHome);
    }

    public void VolleyDelete(String token_user, String URL, long id_stock){
        String URL_COMPLETE = URL + id_stock;
        final ProgressDialog pDialog = new ProgressDialog(this);
        final String token_auth = token_user;
        pDialog.setMessage("Borrando inventario...");
        pDialog.setCancelable(false);
        pDialog.show();

        JSONObject obj = new JSONObject();
        try {
            obj.put("id", "1");
            obj.put("name", "ok");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.DELETE, URL_COMPLETE, obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("App", response.toString());
                        pDialog.hide();
                        popDialog.hide();
                        Toast.makeText(context, "Inventario eliminado.", Toast.LENGTH_SHORT).show();
                        new getMyStock().execute();
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
