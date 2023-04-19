package com.example.censusapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import yuku.ambilwarna.AmbilWarnaDialog;

public class Preferences extends AppCompatActivity {

    Button button;
    Button homeScreenBtn;
    RelativeLayout relativeLayout;
    int  defaultColor;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);
        
        button = (Button)findViewById(R.id.colorpickerbtn);
        homeScreenBtn = (Button)findViewById(R.id.homescreenbtn);

        relativeLayout = findViewById(R.id.layout);

        defaultColor = ContextCompat.getColor(Preferences.this, com.google.android.material.R.color.design_default_color_primary);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openColorPicker();
            }
        });

        homeScreenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Preferences.this, HomeScreen.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void openColorPicker(){
        AmbilWarnaDialog ambilWarnaDialog = new AmbilWarnaDialog(this, defaultColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {

            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                defaultColor = color;
                relativeLayout.setBackgroundColor(defaultColor);
            }
        });

        ambilWarnaDialog.show();
    }


}