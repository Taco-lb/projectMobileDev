package com.example.projectlab;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ExpenseView extends AppCompatActivity {

    ExpensesDb dbHelp = new ExpensesDb();

    public String itemId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_view);
        populateList();
        togglePrice();
        clickListItem();
    }

    private void populateList() {
        Uri expenses = ExpensesDb.CONTENT_URI;
        Cursor cursor = getContentResolver().query(expenses, null, null, null, "_id");

        //An array list made of the special class i made to store the expenses properly.
        ArrayList<UsersData> dataList = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                if(cursor.getInt(6) == ExpensesDb.USERFINALID) {
                    UsersData dataValues = new UsersData(cursor.getString(cursor.getColumnIndex(ExpensesDb._ID)), cursor.getString(cursor.getColumnIndex(ExpensesDb.EXPNAME)), cursor.getString(cursor.getColumnIndex(ExpensesDb.EXPRICE)), cursor.getString(cursor.getColumnIndex(ExpensesDb.EXPDATE)), cursor.getString(cursor.getColumnIndex(ExpensesDb.EXPTYPE)), cursor.getString(cursor.getColumnIndex(ExpensesDb.EXPCAT)));
                    dataList.add(dataValues);
                    ExpensesAdapter adapter = new ExpensesAdapter(this, R.layout.single_item, dataList);
                    ListView listview = (ListView) findViewById(R.id.listViewExp);
                    listview.setAdapter(adapter);
                }

            } while (cursor.moveToNext());
        }
    }

    private String totalPrice(){
        Uri expenses = ExpensesDb.CONTENT_URI;
        Cursor cursor = getContentResolver().query(expenses, null, null, null, "_id");

        float result = 0;
        while(cursor.moveToNext()) {
            if(cursor.getInt(6) == ExpensesDb.USERFINALID) {
                result += cursor.getFloat(2);
            }
        }

        return String.valueOf(result);
    }

    public void togglePrice() {

        ToggleButton tb1 = (ToggleButton) findViewById(R.id.toggleColor);
        TextView t = (TextView) findViewById(R.id.totalPrice);
        DecimalFormat formatter = new DecimalFormat("#,###");

        float i = Float.parseFloat(totalPrice());
        float temp = i*94000;
        int tempLbp = (int)temp;

        String totalLbp = formatter.format(tempLbp);
        t.setText("Total expense in USD is: \n$ " + totalPrice() );

        tb1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(tb1.isChecked()){
                    t.setText("Total expense in LBP is : \n" + totalLbp + " LBP");
                }
                else{
                    t.setText("Total expense in USD is : \n$ " + totalPrice() );
                }
            }
        });
    }

    public void clickListItem(){
        ListView lv = (ListView) findViewById(R.id.listViewExp);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                UsersData item = (UsersData) parent.getItemAtPosition(position);
                Intent openNext = new Intent(ExpenseView.this, EditExpense.class);
                openNext.putExtra("id", item.getId());
                openNext.putExtra("expense", item.getName());
                openNext.putExtra("price", item.getPrice());
                openNext.putExtra("date", item.getDate());
                openNext.putExtra("paytype", item.getType());
                openNext.putExtra("category", item.getForwho());
                startActivity(openNext);
                finish();
            }
        });
    }

}