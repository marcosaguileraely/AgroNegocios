package com.cool4code.doncampoapp.services;

import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by marcosantonioaguilerely on 11/17/14.
 */
public class DeleteStock {
    private String URLside;
    private String WS_Method;

    public DeleteStock(String URL, String WS_Method){
        this.URLside = URL;
        this.WS_Method= WS_Method;
    }

    public int DeleteMyStock(String token) {
        String URLComplete = this.URLside + this.WS_Method;
        URL url = null;
        int statusCode = 0;
        try {
            url = new URL(URLComplete);
        } catch (MalformedURLException exception) {
            exception.printStackTrace();
        }
        HttpURLConnection httpURLConnection = null;
        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestProperty("Authorization", "Bearer " + token);

            httpURLConnection.setRequestMethod("DELETE");
            statusCode = httpURLConnection.getResponseCode();
            Log.d("//Delete", "//Code " + httpURLConnection.getResponseCode());
            Log.d("//Delete", "//Code " + statusCode);

        } catch (IOException exception) {
            exception.printStackTrace();
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
        return statusCode;
    }
}
