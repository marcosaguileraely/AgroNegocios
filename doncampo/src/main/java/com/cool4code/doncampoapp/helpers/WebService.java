package com.cool4code.doncampoapp.helpers;

import android.content.Context;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by COOL4CODE TEAM @cool4code on 9/28/14.
 * Indeed DonCampo Team
 * David Alméciga @dagrinchi
 * Marcos Aguilera @marcode_ely
 */

public class WebService{
    boolean resul = true;
    private Context context;


    private String URL;
    private String WS_Method;

    public WebService(String URL, String WS_Method){
        this.URL = URL;
        this.WS_Method= WS_Method;
    }

    public Integer WSPostAction(JSONObject data) {
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(URL + WS_Method);
        post.setHeader("content-type", "application/json");
        int statusCode = 0;
        try {
            StringEntity entity = new StringEntity(data.toString());
            post.setEntity(entity);

            HttpResponse response = client.execute(post);
            StatusLine statusLine = response.getStatusLine();
            statusCode = statusLine.getStatusCode();

            String respStr = EntityUtils.toString(response.getEntity());
            Log.d("httpResponse", "-->" + respStr);
            Log.d("httpResponse", "-->" + statusCode);

            if (!respStr.equals("true"))
                resul = false;

        } catch (Exception e) {
            Log.e("HttpResponse-->", "Error " + e);
            resul = false;
        }
        return statusCode;
    }

    public ArrayList WSPostAuth(List nameValuePairs) {
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(URL + WS_Method);
        int statusCode = 0;
        String respStr = null;
        String statusCodeStr = null;
        ArrayList<String> arrayAuth = new ArrayList<String>();
        try {
            post.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            HttpResponse response = client.execute(post);
            StatusLine statusLine = response.getStatusLine();
            statusCode = statusLine.getStatusCode();
            statusCodeStr = Integer.toString(statusCode);

            respStr = EntityUtils.toString(response.getEntity());
            Log.d("httpResponse", "-->" + respStr);
            Log.d("httpResponse", "-->" + statusCode);

        }catch (Exception e){
            Log.e("HttpResponse-->", "Error " + e);
        }
            arrayAuth.add(statusCodeStr);
            arrayAuth.add(respStr);
        return arrayAuth;
    }

    public Integer WSPostStock(JSONObject data, String token) {
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(URL + WS_Method);
        post.setHeader("Authorization", "Bearer " + token);
        post.setHeader("Accept", "application/json");
        post.setHeader("Content-Type", "application/json; charset=utf-8");

        Log.d("url", "-->" + URL + WS_Method);
        int statusCode = 0;
        try {
            StringEntity entity = new StringEntity(data.toString());

            entity.setContentType("application/json");
            Log.d("//entity", "-->" + entity.getContent().toString());
            post.setEntity(entity);

            HttpResponse response = client.execute(post);
            StatusLine statusLine = response.getStatusLine();
            statusCode = statusLine.getStatusCode();

            String respStr = EntityUtils.toString(response.getEntity());
            Log.d("httpResponse", "-->" + respStr);
            Log.d("httpResponse", "-->" + statusCode);

            if (!respStr.equals("true"))
                resul = false;

        } catch (Exception e) {
            Log.e("HttpResponse-->", "Error " + e);
            resul = false;
        }
        return statusCode;
    }

