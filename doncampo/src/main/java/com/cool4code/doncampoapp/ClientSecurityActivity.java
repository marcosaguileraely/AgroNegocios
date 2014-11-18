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
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cool4code.doncampoapp.helpers.DatabaseHandler;
import com.cool4code.doncampoapp.helpers.WebService;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class ClientSecurityActivity extends ActionBarActivity{
    ProgressDialog mProgressDialog;
    final Context context = this;
    EditText farmer_login_dni;
    EditText farmer_login_pass;
    Button login;
    Button noAccount;

    private String URL_WS = "http://placita.azurewebsites.net/";
    private String WS_ACTION = "Token";
    private Integer codeResponse;

    File db = new File("/data/data/com.cool4code.doncampoapp/databases/placitadb");

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_security);

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

        Boolean existsAuth = existAuthTable();
        String tableName = "auth";

        String DATE_FORMAT_PATTERN = "yyyy-MM-dd'T'HH:mm:ss'Z'";
        DateFormat df = new SimpleDateFormat(DATE_FORMAT_PATTERN);
        Date today = Calendar.getInstance().getTime();
        String todayDate = df.format(today);
        Log.d("//date", "//date " + todayDate);

        if(existsAuth == true){
            DatabaseHandler checkExpires = new DatabaseHandler(getApplicationContext());
            String checkCredentials = checkExpires.validateExpiresAt(tableName);
            Log.d("//date", "//date " + checkCredentials);

            SimpleDateFormat formatoDelTexto = new SimpleDateFormat("yyyy-MM-dd");
            Date fecha = null;
            try {
                fecha = formatoDelTexto.parse(checkCredentials);
                Log.d("//date", "//date " + fecha);
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }

            /*if(today.after(fecha)){
                System.out.println("Date1 is after Date2");
            }

            if(today.before(fecha)){
                System.out.println("Date1 is before Date2");
            }

            if(today.equals(fecha)){
                System.out.println("Date1 is equal Date2");
            }*/

        }else{

        }

        login = (Button) findViewById(R.id.farmer_login_button);
        noAccount = (Button) findViewById(R.id.farmer_login_button_singup);
        farmer_login_dni = (EditText) findViewById(R.id.farmer_login_dni);
        farmer_login_pass = (EditText) findViewById(R.id.farmer_login_pass);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dni_value = farmer_login_dni.getText().toString();
                String pass_value = farmer_login_dni.getText().toString();
                if((dni_value.equals("")) && pass_value.equals("")){
                    Toast.makeText(context, "¡Ingrese usuario y clave para acceder!", Toast.LENGTH_SHORT).show();
                }else{
                    new AsyncWS().execute();
                }
            }
        });

        noAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Action", "--> ¿No tienes cuenta?");
                Intent goToSignUp= new Intent(ClientSecurityActivity.this, SingUp.class);
                startActivity(goToSignUp);
            }
        });
    }

    private class AsyncWS extends AsyncTask<Void, Void, Void> {
        File db =new File("/data/data/com.cool4code.doncampoapp/databases/placitadb");
        private ArrayList codeResponse;
        String getCode;
        String getAuth;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(ClientSecurityActivity.this);
            mProgressDialog.setTitle("Agronegocios");
            mProgressDialog.setMessage("Validando información. Espere...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }

        protected Void doInBackground(Void... params) {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
            nameValuePairs.add(new BasicNameValuePair("grant_type", "password"));
            nameValuePairs.add(new BasicNameValuePair("username", farmer_login_dni.getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("password", farmer_login_pass.getText().toString()));

            WebService AuthUser = new WebService(URL_WS , WS_ACTION);
            ArrayList responseList = AuthUser.WSPostAuth(nameValuePairs);
            codeResponse = responseList;
            getCode = (String) responseList.get(0);
            getAuth = (String) responseList.get(1);

            JSONObject jsonobj = null;
            try {
                jsonobj = new JSONObject(getAuth);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d("==>", "JsonObj : " + jsonobj);
            if(db.exists()){
                SQLiteDatabase mydb = getBaseContext().openOrCreateDatabase("placitadb", SQLiteDatabase.OPEN_READWRITE, null);
                mydb.execSQL("CREATE TABLE IF NOT EXISTS "+ "auth" + "(objectId INT, access_token VARCHAR, token_type VARCHAR, expires_in INT, userName VARCHAR, _issued VARCHAR , _expires VARCHAR);");
                mydb.execSQL("DELETE FROM auth;");
                    try {
                        for (int i=0; i<=0; i++) {
                            Integer objectId = i+1;
                            String access_token = jsonobj.getString("access_token");
                            String token_type   = jsonobj.getString("token_type");
                            Integer expires_in  = jsonobj.getInt("expires_in");
                            String userName     = jsonobj.getString("userName");
                            String _issued      = jsonobj.getString(".issued");
                            String _expires     = jsonobj.getString(".expires");
                            mydb.execSQL("INSERT INTO auth"+"(objectId, access_token, token_type, expires_in, userName, _issued, _expires)"+
                                         "VALUES ('"+objectId+"','"+access_token+"','"+token_type+"','"+expires_in+"','"+userName+"','"+_issued+"','"+_expires+"');");
                        }
                        mydb.close();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
            }else{
                Log.d("->", "db Exists");
                Log.d("->", "Create db not needed!");
            }
            return null;
        }
        protected void onPostExecute(Void result) {
            int code = Integer.parseInt(getCode);
            if(code == 200){
                mProgressDialog.dismiss();
                Toast.makeText(context, "¡Acceso exitoso!", Toast.LENGTH_SHORT).show();
                Intent goToLifeClient= new Intent(ClientSecurityActivity.this, MainActivity.class);
                startActivity(goToLifeClient);
            }else {
                mProgressDialog.dismiss();
                Toast toast = Toast.makeText(context,"Agronegocios no ha podido validar el acceso. Intentalo nuevamente.", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        }
    }

    public Boolean existAuthTable(){
        SQLiteDatabase mDatabase = openOrCreateDatabase("placitadb", SQLiteDatabase.CREATE_IF_NECESSARY,null);
        Cursor c = null;
        boolean tableExists = false;
        try{
            c = mDatabase.query("auth", null,
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
