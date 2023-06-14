package com.example.projectlab;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ExpensesAdapter extends ArrayAdapter<UsersData> {
    private Context mContext;
    int mResource;

    public ExpensesAdapter(Context context, int resource, ArrayList<UsersData> data) {
        super(context, resource, data);
        mContext = context;
        mResource = resource;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        String name = getItem(position).getName();
        String price = getItem(position).getPrice();
        String date = getItem(position).getDate();
        String type = getItem(position).getType();
        String category = getItem(position).getForwho();
        String id = getItem(position).getId();

        UsersData exp = new UsersData(name,price,date,type,category);

        LayoutInflater inflater = LayoutInflater.from(mContext);

        convertView = inflater.inflate(mResource, parent, false);

        TextView n1 = (TextView) convertView.findViewById(R.id.Name1);

        TextView p1 = (TextView) convertView.findViewById(R.id.Price1);

        TextView d1 = (TextView) convertView.findViewById(R.id.Date1);

        TextView t1 = (TextView) convertView.findViewById(R.id.Type1);

        TextView c1 = (TextView) convertView.findViewById(R.id.category1);

        TextView i1 = (TextView) convertView.findViewById(R.id.idValue);

        n1.setText(name);
        p1.setText(price);
        d1.setText(date);
        t1.setText(type);
        c1.setText(category);
        i1.setText(id);

        return convertView;
    }
}
