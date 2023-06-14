package com.example.projectlab;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

public class LoginScreen extends AppCompatActivity {

    protected boolean loginClick = false;

    public String usernameFinal = "";

    public int userIdFinal = 0;

    SQLiteDatabase db = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        redirectToRegister();
    }

    public void redirectToRegister(){
        TextView registerText = (TextView) findViewById(R.id.registerHereText);
        registerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openNext = new Intent(LoginScreen.this,RegisterScreen.class);
                startActivity(openNext);
                finish();
            }
        });
    }

    public void onLoginScreen(View v) {

        int errors = 0;
        boolean usernameCheck = false;

        EditText username, password;
        username = findViewById(R.id.usernameFieldLogin);
        password = findViewById(R.id.passwordFieldLogin);

        String susername = username.getText().toString();
        String spassword = password.getText().toString();

        if (susername.matches("") || spassword.matches("")) {
            Toast.makeText(getBaseContext(), "Please fill in all of the fields", Toast.LENGTH_SHORT).show();
            errors++;
        }
        else {
            Uri users = ExpensesDb.CONTENT_URI_USER;
            Cursor cursor = getContentResolver().query(users, null, null, null, "_userid");

            if (cursor.moveToFirst()) {
                do {
                    if (!cursor.getString(cursor.getColumnIndex(ExpensesDb.USERPASSWORD)).equals(spassword) && cursor.getString(cursor.getColumnIndex(ExpensesDb.USERNAME)).equals(susername)) {
                        Toast.makeText(getBaseContext(), "Password does not match Username", Toast.LENGTH_LONG).show();
                        errors++;
                    }
                    if(cursor.getString(cursor.getColumnIndex(ExpensesDb.USERNAME)).equals(susername)) {
                        usernameCheck = true;
                    }
                } while (cursor.moveToNext());
            }
            if(!usernameCheck){
                Toast.makeText(getBaseContext(), "Username does not exist", Toast.LENGTH_LONG).show();
                errors++;
            }
        }

        if(errors == 0) {

            ///I ADDED THIS HERE IN ORDER TO GET THE USERID AND PASS IT TO THE TABLE
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

            Intent openNext = new Intent(this, MainActivity.class);
            startActivity(openNext);
            finish();
        }
    }

}