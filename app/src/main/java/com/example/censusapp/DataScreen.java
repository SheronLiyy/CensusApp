package com.example.censusapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class DataScreen extends AppCompatActivity {

    EditText name,age;
    MediaStore.Audio.Radio radioBtn;

    Button cameraButton;
    Button saveButton;
    Button viewButton;
    DBHelper DB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_screen);

            cameraButton = (Button)findViewById(R.id.cameraButton);
            saveButton = (Button)findViewById(R.id.savebtn);
            viewButton = (Button)findViewById(R.id.viewbtn);
            DB = new DBHelper(this);

            name = findViewById(R.id.entername);
            age = findViewById(R.id.enterage);

            cameraButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Open the camera
                    try{
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivity(intent);
                    }catch(Exception e)
                    {
                        e.printStackTrace();
                    }

                }
            });

            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String nameText = name.getText().toString();
                    String ageText = age.getText().toString();
                    Boolean savedata = DB.saveuserdata(nameText,ageText);
                    if (TextUtils.isEmpty(nameText) || TextUtils.isEmpty(ageText)){
                        Toast.makeText(DataScreen.this,"Add user details", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    else{
                        if(savedata == true){
                            Toast.makeText(DataScreen.this, "Save user data", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(DataScreen.this, "Exists User Details", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });

            viewButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Cursor WL = DB.gettext();
                    if (WL.getCount() == 0){
                        Toast.makeText(DataScreen.this, "User data does not exist", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    StringBuffer buffer = new StringBuffer();
                    while (WL.moveToNext()){
                        buffer.append("Name: " + WL.getString(0) + "\n");
                        buffer.append("Age: " + WL.getString(0) + "\n\n");

                    }

                    AlertDialog.Builder builder = new AlertDialog.Builder(DataScreen.this);
                    builder.setCancelable(true);
                    builder.setTitle("User Data");
                    builder.setMessage(buffer.toString());
                    builder.show();


                }
            });





        }

    }
