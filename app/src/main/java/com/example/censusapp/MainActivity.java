package com.example.censusapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {
    private EditText password;
    private EditText rePassword;
    private Button loginBtn;
    private String text;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String TEXT = "text";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        password = (EditText) findViewById(R.id.password);
        rePassword = (EditText) findViewById(R.id.reenterpassword);
        loginBtn = (Button) findViewById(R.id.loginbtn);

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);

        String check = sharedPreferences.getString(TEXT,null);

        /*if(check!=null){
            Intent intent = new Intent(MainActivity.this, LoginScreen.class);
            startActivity(intent);
            finish();
        }*/


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
            }
        });

        loadData();

    }

    public void saveData(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if(password!=rePassword){
            Toast.makeText(this,"Passwords do not match!",Toast.LENGTH_SHORT);
        }
        editor.putString(TEXT, password.getText().toString());
        editor.apply();

        Toast.makeText(this,"Password saved!",Toast.LENGTH_SHORT).show();



        Intent intent = new Intent(MainActivity.this,LoginScreen.class);
        startActivity(intent);
        finish();


    }

    public void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        text = sharedPreferences.getString(TEXT,"");
    }
}