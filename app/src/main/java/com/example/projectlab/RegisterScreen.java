package com.example.projectlab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class RegisterScreen extends AppCompatActivity {

    ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_screen);
        redirectToLogin();
    }


    public void redirectToLogin() {
        TextView LoginText = (TextView) findViewById(R.id.loginHereText);
        LoginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openNext = new Intent(RegisterScreen.this, LoginScreen.class);
                startActivity(openNext);
                finish();
            }
        });
    }


    public void onRegisterScreen(View v) {

        ContentValues values = new ContentValues();
        int errors = 0;

        EditText username, email, password;
        username = findViewById(R.id.usernameFieldRegister);
        email = findViewById(R.id.emailFieldRegister);
        password = findViewById(R.id.passwordFieldRegister);

        String susername = username.getText().toString();
        String semail = email.getText().toString();
        String spassword = password.getText().toString();


        if (susername.matches("") || semail.matches("") || spassword.matches("")) {
            Toast.makeText(getBaseContext(), "Please fill in all of the fields", Toast.LENGTH_SHORT).show();
            errors++;
        }
        else if (spassword.length() <6) {
            Toast.makeText(getBaseContext(), "Password must be above 6 characters", Toast.LENGTH_SHORT).show();
            errors++;
        }
        else if (susername.length() > 12) {
            Toast.makeText(getBaseContext(), "Username must not be longer than 12 characters", Toast.LENGTH_SHORT).show();
            errors++;
        }
        else {
            values.put(ExpensesDb.USERNAME, susername);
            values.put(ExpensesDb.USERMAIL, semail);
            values.put(ExpensesDb.USERPASSWORD, spassword);

            Uri users = ExpensesDb.CONTENT_URI_USER;
            Cursor cursor = getContentResolver().query(users, null, null, null, "_userid");

            if (cursor.moveToFirst()) {
                do {
                    if (cursor.getString(cursor.getColumnIndex(ExpensesDb.USERNAME)).equals(susername)) {
                        Toast.makeText(getBaseContext(), "Username already in use", Toast.LENGTH_LONG).show();
                        errors++;
                    }
                    if (cursor.getString(cursor.getColumnIndex(ExpensesDb.USERMAIL)).equals(semail)) {
                        Toast.makeText(getBaseContext(), "Email already in use", Toast.LENGTH_LONG).show();
                        errors++;
                    }
                } while (cursor.moveToNext());
            }
        }
        if(errors == 0) {
            Uri uri = getContentResolver().insert(ExpensesDb.CONTENT_URI_USER, values);
            Toast.makeText(getBaseContext(), "Account Created", Toast.LENGTH_SHORT).show();
            ExpensesDb.USERFINAL = susername;
            Uri users = ExpensesDb.CONTENT_URI_USER;
            Cursor cursor = getContentResolver().query(users, null, null, null, "_userid");
            int temp = 0;
            while(cursor.moveToNext()) {
                if (cursor.getString(1).equals(susername)) {
                    temp = cursor.getInt(0);
                }
            }

            ExpensesDb.USERFINALID = temp;

            Intent openNext = new Intent(RegisterScreen.this, MainActivity.class);
            startActivity(openNext);
            finish();
        }
    }




}