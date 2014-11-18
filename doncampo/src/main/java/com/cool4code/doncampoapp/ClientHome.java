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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cool4code.doncampoapp.helpers.AdapterMarket;
import com.cool4code.doncampoapp.helpers.DatabaseHandler;
import com.cool4code.doncampoapp.helpers.MarketModel;
import com.cool4code.doncampoapp.helpers.WebService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ClientHome extends ActionBarActivity implements AdapterView.OnItemClickListener {
    final Context context = this;
    Button aprende;
    Button placita;
    Button pedidos;
    ListView lview;
    AdapterMarket adapter;

    ProgressDialog mProgressDialog;

    private String URL_WS = "http://placita.azurewebsites.net/";
    private String WS_ACTION_UNITS = "api/Stocks";

    String token;

    JSONArray littleMarketArray;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_home);

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

        aprende= (Button) findViewById(R.id.share_twitter);
        placita= (Button) findViewById(R.id.client_home_ofertas);
        pedidos= (Button) findViewById(R.id.client_home_pedidos);
        lview  = (ListView) findViewById(R.id.marketListView);

        placita.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new littleStock().execute();
            }
        });

        pedidos.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("comprador", "click en pedidos");
                Intent goToOrder= new Intent(ClientHome.this, ClientPaysHistory.class);
                startActivity(goToOrder);
            }
        });

        aprende.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("comprador", "click en aprende");
                Intent goToLearn= new Intent(ClientHome.this, AprendozActivity.class);
                startActivity(goToLearn);
            }
        });

        lview.setOnItemClickListener(this);

        new littleStock().execute();
    }

    @Override
    public void onBackPressed() {
        Intent goToHome = new Intent(ClientHome.this, MainActivity.class);
        startActivity(goToHome);
    }

    private class littleStock extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(context);
            mProgressDialog.setTitle("Agronegocios");
            mProgressDialog.setMessage("¡Bienvenido! Estamos preparando la placita...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            String table_name = "auth";
            DatabaseHandler db = new DatabaseHandler(getApplicationContext());
            token = db.getAuth(table_name);

            WebService getLittleMarket = new WebService(URL_WS, WS_ACTION_UNITS);
            String stringResponse = getLittleMarket.GetLittleMarket(token);
            littleMarketArray = getLittleMarket.parseJsonText(stringResponse);
            Log.d(" -> response ", " String : " + littleMarketArray);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter = new AdapterMarket(context, generateDataMarket(littleMarketArray));
                    lview.setAdapter(adapter);
                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            mProgressDialog.hide();
            Toast.makeText(ClientHome.this, "¡Placita de mercado lista!", Toast.LENGTH_SHORT).show();
        }
    }

    public ArrayList<MarketModel> generateDataMarket(JSONArray stockArray){
        int objectId, Id, product_id, unit_id, Qty, PricePerUnit;
        String Product_Code, Product_Name, Unit_Code, Unit_Name, ExpiresAt, GeoPoint_Address, GeoPoint_Town, GeoPoint_State, GeoPoint_Country;
        String Email, User_Name, User_Identification, User_Phone, Created, Updated;
        double Latitude, Longitude;

        ArrayList<MarketModel> items = new ArrayList<MarketModel>();
        JSONArray jsonArray = stockArray;
        Log.d("lenght", "=>" + jsonArray.length());
        try{
            for(int i=0 ; i<= jsonArray.length()-1; i++){
                JSONObject obj = jsonArray.getJSONObject(i);
                JSONObject objProduct = obj.getJSONObject("Product");
                JSONObject objUnit = obj.getJSONObject("Unit");
                JSONObject objGeo = obj.getJSONObject("GeoPoint");
                JSONObject objEmail = obj.getJSONObject("User");
                JSONObject objUser = objEmail.getJSONObject("User");

                objectId = i;
                Id = obj.getInt("Id");
                PricePerUnit = obj.getInt("PricePerUnit");
                Qty = obj.getInt("Qty");
                ExpiresAt = obj.getString("ExpiresAt");

                product_id = objProduct.getInt("Id");
                Product_Code = objProduct.getString("Code");
                Product_Name = objProduct.getString("Name");

                unit_id = objUnit.getInt("Id");
                Unit_Code = objUnit.getString("Code");
                Unit_Name = objUnit.getString("Name");

                Latitude = objGeo.getDouble("Latitude");
                Longitude = objGeo.getDouble("Longitude");
                GeoPoint_Address = objGeo.getString("Address");
                GeoPoint_Town = objGeo.getString("Town");
                GeoPoint_State = objGeo.getString("State");
                GeoPoint_Country = objGeo.getString("Country");

                Email = objEmail.getString("Email");
                User_Name = objUser.getString("Name");
                User_Identification = objUser.getString("Identification");
                User_Phone = objUser.getString("Phone");

                Created = obj.getString("Created");
                Updated = obj.getString("Updated");

                Log.d(" //i ", " //i :" + objectId + " stockId : " + Id + " Product id : " + product_id + " Product name : " + Product_Name + " Unit name : " + Unit_Name + " User name : " +User_Name );
                items.add(new MarketModel(Id, product_id, unit_id, Qty, PricePerUnit, Product_Code, Product_Name, Unit_Code, Unit_Name, ExpiresAt, GeoPoint_Address, GeoPoint_Town, GeoPoint_State, GeoPoint_Country, Email, User_Name, User_Identification, User_Phone, Created, Updated, Latitude, Longitude));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return items;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        long idstock = adapter.getItemId(position);
        ArrayList<String> detailsMarket = adapter.getAllData(position);
        Log.d("//Market", "//Market" + detailsMarket.toString());
        Intent goToMarketDetails = new Intent(ClientHome.this, ClientMarketDetails.class);
        goToMarketDetails.putExtra("DetailsArray", detailsMarket);
        startActivity(goToMarketDetails);
    }
}
