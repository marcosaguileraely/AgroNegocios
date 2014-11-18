package com.cool4code.doncampoapp;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cool4code.doncampoapp.helpers.DBAdapter;
import com.cool4code.doncampoapp.helpers.PricesModel;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cool4code team on 7/8/14.
 * David Almeciga
 * Marcos A. Aguilera Ely
 */

public class SearchActivity extends ActionBarActivity {
    private CharSequence mTitle;
    ListView listview;
    List<ParseObject> ob;
    ProgressDialog mProgressDialog;
    search_products_adapter adapter;
    private List<model_products> productsList = null;
    EditText search_input;
    Button ingreso;
    Button buscar;
    Context context = this;

    int objectId, Id, PriceMaxPerUnit, PriceMinPerUnit, PriceAvgPerUnit;
    String  Product_Code, Product_Name, Unit_Code, Unit_Name, Location, Created, Updated;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        ActionBar bar = getActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#669900")));
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

        search_input = (EditText) findViewById(R.id.search_product);
        ingreso = (Button) findViewById(R.id.search_home_ingresar);
        buscar = (Button) findViewById(R.id.search_button);
        listview = (ListView) findViewById(R.id.search_productListView);

        DBAdapter adapter = new DBAdapter(context, generateData(search_input.getText().toString()));
        listview.setAdapter(adapter);

        ingreso.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("tocar", "click en INGRESO");
                Intent goToSecurity= new Intent(SearchActivity.this, ClientSecurityActivity.class);
                startActivity(goToSecurity);
            }
        });

        search_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                String searchData=  search_input.getText().toString();
                Log.d("search", "->"+searchData);
                if(s.equals("")){
                    //cuando vacio
                }else{
                    DBAdapter adapter = new DBAdapter(context, generateData(searchData));
                    listview.setAdapter(adapter);
                }
            }
        });
    }

    public ArrayList<PricesModel> generateData(String searchInputData){
        ArrayList<PricesModel> items = new ArrayList<PricesModel>();
        SQLiteDatabase mydb = getBaseContext().openOrCreateDatabase("placitadb", SQLiteDatabase.OPEN_READWRITE, null);
        String searchData = searchInputData;
        Log.d("search", "->"+searchData);
        if(searchData.equals("")){
            Log.d("searchData", "Vacio: " + searchData);
            Cursor c= mydb.rawQuery("SELECT Product_Name, Location, Unit_Name, PriceAvgPerUnit FROM prices ORDER BY Created", null);
            int count= c.getCount();

            Toast toast = Toast.makeText(SearchActivity.this,"Hemos consechado " + count + " productos.", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();

            if(c.moveToNext()){
                do {
                    Product_Name = c.getString(0);
                    Location = c.getString(1);
                    Unit_Name = c.getString(2);
                    PriceAvgPerUnit = c.getInt(3);

                    items.add(new PricesModel(Product_Name, Location, Unit_Name, PriceAvgPerUnit));
                }while(c.moveToNext());
            }
            c.close();
            mydb.close();
        }else{
            Log.d("searchData", "No vacio: " + searchData);
            Cursor c = mydb.rawQuery("SELECT Product_Name, Location, Unit_Name, PriceAvgPerUnit FROM prices WHERE Product_Name like '%"+searchData+"%' OR Location like '%"+searchData+"%' ORDER BY Created", null);
            int count = c.getCount();

            Toast toast = Toast.makeText(SearchActivity.this,"Hemos consechado " + count + " productos.", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();

            if(c.moveToNext()){
                do {
                    Product_Name = c.getString(0);
                    Location = c.getString(1);
                    Unit_Name = c.getString(2);
                    PriceAvgPerUnit = c.getInt(3);

                    items.add(new PricesModel(Product_Name, Location, Unit_Name, PriceAvgPerUnit));
                }while(c.moveToNext());
            }
            c.close();
            mydb.close();
        }
        return items;
    }
}
