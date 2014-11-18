package com.cool4code.doncampoapp;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.util.Date;


public class TermsActivity extends ActionBarActivity {
    Button goToSearch;
    File db = new File("/data/data/com.cool4code.doncampoapp/databases/placitadb");
    long milidate = new Date().getTime();

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);
        goToSearch = (Button) findViewById(R.id.accept_conditions);

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

        Boolean existsLegal = existLegalTable();

            if(existsLegal == true){
                Intent goToTerms = new Intent(TermsActivity.this, SearchActivity.class);
                startActivity(goToTerms);
            }else{
                SQLiteDatabase mydb = getBaseContext().openOrCreateDatabase("placitadb", SQLiteDatabase.OPEN_READWRITE, null);
                mydb.execSQL("CREATE TABLE IF NOT EXISTS "+ "legal" + "(agree INT);");
                mydb.execSQL("INSERT INTO legal"+"(agree)"+
                             "VALUES ('"+milidate+"');");
            }

        goToSearch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent goToSearch = new Intent(TermsActivity.this, SearchActivity.class);
                startActivity(goToSearch);
            }
        });
    }

    public Boolean existLegalTable(){
        SQLiteDatabase mDatabase = openOrCreateDatabase("placitadb", SQLiteDatabase.CREATE_IF_NECESSARY,null);
        Cursor c = null;
        boolean tableExists = false;
        try{
            c = mDatabase.query("legal", null,
                    null, null, null, null, null);
            tableExists = true;
            c.close();
        }
        catch (Exception e) {
            Log.d("checkingTable", "Units : "+" doesn't exist :(((");
        }
        return tableExists;
    }
}
