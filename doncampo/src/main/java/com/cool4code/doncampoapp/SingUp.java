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
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cool4code.doncampoapp.helpers.WebService;

import org.json.JSONException;
import org.json.JSONObject;


public class SingUp extends ActionBarActivity {
    private String URL_WS = "http://placita.azurewebsites.net/";
    private String WS_ACTION_REGISTER = "api/Account/Register/" ;

    ProgressDialog mProgressDialog;
    Button fire;
    EditText name;
    EditText documento;
    EditText phone;
    EditText email;
    EditText address;
    EditText pass;
    EditText repass;
    boolean resul = true;

    Context context = this;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);
        getActionBar().setDisplayHomeAsUpEnabled(true);

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

        name = (EditText) findViewById(R.id.register_name);
        documento = (EditText) findViewById(R.id.register_dni);
        phone = (EditText) findViewById(R.id.register_phone);
        email = (EditText) findViewById(R.id.register_email);
        address = (EditText) findViewById(R.id.register_address);
        pass = (EditText) findViewById(R.id.register_pass);
        repass = (EditText) findViewById(R.id.register_repass);
        fire= (Button) findViewById(R.id.fire_ws);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        fire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("click", "Boton Guardar");
                if((pass.getText().toString()).equals(repass.getText().toString())){
                    new AsyncWS().execute();
                }else{
                    Toast.makeText(context, "Las claves no coinciden. Intentalo nuevamente.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private class AsyncWS extends AsyncTask<Void, Void, Void> {
        private Integer code;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(SingUp.this);
            mProgressDialog.setTitle("AgroNegocios");
            mProgressDialog.setMessage("Registrando usuario. Espere...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }

        protected Void doInBackground(Void... params) {
            JSONObject data =  new JSONObject();
            try {
                data.put("Name", name.getText().toString());
                data.put("Identification", documento.getText().toString());
                data.put("Phone", phone.getText().toString());
                data.put("Email", email.getText().toString());
                data.put("Address", address.getText().toString());
                data.put("Password", pass.getText().toString());
                data.put("ConfirmPassword", repass.getText().toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            WebService RegisterUser= new WebService(URL_WS, WS_ACTION_REGISTER);
            Integer code= RegisterUser.WSPostAction(data);
            this.code = code;
            Log.d("code", "code...."+code);
            return null;
        }

        protected void onPostExecute(Void result) {
            if(code == 200){
                mProgressDialog.dismiss();
                Toast.makeText(context, "Usuario creado exitosamente", Toast.LENGTH_SHORT).show();
                Intent iraLogin = new Intent(SingUp.this, ClientSecurityActivity.class);
                startActivity(iraLogin);
            }else {
                mProgressDialog.dismiss();
                Toast.makeText(context, "Acción no completada. Omita acentuaciones y caracteres especiales.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
