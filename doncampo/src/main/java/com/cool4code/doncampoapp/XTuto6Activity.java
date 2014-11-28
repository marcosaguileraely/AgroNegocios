package com.cool4code.doncampoapp;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


public class XTuto6Activity extends ActionBarActivity {
    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    private GestureDetector gestureDetector;
    View.OnTouchListener gestureListener;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xtuto6);

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

        ImageView tuto_home_img = (ImageView) findViewById(R.id.tutoHomeImageView);

        gestureDetector = new GestureDetector(this, new MyGestureDetector());
        gestureListener = new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        };

        tuto_home_img.setOnTouchListener(gestureListener);
    }

    class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
                if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                    return false;
                // IR ADELANTE E IR ATRAS
                if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    Intent goToTutoFinal =  new Intent(XTuto6Activity.this, XTuto6Activity.class);
                    startActivity(goToTutoFinal);
                }  else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    Intent goToTuto5 =  new Intent(XTuto6Activity.this, XTuto5Activity.class);
                    startActivity(goToTuto5);
                }
            } catch (Exception e) {
                // nothing
            }
            return false;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }
    }

    @Override
    public void onBackPressed() {

    }
}
