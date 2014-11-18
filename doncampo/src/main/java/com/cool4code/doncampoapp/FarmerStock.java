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
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cool4code.doncampoapp.helpers.AdapterMyStock;
import com.cool4code.doncampoapp.helpers.DatabaseHandler;
import com.cool4code.doncampoapp.helpers.MyStockModel;
import com.cool4code.doncampoapp.helpers.WebService;
import com.cool4code.doncampoapp.services.DeleteStock;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class FarmerStock extends ActionBarActivity implements OnItemClickListener {
    private String URL_WS = "http://placita.azurewebsites.net/";
    private String WS_ACTION_UNITS = "api/MyStocks/0";
    private String WS_ACTION_DELETE = "api/MyStocks/";

    String token;

    final Context context = this;
    ProgressDialog mProgressDialog;
    Dialog popDialog;
    AdapterMyStock adapter;
    ListView lview;
    Button nuevo_stock;

    long idstock;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_stock);

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

        nuevo_stock = (Button) findViewById(R.id.new_stock);
        lview = (ListView) findViewById(R.id.stockListView);

        //lview.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1,stock));
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
        Toast.makeText(this, "Has seleccionado ", Toast.LENGTH_SHORT).show();
        idstock = adapter.getItemId(position);
        ArrayList<String> detailsMyStock = adapter.getAllData(position);
        Log.d("//Stock", "// Stock" + detailsMyStock.toString());
        String idStock = detailsMyStock.get(0);
        Log.d("//id ", "// id " + idStock + " :=: "+idstock);

        popDialog = new Dialog(context);
        popDialog.setContentView(R.layout.activity_actions_elements);
        popDialog.setTitle("Acciones");
        popDialog.getWindow().setLayout(800, 700);
        Button delete_stock= (Button) popDialog.findViewById(R.id.delete_stock);
        Button details_stock= (Button) popDialog.findViewById(R.id.details_stock);
        delete_stock.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDeleteDialog();
            }
        });
        details_stock.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("//details ", "// details ");
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
            Toast.makeText(FarmerStock.this, "¡Inventario cargado!", Toast.LENGTH_SHORT).show();
        }
    }

    //Listado de inventarios
    public ArrayList<MyStockModel> generateData(JSONArray stockArray){
        int objectId, stockId, product_id, Qty, unit_id, pricePerUnit, user_identification, user_phone;
        String product_name, unit_name, expiresAt, user_email, user_name, created = null;
        double geo_lat, geo_long;

        ArrayList<MyStockModel> items = new ArrayList<MyStockModel>();
        JSONArray jsonArray = stockArray;
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
                user_identification = objUser.getInt("Identification");
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
                                String deleteElementString =  WS_ACTION_DELETE + idstock;
                                DeleteStock deleteStock = new DeleteStock(URL_WS , deleteElementString);
                                int code = deleteStock.DeleteMyStock(token);
                                Log.d("//Delete", "//Code " + code);
                                /*if (code == 200) {
                                    new getMyStock().execute();
                                    Toast.makeText(context, "Inventario eliminado exitosamente", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(context, "Acción no realizada. Intente nuevamente.", Toast.LENGTH_SHORT).show();
                                }*/
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
}
