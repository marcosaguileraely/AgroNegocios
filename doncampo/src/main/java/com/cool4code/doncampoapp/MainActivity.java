package com.cool4code.doncampoapp;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by cool4code team on 7/8/14.
 * Paola Vanegas
 * Alejandro Zarate Orjuela
 * David Almeciga
 * Marcos A. Aguilera Ely
 */

public class MainActivity extends ActionBarActivity{
    ImageView farmers;
    ImageView clients;
    Button share_facebook;
    Button share_twitter;
    Button about;
    TextView logout;

    ArrayAdapter<String> listAdapter;
    ListView list;
    String[] rank;
    String[] country;
    String[] population;

    String url_playstore = "https://play.google.com/store/apps/details?id=com.irisinteractive.infografia_mintic&hl=es";
    String social_msn = "Acabo de comprar y/o vender un producto agropecuario a través de #AGRONEGOCIOS, app de @AgronetMADR descárgala: http://goo.gl/UnfiH1";

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        farmers        = (ImageView) findViewById(R.id.home_img_farmer);
        clients        = (ImageView) findViewById(R.id.home_img_client);
        share_facebook = (Button) findViewById(R.id.share_facebook);
        share_twitter  = (Button) findViewById(R.id.share_twitter);
        about          = (Button) findViewById(R.id.about);
        logout         = (TextView) findViewById(R.id.home_salir);

        farmers.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent goToFarmer= new Intent(MainActivity.this, FarmerHome.class);
                startActivity(goToFarmer);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SQLiteDatabase mydb = getBaseContext().openOrCreateDatabase("placitadb", SQLiteDatabase.OPEN_READWRITE, null);
                mydb.execSQL("DELETE FROM auth;");
                Intent goToLogin= new Intent(MainActivity.this, ClientSecurityActivity.class);
                goToLogin.putExtra("LOGOUT_MESSAGE", "Auth_LogOut");
                startActivity(goToLogin);
            }
        });

        clients.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent goToClient= new Intent(MainActivity.this, ClientHome.class);
                startActivity(goToClient);
            }
        });

        share_facebook.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String urlToShare = social_msn;
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, urlToShare);

                // See if official Facebook app is found
                boolean facebookAppFound = false;
                List<ResolveInfo> matches = getPackageManager().queryIntentActivities(intent, 0);
                for (ResolveInfo info : matches) {
                    if (info.activityInfo.packageName.toLowerCase().startsWith("com.facebook.katana")) {
                        intent.setPackage(info.activityInfo.packageName);
                        facebookAppFound = true;
                        break;
                    }
                }
                // As fallback, launch sharer.php in a browser
                if (!facebookAppFound) {
                    String sharerUrl = "https://www.facebook.com/sharer/sharer.php?u=" + urlToShare;
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse(sharerUrl));
                }
                startActivity(intent);
            }
        });

        share_twitter.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String urlToShare = social_msn;
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, urlToShare);

                // See if official Facebook app is found
                boolean twitterAppFound = false;
                List<ResolveInfo> matches = getPackageManager().queryIntentActivities(intent, 0);
                for (ResolveInfo info : matches) {
                    if (info.activityInfo.packageName.toLowerCase().startsWith("com.twitter")) {
                        intent.setPackage(info.activityInfo.packageName);
                        twitterAppFound = true;
                        break;
                    }
                }
                // As fallback, launch sharer.php in a browser
                if (!twitterAppFound) {
                    Toast.makeText(MainActivity.this, "¡Twitter app no esta instalada en el dispositivo!", Toast.LENGTH_SHORT).show();
                }
                startActivity(intent);
            }
        });

        about.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent goToAbout= new Intent(MainActivity.this, AboutActivity.class);
                startActivity(goToAbout);
            }
        });

        if (android.os.Build.VERSION.SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }

    @Override
    public void onBackPressed() {
        // Do Here what ever you want do on back press;
    }
}
