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
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cool4code.doncampoapp.helpers.DatabaseHandler;
import com.cool4code.doncampoapp.helpers.WebService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;


public class ClientBuy extends ActionBarActivity {
    private String URL_WS = "http://placita.azurewebsites.net/";
    private String WS_ACTION_ORDERS = "api/Orders";
    private String GeoUrl    = "https://maps.googleapis.com/maps/";
    private String GeoParams = "api/geocode/json?latlng=";

    ArrayList<String> buyMarketArray;
    ProgressDialog mProgressDialog;

    EditText    Name;
    EditText    Phone;
    EditText    Qty;
    Button      Buy;
    JSONObject  jsonObj = new JSONObject();

    boolean     firstTime = true;
    Integer     statusCodeGlobal;
    String      token;
    String      FullName;
    String      PhoneStr;
    int         id_stock;
    int         QtyInt;

    Context context = this;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_buy);

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

        Bundle extras = getIntent().getExtras();
        buyMarketArray = extras.getStringArrayList("buyDetails");

        String id_str = (String) buyMarketArray.get(0);
        id_stock = Integer.parseInt(id_str);
        Log.d("//ID_STOCK", " //ID_STOCK " + id_stock);

        Name = (EditText) findViewById(R.id.buy_complete_name);
        Phone = (EditText) findViewById(R.id.buy_phone);
        Qty = (EditText) findViewById(R.id.buy_qty);
        Buy = (Button) findViewById(R.id.buy_buyButton);

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            //nothing to do here!
        }else{
            showGPSDisabledAlertToUser();
        }

        Buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name_data  = Name.getText().toString();
                String phone_data = Phone.getText().toString();
                String qty_data   = Qty.getText().toString();

                if(name_data.isEmpty() || phone_data.isEmpty() || qty_data.isEmpty()){
                    Toast.makeText(context, "¡Todos los campos son requeridos!" , Toast.LENGTH_SHORT).show();
                }else{
                    FullName        = Name.getText().toString();
                    PhoneStr        = Phone.getText().toString();
                    String QtyStr   = Qty.getText().toString();
                    QtyInt = Integer.parseInt(Qty.getText().toString());
                    double QtyDouble =  (double) QtyInt;
                    Log.d("->", "->FullName-> " + FullName + " Phone-> " + PhoneStr + " Qty-> " + QtyStr + " Qty-> " + QtyInt);

                    LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                    Location location   = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
                    double longitude    = location.getLongitude();
                    double latitude     = location.getLatitude();
                    Log.d("->", " Lat-> " + latitude + " Long-> " + longitude);

                    WebService geo      = new WebService(GeoUrl, GeoParams);
                    ArrayList geoArray  = geo.WSGetGeoCode(latitude, longitude);
                    String geoString    = (String) geoArray.get(0);
                    Log.d("Geo", "geo : " + geoString);
                    String[] geoStringArray = geoString.trim().split("\\s*,\\s*");
                    ArrayList<String> arryLocation = new ArrayList<String>();

                    for(int i=0 ; i<= geoStringArray.length-1 ; i++){
                        Log.d("Geo", "geo : " + geoStringArray[i]);
                        arryLocation.add(geoStringArray[i]);
                    }
                    Log.d("String", "String : " + arryLocation);
                    String geoAddress = (String) arryLocation.get(0);
                    String geoTown    = (String) arryLocation.get(1);
                    String geoState   = (String) arryLocation.get(2);
                    String geoCountry = (String) arryLocation.get(3);
                    String yyy = null;
                    try {
                        String xxx = new String(geoTown.getBytes("ISO-8859-1"), "UTF-8");
                        yyy = xxx;
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    Log.d("Address", "Address : " + geoAddress);
                    Log.d("Town", "Town : " + geoTown);
                    Log.d("State", "State : " + geoState);
                    Log.d("Country", "Country : " + geoCountry);

                    String table_name = "auth";
                    DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                    token = db.getAuth(table_name);

                    try {
                        jsonObj.put("StockId", id_stock);
                        jsonObj.put("FullName", FullName);
                        jsonObj.put("Phone", PhoneStr);
                        jsonObj.put("Qty", QtyDouble);
                        JSONObject GeoPoint = new JSONObject();
                        GeoPoint.put("Latitude", longitude);
                        GeoPoint.put("Longitude", latitude);
                        GeoPoint.put("Address", geoAddress);
                        GeoPoint.put("Town", yyy);
                        GeoPoint.put("State", geoState);
                        GeoPoint.put("Country", geoCountry);
                        jsonObj.put("GeoPoint", GeoPoint);
                        Log.d("json", "json : " + jsonObj.toString());

                        PostOrderAT newExe = new PostOrderAT();
                        newExe.execute();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
    //AsyncTask PostOrderAt
    private class PostOrderAT extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(context);
            mProgressDialog.setTitle("Agronegocios");
            mProgressDialog.setMessage("¡Pesando productos para venta!. Compra en proceso. Espere...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            WebService postStock = new WebService(URL_WS, WS_ACTION_ORDERS);
            final Integer statusCode = postStock.WSPostStock(jsonObj, token);
            Log.d("Code", "Code : " + statusCode);
            statusCodeGlobal = statusCode;
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if(statusCodeGlobal == 200 || statusCodeGlobal == 201){
                mProgressDialog.hide();
                Toast.makeText(ClientBuy.this, "¡Compra realizada con exito!", Toast.LENGTH_SHORT).show();
                Intent goToStockHome =  new Intent(ClientBuy.this, ClientHome.class);
                startActivity(goToStockHome);
            }else{
                mProgressDialog.hide();
                Toast.makeText(ClientBuy.this, "¡Verifica los detalles de la compra e intentalo nuevamente!", Toast.LENGTH_SHORT).show();
            }
        }
    }
    //Mostrar dialogo para activar gps
    private void showGPSDisabledAlertToUser(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("El GPS es necesario para esta operación. Quiere habilitarlo?")
                .setCancelable(false)
                .setPositiveButton("Aceptar",
                        new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int id){
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancelar",
                new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }
    //Verificar y ejecutar metodos
    @Override
    protected void onResume(){
        super.onResume();
        Log.i("test", "onResume");
        if (firstTime){
            Log.i("test", "the first time");
            firstTime = false;
        }else{
            Log.i("test", "it's not the first time");
        }
    }
}
