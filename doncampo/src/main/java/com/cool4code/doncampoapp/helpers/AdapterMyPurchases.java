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
public class AdapterMyPurchases extends ArrayAdapter<MyPurchasesModel> {
    private final Context context;
    private final ArrayList<MyPurchasesModel> myPurchasesArrayList;

    public AdapterMyPurchases(Context context, ArrayList<MyPurchasesModel> myPurchasesArrayList) {
        super(context, R.layout.my_purchases_listview, myPurchasesArrayList);
        this.context = context;
        this.myPurchasesArrayList = myPurchasesArrayList;
    }

    public int getCount() {
        if (myPurchasesArrayList != null)
            return myPurchasesArrayList.size();
        return 0;
    }

    public MyPurchasesModel getItem(int position) {
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
        MyPurchasesModel myPurchasesModel= myPurchasesArrayList.get(position);
        ArrayList<String> arrayData = new ArrayList<String>();
        String Id = Integer.toString(myPurchasesModel.getId_Order());
        String Product_Name = myPurchasesModel.getProduct_Name();
        String Unit_Name = myPurchasesModel.getUnit_Name();
        String Qty = Integer.toString(myPurchasesModel.getStock_Qty());

        /*formating local currency*/
        double Price = myPurchasesModel.getPricePerUnit();
        NumberFormat defaultFormat = NumberFormat.getCurrencyInstance();

        Locale COP = new Locale("es", "CO");
        NumberFormat copFormat = NumberFormat.getCurrencyInstance(COP);
        Log.d("COPFormat", "==>" + copFormat.format(Price));
        String priceCOPString = copFormat.format(Price);

        String ExpiresAt = myPurchasesModel.getExpiresAt();
        String Address = myPurchasesModel.getAddress() + ", " + myPurchasesModel.getGeo_State() + ", " + myPurchasesModel.getCountry();
        String Farmer = myPurchasesModel.getName();
        String Email = myPurchasesModel.getEmail();
        String Phone = myPurchasesModel.getPhone();

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
        View rowView = inflater.inflate(R.layout.my_purchases_listview, parent, false);

        // 3. Get the two text view from the rowView
        TextView product_name = (TextView) rowView.findViewById(R.id.purchase_productname);
        TextView farmer = (TextView) rowView.findViewById(R.id.purchase_farmer);
        TextView price = (TextView) rowView.findViewById(R.id.purchase_priceunit);
        TextView unit = (TextView) rowView.findViewById(R.id.purchase_unit);
        TextView qty = (TextView) rowView.findViewById(R.id.purchase_qty);
        TextView expiresAt = (TextView) rowView.findViewById(R.id.purchase_expiresat);
        TextView address = (TextView) rowView.findViewById(R.id.purchase_address);

        MyPurchasesModel myPurchasesModel = myPurchasesArrayList.get(position);

        /*formating local currency*/
        int qtyValue = myPurchasesModel.getStock_Qty();
        String expires = myPurchasesModel.getExpiresAt();
        double priceValue = myPurchasesModel.getPricePerUnit();
        NumberFormat defaultFormat = NumberFormat.getCurrencyInstance();

        Locale COP = new Locale("es", "CO");
        NumberFormat copFormat = NumberFormat.getCurrencyInstance(COP);
        Log.d("COPFormat", "==>" + copFormat.format(priceValue));
        String priceCOPString = copFormat.format(priceValue);
        String qtyString = Integer.toString(qtyValue);
        String qtyComplete = "Cantidad: " + qtyString;
        String expiresComplete = "Vence: "+ expires;

        /*concat address to view*/
        String town = myPurchasesModel.getTown();
        String state = myPurchasesModel.getGeo_State();
        String concatAddress = town + ", " + state;

        // 4. Set the text for textView
        product_name.setText(myPurchasesModel.getProduct_Name());
        farmer.setText(myPurchasesModel.getName());
        price.setText(priceCOPString);
        unit.setText(myPurchasesModel.getUnit_Name());
        qty.setText(qtyComplete);
        expiresAt.setText(expiresComplete);
        address.setText(concatAddress);
        // 5. return rowView
        return rowView;
    }


}
