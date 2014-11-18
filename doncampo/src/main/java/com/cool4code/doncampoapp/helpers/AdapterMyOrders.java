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
 * Created by marcosantonioaguilerely on 11/9/14.
 */
public class AdapterMyOrders extends ArrayAdapter<MyOrdersModel> {
    private final Context context;
    private final ArrayList<MyOrdersModel> myPurchasesArrayList;

    public AdapterMyOrders(Context context, ArrayList<MyOrdersModel> myPurchasesArrayList) {
        super(context, R.layout.my_orders_listview, myPurchasesArrayList);
        this.context = context;
        this.myPurchasesArrayList = myPurchasesArrayList;
    }

    public int getCount() {
        if (myPurchasesArrayList != null)
            return myPurchasesArrayList.size();
        return 0;
    }

    public MyOrdersModel getItem(int position) {
        if (myPurchasesArrayList != null)
            return myPurchasesArrayList.get(position);
        return null;
    }

    public long getItemId(int position) {
        if (myPurchasesArrayList != null)
            return myPurchasesArrayList.get(position).getId_Order();
        return 0;
    }

    public ArrayList getAllData(int position){
        MyOrdersModel myOrdersModel= myPurchasesArrayList.get(position);
        ArrayList<String> arrayData = new ArrayList<String>();
        String Id = Integer.toString(myOrdersModel.getId_Order());
        String Product_Name = myOrdersModel.getProduct_Name();
        String Unit_Name = myOrdersModel.getUnit_Name();
        String Qty = Integer.toString(myOrdersModel.getStock_Qty());

        /*formating local currency*/
        double Price = myOrdersModel.getPricePerUnit();
        NumberFormat defaultFormat = NumberFormat.getCurrencyInstance();

        Locale COP = new Locale("es", "CO");
        NumberFormat copFormat = NumberFormat.getCurrencyInstance(COP);
        Log.d("COPFormat", "==>" + copFormat.format(Price));
        String priceCOPString = copFormat.format(Price);

        String ExpiresAt = myOrdersModel.getExpiresAt();
        String Address = myOrdersModel.getAddress() + ", " + myOrdersModel.getGeo_State() + ", " + myOrdersModel.getCountry();
        String Farmer = myOrdersModel.getName();
        String Email = myOrdersModel.getEmail();
        String Phone = myOrdersModel.getPhone();

        arrayData.add(Id);
        arrayData.add(Product_Name);
        arrayData.add(Unit_Name);
        arrayData.add(Qty);
        arrayData.add(priceCOPString);
        arrayData.add(ExpiresAt);
        arrayData.add(Address);
        arrayData.add(Farmer);
        arrayData.add(Email);
        arrayData.add(Phone);

        return  arrayData;
    }

    @Override
    public boolean isEnabled(int position){
        return true;
    }

    //@Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // 1. Create inflater
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 2. Get rowView from inflater
        View rowView = inflater.inflate(R.layout.my_orders_listview, parent, false);

        // 3. Get the two text view from the rowView
        TextView product_name = (TextView) rowView.findViewById(R.id.orders_productname);
        TextView farmer = (TextView) rowView.findViewById(R.id.orders_farmer);
        TextView price = (TextView) rowView.findViewById(R.id.orders_priceunit);
        TextView unit = (TextView) rowView.findViewById(R.id.orders_unit);
        TextView qty = (TextView) rowView.findViewById(R.id.orders_qty);
        TextView expiresAt = (TextView) rowView.findViewById(R.id.orders_expiresat);
        TextView address = (TextView) rowView.findViewById(R.id.orders_address);

        MyOrdersModel myOrdersModel = myPurchasesArrayList.get(position);

        /*formating local currency*/
        int qtyValue = myOrdersModel.getStock_Qty();
        String comprado = myOrdersModel.getExpiresAt();
        double priceValue = myOrdersModel.getPricePerUnit();
        NumberFormat defaultFormat = NumberFormat.getCurrencyInstance();

        Locale COP = new Locale("es", "CO");
        NumberFormat copFormat = NumberFormat.getCurrencyInstance(COP);
        Log.d("COPFormat", "==>" + copFormat.format(priceValue));
        String priceCOPString = copFormat.format(priceValue);
        String qtyString = Integer.toString(qtyValue);
        String qtyComplete = "Cantidad: " + qtyString;
        String compradoComplete = "Fecha: "+ comprado;

        /*concat address to view*/
        String town = myOrdersModel.getTown();
        String state = myOrdersModel.getGeo_State();
        String concatAddress = town + ", " + state;

        // 4. Set the text for textView
        product_name.setText(myOrdersModel.getProduct_Name());
        farmer.setText(myOrdersModel.getName());
        price.setText(priceCOPString);
        unit.setText(myOrdersModel.getUnit_Name());
        qty.setText(qtyComplete);
        expiresAt.setText(compradoComplete);
        address.setText(concatAddress);
        // 5. return rowView
        return rowView;
    }
}
