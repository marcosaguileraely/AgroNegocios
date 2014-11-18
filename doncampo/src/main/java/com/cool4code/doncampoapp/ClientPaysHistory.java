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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cool4code.doncampoapp.helpers.AdapterMyPurchases;
import com.cool4code.doncampoapp.helpers.DatabaseHandler;
import com.cool4code.doncampoapp.helpers.MyPurchasesModel;
import com.cool4code.doncampoapp.helpers.WebService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class ClientPaysHistory extends ActionBarActivity implements AdapterView.OnItemClickListener {
    final Context context = this;
    ListView lview;
    AdapterMyPurchases adapter;

    ProgressDialog mProgressDialog;

    private String URL_WS = "http://placita.azurewebsites.net/";
    private String WS_ACTION_UNITS = "api/MyPurchases";

    String token;
    JSONArray myPurchasesArray;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_pays_history);

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

        lview  = (ListView) findViewById(R.id.historyListView);
        lview.setOnItemClickListener(this);
        new myPurchases().execute();
    }

    public class myPurchases extends AsyncTask<Void, Void, Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(context);
            mProgressDialog.setTitle("Agronegocios");
            mProgressDialog.setMessage("¡Cargando listado de compras...");
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
            myPurchasesArray = getMyPuchases.parseJsonText(stringResponse);
            Log.d(" -> response ", " String : " + myPurchasesArray);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter = new AdapterMyPurchases(context, generateDataMyPurchases(myPurchasesArray));
                    lview.setAdapter(adapter);
                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            mProgressDialog.hide();
            Toast.makeText(ClientPaysHistory.this, "¡Placita de mercado lista!", Toast.LENGTH_SHORT).show();
        }
    }

    public ArrayList<MyPurchasesModel> generateDataMyPurchases(JSONArray stockArray){
        String Created, Updated;

        String Product_Name, Unit_Name, ExpiresAt, Address, Town, Geo_State, Country;
        String Email, Name, Identification, Phone;
        int Stock_Qty, Id_Order;
        double PricePerUnit;

        ArrayList<MyPurchasesModel> items = new ArrayList<MyPurchasesModel>();
        JSONArray jsonArray = stockArray;
        Log.d("lenght", "=>" + jsonArray.length());
        try{
            for(int i = 0 ; i <= jsonArray.length()-1; i++){
                JSONObject obj = jsonArray.getJSONObject(i);
                JSONObject objStock = obj.getJSONObject("Stock");
                JSONObject objProduct = objStock.getJSONObject("Product");
                JSONObject objUnit = objStock.getJSONObject("Unit");
                JSONObject objGeo = objStock.getJSONObject("GeoPoint");
                JSONObject objEmail = objStock.getJSONObject("User");
                JSONObject objUser = objEmail.getJSONObject("User");

                int objectId = i;
                Id_Order = obj.getInt("Id");
                PricePerUnit = objStock.getInt("PricePerUnit");
                Stock_Qty = objStock.getInt("Qty");
                ExpiresAt = objStock.getString("ExpiresAt");

                Product_Name = objProduct.getString("Name");
                Unit_Name = objUnit.getString("Name");

                Address = objGeo.getString("Address");
                Town = objGeo.getString("Town");
                Geo_State = objGeo.getString("State");
                Country = objGeo.getString("Country");

                Email = objEmail.getString("Email");
                Name = objUser.getString("Name");
                Identification = objUser.getString("Identification");
                Phone = objUser.getString("Phone");

                Created = obj.getString("Created");
                Updated = obj.getString("Updated");

                Log.d(" //i ", " //i :" + objectId + " orderId : " + Id_Order + " Product name : " + Product_Name + " Unit name : " + Unit_Name + " User name : " +Name );
                items.add(new MyPurchasesModel(Product_Name, Unit_Name, ExpiresAt, Address, Town, Geo_State, Country, Email, Name, Identification, Phone, Stock_Qty, Id_Order, PricePerUnit));
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
        Intent goToMarketDetails = new Intent(ClientPaysHistory.this, ClientHistoryDetails.class);
        goToMarketDetails.putExtra("DetailsArray", detailsOrders);
        startActivity(goToMarketDetails);
    }
}
