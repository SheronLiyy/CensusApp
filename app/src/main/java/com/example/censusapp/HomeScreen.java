package com.example.censusapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeScreen extends AppCompatActivity {

    private Button preferenceBtn;
    private Button dataScreenBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        preferenceBtn = (Button)findViewById(R.id.preferenceBtn);
        dataScreenBtn = (Button)findViewById(R.id.datascreenbtn);



        preferenceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeScreen.this, Preferences.class);
                startActivity(intent);
                finish();
            }
        });

        dataScreenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeScreen.this, DataScreen.class);
                startActivity(intent);
                finish();
            }
        });


    }
}