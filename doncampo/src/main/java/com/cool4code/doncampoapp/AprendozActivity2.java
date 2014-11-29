package com.cool4code.doncampoapp;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;


public class AprendozActivity2 extends ActionBarActivity {
    WebView news;
    private static final String URL = "http://www.agronet.gov.co/www/htm3b/noticias2uniNuke_2011.asp";

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aprendoz_activity2);

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

        news = (WebView) findViewById(R.id.newsWebView);

        WebSettings webSettings = news.getSettings();
        webSettings.setJavaScriptEnabled(true);
        news.loadUrl(URL);
        news.setWebViewClient(new WebViewClient());
    }

    @Override
    public void onBackPressed() {
        Intent goToClientHome = new Intent(AprendozActivity2.this, ClientHome.class);
        startActivity(goToClientHome);
    }
}
