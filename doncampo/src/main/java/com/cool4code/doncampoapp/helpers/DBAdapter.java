package com.cool4code.doncampoapp.helpers;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cool4code.doncampoapp.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 **Created by marcosantonioaguilerely on 10/23/14.
 **/
public class DBAdapter extends ArrayAdapter<PricesModel> {

    private final Context context;
    private final ArrayList<PricesModel> itemsArrayList;

    public DBAdapter(Context context, ArrayList<PricesModel> itemsArrayList) {
        super(context, R.layout.product_listview, itemsArrayList);
        this.context = context;
        this.itemsArrayList = itemsArrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // 1. Create inflater
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 2. Get rowView from inflater
        View rowView = inflater.inflate(R.layout.product_listview, parent, false);

        // 3. Get the two text view from the rowView
        TextView nameList = (TextView) rowView.findViewById(R.id.product_nameBox);
        TextView locationList = (TextView) rowView.findViewById(R.id.product_location);
        TextView unitList = (TextView) rowView.findViewById(R.id.product_unit);
        TextView avgPriceList = (TextView) rowView.findViewById(R.id.product_avg);

        PricesModel pricesModel = (PricesModel) itemsArrayList.get(position);

        //Currency COP Convertion
        int avgValue = pricesModel.getPriceAvgPerUnit();
        NumberFormat defaultFormat = NumberFormat.getCurrencyInstance();
        Log.d("CURRENCY", "==>"+ defaultFormat.format(avgValue));

        Locale COP = new Locale("es", "CO");
        NumberFormat copFormat = NumberFormat.getCurrencyInstance(COP);
        Log.d("COPFormat", "===>"+ copFormat.format(avgValue));
        String copString = copFormat.format(avgValue);

        // 4. Set the text for textView
        nameList.setText(pricesModel.getProduct_Name());
        locationList.setText(pricesModel.getLocation());
        unitList.setText(pricesModel.getUnit_Name());
        avgPriceList.setText(copString);

        // 5. return rowView
        return rowView;
    }
}
