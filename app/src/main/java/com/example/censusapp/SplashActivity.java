
package com.example.censusapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        sp = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE);
        String password = sp.getString("text","");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(password != null){
                    startActivity(new Intent(SplashActivity.this, LoginScreen.class));
                    finish();
                }
                else{
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                }
            }
        }, 3000);

    }

}
