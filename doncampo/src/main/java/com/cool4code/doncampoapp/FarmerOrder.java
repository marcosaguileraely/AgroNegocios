package com.cool4code.doncampoapp;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class FarmerOrder extends ActionBarActivity implements OnItemClickListener {
    ListView lview;
    private final static String orders[] = {
            "P001 - JUAN DANIEL\nPAPA SABANERA\nRequerido: 10 ARR\nKr 13 #24-56 CHIA, CUND\n320 550 4567",
            "P002 - CARLOS DANIEL ALOHA\nYUCA COMUN\nRequerido: 120 KG\nCll 170 #7a El cordero dorado, BOGOTÁ\n312 456 2343",
            "P003 - MARCOS ELY\nACELGA\nRequerido: 120 ARR\nVereda Los bajos, Choconta, CUND\n321 440 5676",
            "P004 - AMALIA SANDERO\nGRANDILLA\nRequerido: 25 ARR\nVereda Camellito, Choconta, CUND\n314 830 7919"
    };

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_order);

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

        lview = (ListView) findViewById(R.id.stockListView);
        lview.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1,orders));
        lview.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(this, "Has seleccionado " + orders[position], Toast.LENGTH_SHORT).show();
    }
}
