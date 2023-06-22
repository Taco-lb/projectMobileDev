package com.example.projectlab;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.net.Uri;
import android.view.View;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import android.widget.ArrayAdapter;
public class MainActivity extends AppCompatActivity {

    final Calendar myCalendar= Calendar.getInstance();
    Handler myHandler= new Handler();

    int userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showCalc();
        calendarOpen();
        autoCompleteText();
        spinnerChoices();
        changeTitleName();
    }

    public void calendarOpen(){
        EditText calendar =(EditText) findViewById(R.id.dateExp);

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
                new DatePickerDialog(MainActivity.this,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    public void showCalc() {
        ImageButton calc = findViewById(R.id.calcBtn);
        calc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getPackageManager().getLaunchIntentForPackage("com.google.android.calculator");
                startActivity(intent);
            }
        });
    }

    public void onClickAddExpense(View view){
        ProgressBar pb1 = (ProgressBar) findViewById(R.id.progressBar);
        TableLayout tb1 = (TableLayout) findViewById(R.id.tl1);
        //Make it so that if it is empty it wont add a new row to the database

        AutoCompleteTextView edt1;
        edt1 = findViewById(R.id.exp);

        EditText edt2,edt3;
        edt2 = findViewById(R.id.priceExp);
        edt3 = findViewById(R.id.dateExp);

        RadioButton rb1, rb2;
        rb1 = findViewById(R.id.rbCard);
        rb2 = findViewById(R.id.rbCash);

        RadioGroup rg1;
        rg1 = findViewById(R.id.radioGroup);

        Spinner sp1 = findViewById(R.id.spin1);

        String sedt1 = edt1.getText().toString();
        String sedt2 = edt2.getText().toString();
        String sedt3 = edt3.getText().toString();
        String ssp1 = sp1.getSelectedItem().toString();
        int ssp2 = sp1.getSelectedItemPosition();

        if(sedt1.matches("") || sedt2.matches("") || sedt3.matches("") || (!rb1.isChecked() && !rb2.isChecked()) || ssp2 == 0 ){
            Toast.makeText(getBaseContext(), "Please fill in all of the fields", Toast.LENGTH_SHORT).show();
        }

        else {
            tb1.setVisibility(View.INVISIBLE);
            pb1.setVisibility(View.VISIBLE);
            Runnable r = new Runnable() {

                @Override
                public void run() {
                    ContentValues values = new ContentValues();
                    values.put(ExpensesDb.EXPNAME, sedt1);
                    values.put(ExpensesDb.EXPRICE, sedt2);
                    values.put(ExpensesDb.EXPDATE, sedt3);
                    values.put(ExpensesDb.EXPCAT, ssp1);
                    values.put(ExpensesDb.EXPUSERID, ExpensesDb.USERFINALID);

                    if(rb1.isChecked()) {
                        values.put(ExpensesDb.EXPTYPE, ((RadioButton) findViewById(R.id.rbCard)).getText().toString());
                    }
                    else{
                        values.put(ExpensesDb.EXPTYPE, ((RadioButton) findViewById(R.id.rbCash)).getText().toString());
                    }

                    Uri uri = getContentResolver().insert(ExpensesDb.CONTENT_URI, values);

                    edt1.getText().clear();
                    edt2.getText().clear();
                    edt3.getText().clear();
                    rg1.clearCheck();
                    sp1.setSelection(0);


                    Toast.makeText(getBaseContext(), "Expense was added", Toast.LENGTH_SHORT).show();
                    tb1.setVisibility(View.VISIBLE);
                    pb1.setVisibility(View.INVISIBLE);
                }
            };

            myHandler.postDelayed(r,500);
        }
    }

    public void onClickRetrieveExpenses(View view){
        Intent openNext = new Intent(MainActivity.this,ExpenseView.class);
        startActivity(openNext);
    }

    public void onClearFields(View view) {
        ProgressBar pb1 = (ProgressBar) findViewById(R.id.progressBar);
        TableLayout tb1 = (TableLayout) findViewById(R.id.tl1);

        EditText edt2,edt3;
        AutoCompleteTextView edt1;
        edt1 = findViewById(R.id.exp);
        edt2 = findViewById(R.id.priceExp);
        edt3 = findViewById(R.id.dateExp);

        RadioButton rb1, rb2;
        rb1 = findViewById(R.id.rbCard);
        rb2 = findViewById(R.id.rbCash);

        RadioGroup rg1;
        rg1 = findViewById(R.id.radioGroup);

        Spinner sp1;
        sp1 = findViewById(R.id.spin1);

        String sedt1 = edt1.getText().toString();
        String sedt2 = edt2.getText().toString();
        String sedt3 = edt3.getText().toString();
        String ssp1 = sp1.getSelectedItem().toString();
        int ssp2 = sp1.getSelectedItemPosition();


        if(sedt1.matches("") && sedt2.matches("") && sedt3.matches("") && !rb1.isChecked() && !rb2.isChecked() && ssp2 == 0 ){
            Toast.makeText(getBaseContext(), "Nothing to Clear :)", Toast.LENGTH_SHORT).show();
        }

        else {
            tb1.setVisibility(View.INVISIBLE);
            pb1.setVisibility(View.VISIBLE);
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    edt1.getText().clear();
                    edt2.getText().clear();
                    edt3.getText().clear();
                    sp1.setSelection(0);
                    rg1.clearCheck();
                    Toast.makeText(getBaseContext(), "All Fields Cleared", Toast.LENGTH_SHORT).show();
                    tb1.setVisibility(View.VISIBLE);
                    pb1.setVisibility(View.INVISIBLE);
                }
            };
            myHandler.postDelayed(r, 500);
        }
    }

    public void autoCompleteText(){
        AutoCompleteTextView edt1 = findViewById(R.id.exp);
        String [] choices = getResources().getStringArray(R.array.choices_autocomplete);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, choices);
        edt1.setAdapter(adapter);
    }


    public void spinnerChoices(){
        Spinner spin = findViewById(R.id.spin1);
        String [] choices = getResources().getStringArray(R.array.choices_spinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, choices);
        spin.setAdapter(adapter);
    }

    //Method prevents the previous activities from being accessed after the click
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    private void changeTitleName(){
        TextView mainTitle = (TextView) findViewById(R.id.ExpensesMainTitle);
        mainTitle.setText("Welcome, " + ExpensesDb.USERFINAL +" !");
    }

    public void showAlertDialog(View v){
        AlertDialog.Builder alertBox = new AlertDialog.Builder(this);
        alertBox.setTitle("Log out");
        alertBox.setMessage("Are you sure you want to log out?");
        alertBox.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent openNext = new Intent(MainActivity.this,LoginScreen.class);
                startActivity(openNext);
                finish();
            }
        });
        alertBox.setNegativeButton("No", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){

            }
        });
        alertBox.create().show();
    }



}