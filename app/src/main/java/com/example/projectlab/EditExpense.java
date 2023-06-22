package com.example.projectlab;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.net.Uri;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import android.database.Cursor;

public class EditExpense extends AppCompatActivity {

    final Calendar myCalendar= Calendar.getInstance();
    String tempID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_expense);
        calendarOpen();
        fillValues();

    }

    public void calendarOpen(){
        EditText calendar =(EditText) findViewById(R.id.dateExp2);

        DatePickerDialog.OnDateSetListener date =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                String myFormat = "dd/MM/yyyy";
                SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
                calendar.setText(dateFormat.format(myCalendar.getTime()));
            }
        };

        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(EditExpense.this,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    protected void fillValues(){
        Intent intent = getIntent();

        TextView expense = (TextView) findViewById(R.id.exp2);
        expense.setText(intent.getStringExtra("expense"));

        TextView price = (TextView) findViewById(R.id.priceExp2);
        price.setText(intent.getStringExtra("price").substring(2));

        TextView date = (TextView) findViewById(R.id.dateExp2);
        date.setText(intent.getStringExtra("date"));

        RadioButton rb1 = (RadioButton) findViewById(R.id.rbCard2);
        RadioButton rb2 = (RadioButton) findViewById(R.id.rbCash2);
        if(intent.getStringExtra("paytype").equals("Card")){
            rb1.setChecked(true);
        }
        else{
            rb2.setChecked(true);
        }

        //Spinner part of the setup
        Spinner spin = findViewById(R.id.spin2);
        String [] choices = getResources().getStringArray(R.array.choices_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, choices);
        spin.setAdapter(adapter);
        String myString = intent.getStringExtra("category");
        int selectionPosition= adapter.getPosition(myString);
        spin.setSelection(selectionPosition);
        tempID = intent.getStringExtra("id");

    }

    public void deleteExpense(View view){
        Uri uri = ExpensesDb.CONTENT_URI;

        AlertDialog.Builder alertBox = new AlertDialog.Builder(this);
        alertBox.setTitle("Delete Exepense");
        alertBox.setMessage("Are you sure you want to delete this expense?");
        alertBox.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int flag = getContentResolver().delete(uri, ExpensesDb._ID + "=" + tempID, null);
                if(flag>0) {
                    Toast.makeText(getBaseContext(), "Expense deleted", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(EditExpense.this, ExpenseView.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
        alertBox.setNegativeButton("No", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                Toast.makeText(getBaseContext(),"Expense not Deleted",Toast.LENGTH_SHORT).show();
                Intent openNext = new Intent(EditExpense.this,ExpenseView.class);
                startActivity(openNext);
                finish();
            }
        });
        alertBox.create().show();
    }

    public void updateExpense(View view){

        AutoCompleteTextView edt1;
        edt1 = findViewById(R.id.exp2);

        EditText edt2,edt3;
        edt2 = findViewById(R.id.priceExp2);
        edt3 = findViewById(R.id.dateExp2);

        RadioButton rb1, rb2;
        rb1 = findViewById(R.id.rbCard2);
        rb2 = findViewById(R.id.rbCash2);

        RadioGroup rg1;
        rg1 = findViewById(R.id.radioGroup2);

        Spinner sp1 = findViewById(R.id.spin2);

        String sedt1 = edt1.getText().toString();
        String sedt2 = edt2.getText().toString();
        String sedt3 = edt3.getText().toString();
        String ssp1 = sp1.getSelectedItem().toString();
        int ssp2 = sp1.getSelectedItemPosition();

        if(sedt1.matches("") || sedt2.matches("") || sedt3.matches("") || (!rb1.isChecked() && !rb2.isChecked()) || ssp2 == 0 ){
            Toast.makeText(getBaseContext(), "Please fill in all of the fields", Toast.LENGTH_SHORT).show();
        }

        else {
                    ContentValues values = new ContentValues();
                    values.put(ExpensesDb.EXPNAME, sedt1);
                    values.put(ExpensesDb.EXPRICE, sedt2);
                    values.put(ExpensesDb.EXPDATE, sedt3);
                    values.put(ExpensesDb.EXPCAT, ssp1);
                    values.put(ExpensesDb.EXPUSERID, ExpensesDb.USERFINALID);

                    if (rb1.isChecked()) {
                        values.put(ExpensesDb.EXPTYPE, ((RadioButton) findViewById(R.id.rbCard2)).getText().toString());
                    } else {
                        values.put(ExpensesDb.EXPTYPE, ((RadioButton) findViewById(R.id.rbCash2)).getText().toString());
                    }

            Uri uri = ExpensesDb.CONTENT_URI;
            int flag = getContentResolver().update(uri, values ,ExpensesDb._ID + "=" + tempID, null);
            if(flag>0) {
                Toast.makeText(getBaseContext(), "Expense Updated", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(EditExpense.this, ExpenseView.class);
                startActivity(intent);
                finish();
            }

            else{
                Toast.makeText(getBaseContext(),"Expense not Updated",Toast.LENGTH_SHORT).show();
            }
        }
    }

}