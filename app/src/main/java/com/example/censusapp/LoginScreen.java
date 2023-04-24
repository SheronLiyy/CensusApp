package com.example.censusapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginScreen extends AppCompatActivity {

    private Button loginBtn;
    private Button signupBtn;
    private EditText editText;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String TEXT = "text";
    private int errorCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        loginBtn = (Button)findViewById(R.id.loginbtn);
        signupBtn = (Button)findViewById(R.id.signupbtn);
        //signupBtn = (Button) findViewById(R.id.signupbtn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                String password = sharedPreferences.getString(TEXT, "");
                editText = (EditText) findViewById(R.id.loginpassword);

                if (password.equals(editText.getText().toString())) {
                    Intent intent = new Intent(LoginScreen.this, HomeScreen.class);
                    startActivity(intent);
                    finish();
                } else {
                    editText.setError("Wrong password");
                    errorCount++;
                    if (errorCount >= 3) {
                        Context context = getApplicationContext();
                        CharSequence text = "Wrong password, Closing App";
                        int duration = Toast.LENGTH_LONG;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();

                        LoginScreen.this.finish();
                        System.exit(0);

                    }


                }
            }


        });

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginScreen.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
}}