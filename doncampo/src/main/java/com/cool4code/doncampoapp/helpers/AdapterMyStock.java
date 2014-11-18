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
 * Created by marcosantonioaguilerely on 11/1/14.
 */
public class AdapterMyStock extends ArrayAdapter<MyStockModel>{

    private final Context context;
    private final ArrayList<MyStockModel> myStockArrayList;

    public AdapterMyStock(Context context, ArrayList<MyStockModel> myStockArrayList) {
        super(context, R.layout.my_stock_listview, myStockArrayList);
        this.context = context;
        this.myStockArrayList = myStockArrayList;
    }

    public long getItemId(int position) {
        if (myStockArrayList != null)
            return myStockArrayList.get(position).getStockId();
        return 0;
    }

    public ArrayList getAllData(int position){
        MyStockModel myStockModel= myStockArrayList.get(position);
        ArrayList<String> arrayData = new ArrayList<String>();
        String Id = Integer.toString(myStockModel.getStockId());
        String Product_Name = myStockModel.getProduct_name();
        String Unit_Name = myStockModel.getUnit_name();
        String Qty = Integer.toString(myStockModel.getQty());

        /*formating local currency*/
        double Price = myStockModel.getPricePerUnit();
        NumberFormat defaultFormat = NumberFormat.getCurrencyInstance();

        Locale COP = new Locale("es", "CO");
        NumberFormat copFormat = NumberFormat.getCurrencyInstance(COP);
        Log.d("COPFormat", "==>" + copFormat.format(Price));
        String priceCOPString = copFormat.format(Price);

        String ExpiresAt = myStockModel.getExpiresAt();
        arrayData.add(Id);
        arrayData.add(Product_Name);
        arrayData.add(Unit_Name);
        arrayData.add(Qty);
        arrayData.add(priceCOPString);
        arrayData.add(ExpiresAt);

        return  arrayData;
    }

    //@Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // 1. Create inflater
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 2. Get rowView from inflater
        View rowView = inflater.inflate(R.layout.my_stock_listview, parent, false);

        // 3. Get the two text view from the rowView
        TextView product_name = (TextView) rowView.findViewById(R.id.mystock_productname);
        TextView farmer = (TextView) rowView.findViewById(R.id.mystock_farmer);
        TextView price = (TextView) rowView.findViewById(R.id.mystock_priceunit);
        TextView unit = (TextView) rowView.findViewById(R.id.mystock_unit);
        TextView qty = (TextView) rowView.findViewById(R.id.mystock_qty);
        TextView expiresAt = (TextView) rowView.findViewById(R.id.mystock_expiresat);

        MyStockModel myStockModel = (MyStockModel) myStockArrayList.get(position);

        int qtyValue = myStockModel.getQty();
        String expires = myStockModel.getExpiresAt();
        int priceValue = myStockModel.getPricePerUnit();
        NumberFormat defaultFormat = NumberFormat.getCurrencyInstance();

        Locale COP = new Locale("es", "CO");
        NumberFormat copFormat = NumberFormat.getCurrencyInstance(COP);
        Log.d("COPFormat", "==>" + copFormat.format(priceValue));
        String priceCOPString = copFormat.format(priceValue);
        String qtyString = Integer.toString(qtyValue);
        String qtyComplete = "Cantidad: " + qtyString;
        String expiresComplete = "Vence: "+ expires;

        // 4. Set the text for textView
        product_name.setText(myStockModel.getProduct_name());
        farmer.setText(myStockModel.getUser_name());
        price.setText(priceCOPString);
        unit.setText(myStockModel.getUnit_name());
        qty.setText(qtyComplete);
        expiresAt.setText(expiresComplete);

        // 5. return rowView
        return rowView;
    }
}