    public String GetMyStock(String token) {
        String URLComplete = this.URL + this.WS_Method;
        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(URLComplete);
        httpGet.setHeader("Authorization", "Bearer " + token);
        httpGet.setHeader("Content-Type", "application/json; charset=utf-8");
        Log.d("URL==>", " ==> "+ URLComplete);
        String jsonText = null;

        try {
            HttpResponse response = client.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            Log.d("Status", "Code =>" + statusCode);
            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null) {
                    Log.d("=>", "=> line");
                    builder.append(line);
                }
                jsonText = builder.toString();
            } else {
                Log.e(WebService.class.getName(), "¡Conexión no exitosa!");
            }
        }catch (ClientProtocolException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        return jsonText;
    }

    public int DeleteMyStock(String token) {
        /*String URLComplete = this.URL + this.WS_Method;
        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpDelete httpDelete = new HttpDelete(URLComplete);
        httpDelete.setHeader("Authorization", "Bearer " + token);
        Log.d(" URL==> ", " ==> "+ URLComplete);
        HttpResponse response = null;
        int statusCode =0;
        try {
            response = client.execute(httpDelete);
            StatusLine statusLine = response.getStatusLine();
            statusCode = statusLine.getStatusCode();
            Log.d("Status", "Code =>" + statusCode);

            if (statusCode == 200) {
                Log.d(WebService.class.getName(), "¡Conexión exitosa!");
            } else {
                Log.e(WebService.class.getName(), "¡Conexión no exitosa!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        return 0;
    }

    public String WSGetUnits(String token) {
        String URLComplete = this.URL + this.WS_Method;
        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(URLComplete);
        Log.d("->", " -> " + token);
        httpGet.setHeader("Authorization", "Bearer " + token);
        httpGet.setHeader("Content-Type", "application/json; charset=utf-8");
        Log.d("URL==>", " ==> "+ URLComplete);
        String jsonText = null;

        try {
            HttpResponse response = client.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            Log.d("Status", "Code =>" + statusCode);
            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null) {
                    Log.d("=>", "=> line");
                    builder.append(line);
                }
                jsonText = builder.toString();
            } else {
                Log.e(WebService.class.getName(), "¡Conexión no exitosa!");
            }
        }catch (ClientProtocolException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        Log.d("Units response", "=>" + jsonText);
        return jsonText;
    }

    public String GetLittleMarket(String token) {
        String URLComplete = this.URL + this.WS_Method;
        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(URLComplete);
        httpGet.setHeader("Authorization", "Bearer " + token);
        httpGet.setHeader("Content-Type", "application/json; charset=utf-8");
        Log.d("URL==>", " ==> "+ URLComplete);
        String jsonText = null;

        try {
            HttpResponse response = client.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            Log.d("Status", "Code =>" + statusCode);
            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null) {
                    Log.d("=>", "=> line");
                    builder.append(line);
                }
                jsonText = builder.toString();
            } else {
                Log.e(WebService.class.getName(), "¡Conexión no exitosa!");
            }
        }catch (ClientProtocolException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        return jsonText;
    }

    public String GetMyPurchases(String token) {
        String URLComplete = this.URL + this.WS_Method;
        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(URLComplete);
        httpGet.setHeader("Authorization", "Bearer " + token);
        httpGet.setHeader("Content-Type", "application/json; charset=utf-8");
        Log.d("URL ==>", " ==> "+ URLComplete);
        String jsonText = null;

        try {
            HttpResponse response = client.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            Log.d("Status", "Code =>" + statusCode);
            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null) {
                    Log.d("=>", "=> line");
                    builder.append(line);
                }
                jsonText = builder.toString();
            } else {
                Log.e(WebService.class.getName(), "¡Conexión no exitosa!");
            }
        }catch (ClientProtocolException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        return jsonText;
    }

    public String GetMyOrders(String token) {
        String URLComplete = this.URL + this.WS_Method;
        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(URLComplete);
        httpGet.setHeader("Authorization", "Bearer " + token);
        httpGet.setHeader("Content-Type", "application/json; charset=utf-8");
        Log.d("URL ==>", " ==> "+ URLComplete);
        String jsonText = null;

        try {
            HttpResponse response = client.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            Log.d("Status", "Code =>" + statusCode);
            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null) {
                    Log.d("=>", "=> line");
                    builder.append(line);
                }
                jsonText = builder.toString();
            } else {
                Log.e(WebService.class.getName(), "¡Conexión no exitosa!");
            }
        }catch (ClientProtocolException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        return jsonText;
    }

    public ArrayList WSGetGeoCode(double latitude, double longitude) {
        String URLComplete = this.URL + this.WS_Method + latitude + "," + longitude;
        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(URLComplete);
        String jsonText = null;
        ArrayList<String> arrayAuth = new ArrayList<String>();

        try {
            HttpResponse response = client.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
                jsonText = builder.toString();
            } else {
                Log.e(WebService.class.getName(), "¡Conexión no exitosa!");
            }
        }catch (ClientProtocolException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }

        try {
            JSONObject obj = new JSONObject(jsonText);
            JSONArray jsonArray = obj.getJSONArray("results");
            for(int i=0 ; i < jsonArray.length() ; i++){
                JSONObject object = jsonArray.getJSONObject(i);
                JSONArray address_components = object.getJSONArray("address_components");
                String formatted_address = object.getString("formatted_address");
                arrayAuth.add(formatted_address);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return arrayAuth;
    }

    public String getJsonText() {
        String URLComplete = this.URL + this.WS_Method;
        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(URLComplete);
        Log.d("URL==>", " ==> "+ URLComplete);
        String jsonText = null;

        try {
            HttpResponse response = client.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            Log.d("Status", "Code =>" + statusCode);
            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null) {
                    Log.d("=>", "=> line");
                    builder.append(line);

                }
                jsonText = builder.toString();
            } else {
                Log.e(WebService.class.getName(), "¡Conexión no exitosa!");
            }
        }catch (ClientProtocolException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        return  jsonText;
    }

    public JSONArray parseJsonText(String jsonText){
        JSONArray jsonArray = null;
        try {
            jsonArray= new JSONArray(jsonText);
        }catch (Exception e){
            e.printStackTrace();
        }
        return jsonArray;
    }
}
