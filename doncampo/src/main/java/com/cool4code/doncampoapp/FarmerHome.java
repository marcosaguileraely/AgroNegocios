package com.cool4code.doncampoapp;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cool4code.doncampoapp.helpers.AdapterMyOrders;
import com.cool4code.doncampoapp.helpers.DatabaseHandler;
import com.cool4code.doncampoapp.helpers.MyOrdersModel;
import com.cool4code.doncampoapp.helpers.WebService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class FarmerHome extends ActionBarActivity implements OnItemClickListener {
    Button aprende;
    Button inventario;
    Button pedidos;
    ListView lview;

    private String URL_WS = "http://placita.azurewebsites.net/";
    private String WS_ACTION_UNITS = "api/MyOrders";

    Context context = this;
    ProgressDialog mProgressDialog;

    AdapterMyOrders adapter;
    String token;
    JSONArray myOrdersArray;


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_home);

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

        aprende = (Button) findViewById(R.id.farmer_home_aprende);
        inventario = (Button) findViewById(R.id.farmer_home_inventario);
        pedidos = (Button) findViewById(R.id.farmer_home_pedidos);
        lview  = (ListView) findViewById(R.id.listview_home_orders);

        pedidos.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("tocar", "click en pedidos");
                new myOrdersAsyncTask().execute();
                //Toast.makeText(context, "¡Usted se encuentra en Mis pedidos!", Toast.LENGTH_LONG).show();
            }
        });

        inventario.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("tocar", "click en inventario");
                Intent goToStock= new Intent(FarmerHome.this, FarmerStock.class);
                startActivity(goToStock);
            }
        });

        aprende.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("tocar", "click en aprende");
                Intent goToLearn= new Intent(FarmerHome.this, AprendozActivity.class);
                startActivity(goToLearn);
            }
        });

        lview.setOnItemClickListener(this);

        new myOrdersAsyncTask().execute();
    }

    private class myOrdersAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(context);
            mProgressDialog.setTitle("Agronegocios");
            mProgressDialog.setMessage("¡Bienvenido! Estamos preparando sus pedidos...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            String table_name = "auth";
            DatabaseHandler db = new DatabaseHandler(getApplicationContext());
            token = db.getAuth(table_name);

            WebService getMyPuchases = new WebService(URL_WS, WS_ACTION_UNITS);
            String stringResponse = getMyPuchases.GetMyPurchases(token);
            myOrdersArray = getMyPuchases.parseJsonText(stringResponse);
            Log.d(" -> response ", " String : " + myOrdersArray);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter = new AdapterMyOrders(context, generateDataMyOrders(myOrdersArray));
                    lview.setAdapter(adapter);
                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            mProgressDialog.hide();
            Toast.makeText(FarmerHome.this, "¡Pedidos cargados!", Toast.LENGTH_SHORT).show();
        }
    }

    public ArrayList<MyOrdersModel> generateDataMyOrders(JSONArray stockArray){
        String Created, Updated;

        String Product_Name, Unit_Name, ExpiresAt, Address, Town, Geo_State, Country;
        String Email, Name, Identification, Phone;
        int Qty_adquired, Id_Order;
        double PricePerUnit;

        ArrayList<MyOrdersModel> items = new ArrayList<MyOrdersModel>();
        JSONArray jsonArray = stockArray;
        Log.d("lenght", "->" + jsonArray.length());
        try{
            for(int i = 0 ; i <= jsonArray.length()-1; i++){
                JSONObject obj = jsonArray.getJSONObject(i);
                JSONObject objStock = obj.getJSONObject("Stock");
                JSONObject objProduct = objStock.getJSONObject("Product");
                JSONObject objUnit = objStock.getJSONObject("Unit");
                //JSONObject objGeo = objStock.getJSONObject("GeoPoint");
                JSONObject objEmail = obj.getJSONObject("User");
                JSONObject objUser = objEmail.getJSONObject("User");
                JSONObject clientGeo = obj.getJSONObject("GeoPoint");

                int objectId = i;
                Id_Order = obj.getInt("Id");
                Qty_adquired = obj.getInt("Qty");
                PricePerUnit = objStock.getInt("PricePerUnit");
                ExpiresAt = obj.getString("Created");

                Product_Name = objProduct.getString("Name");
                Unit_Name = objUnit.getString("Name");

                Address = clientGeo.getString("Address");
                Town = clientGeo.getString("Town");
                Geo_State = clientGeo.getString("State");
                Country = clientGeo.getString("Country");

                Email = objEmail.getString("Email");
                Name = obj.getString("FullName");
                Identification = objUser.getString("Identification");
                Phone = obj.getString("Phone");

                Created = obj.getString("Created");
                Updated = obj.getString("Updated");

                Log.d(" //i ", " //i :" + objectId + " orderId : " + Id_Order + " Product name : " + Product_Name + " Unit name : " + Unit_Name + " User name : " +Name );
                items.add(new MyOrdersModel(Product_Name, Unit_Name, ExpiresAt, Address, Town, Geo_State, Country, Email, Name, Identification, Phone, Qty_adquired, Id_Order, PricePerUnit));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return items;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        long idstock = adapter.getItemId(position);
        ArrayList<String> detailsOrders = adapter.getAllData(position);
        Log.d("//Orders", "//Orders" + detailsOrders.toString());
        Intent goToMarketDetails = new Intent(FarmerHome.this, FarmerOrderDetails.class);
        goToMarketDetails.putExtra("DetailsArray", detailsOrders);
        startActivity(goToMarketDetails);
    }

    @Override
    public void onBackPressed() {
        Intent goToHome = new Intent(FarmerHome.this, MainActivity.class);
        startActivity(goToHome);
    }
}

