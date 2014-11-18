package com.cool4code.doncampoapp;

import android.app.Activity;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class RestfulActivity extends ActionBarActivity implements OnClickListener{
    Button cargar;
    EditText etResponse;
    TextView tvIsConnected;
    private static String url_src = "http://bit2media.com/stocks.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restful);

        cargar= (Button) findViewById(R.id.boton_cargar_rest);
        etResponse = (EditText) findViewById(R.id.etResponse);
        tvIsConnected = (TextView) findViewById(R.id.tvIsConnected);

        cargar.setOnClickListener(this);
    }

    public static String GET(String url){
        InputStream inputStream = null;
        String result = "";
        try {
            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));
            inputStream = httpResponse.getEntity().getContent();
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";
        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }
        return result;
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
             result += line;
        inputStream.close();
        return result;
    }

    public boolean isConnected(){
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            return GET(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            try {
               // JSONObject json = new JSONObject(result);
                JSONArray json = new JSONArray(result);
                //JSONArray data = json.getJSONArray(0); // get articles array
                int total= json.length();
                Log.d("-->", "--> "+json.getJSONObject(0)); // get first article in the array
                Log.d("-->", "--> "+json.getJSONObject(1)); // get first article in the array

                etResponse.setText("Length: "+total+"\n\n"+json.toString(1));
                Toast.makeText(getBaseContext(), "Received!", Toast.LENGTH_LONG).show();
                //etResponse.setText(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View v) {
        Log.d("onClick", "Cargar rest");
        // check if you are connected or not
        if(isConnected()){
            tvIsConnected.setBackgroundColor(0xFF00CC00);
            tvIsConnected.setText("You are conncted");
        }
        else{
            tvIsConnected.setText("You are NOT conncted");
        }
        // call AsynTask to perform network operation on separate thread
        new HttpAsyncTask().execute(url_src);
        //new HttpAsyncTask().execute("http://hmkcode.appspot.com/rest/controller/get.json");
    }
}
