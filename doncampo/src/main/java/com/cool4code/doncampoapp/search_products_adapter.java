package com.cool4code.doncampoapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cool4code team on 7/8/14.
 * David Almeciga
 * Marcos A. Aguilera Ely
 */

public class search_products_adapter extends BaseAdapter{

    Context mContext;
    LayoutInflater inflater;
    private List<model_products> productsList= null;
    private ArrayList<model_products> arrayList;

    public search_products_adapter(Context context,
                                   List<model_products> productsList){
        mContext= context;
        this.productsList= productsList;
        inflater= LayoutInflater.from(mContext);
        this.arrayList= new ArrayList<model_products>();
        this.arrayList.addAll(productsList);
    }

    public class ViewHolder{
        TextView product_codeBox;
        TextView product_nameBox;
        TextView product_createdBox;
    }

    @Override
    public int getCount() {
        return productsList.size();
    }

    @Override
    public Object getItem(int position) {
        return productsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if(view == null){
            holder= new ViewHolder();
            view = inflater.inflate(R.layout.product_listview, null);
            //holder.product_codeBox= (TextView) view.findViewById(R.id.product_codeBox);
            //holder.product_nameBox= (TextView) view.findViewById(R.id.product_nameBox);
            //holder.product_createdBox= (TextView) view.findViewById(R.id.product_createdBox);
            view.setTag(holder);
        }else{
            holder= (ViewHolder) view.getTag();
        }
        //holder.product_codeBox.setText(productsList.get(position).getCode());
        //holder.product_nameBox.setText(productsList.get(position).getName());
        //holder.product_createdBox.setText(productsList.get(position).getCreatedAt());
        return view;
    }
}
