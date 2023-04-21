package com.example.censusapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class DataScreen extends AppCompatActivity {

    EditText name,age;
    Button cameraButton;
    Button saveButton;
    Button viewButton;
    DBHelper DB;
    RadioGroup radioGroup;
    RadioButton genderRadioButton;
    String gender,entryTime;
    Uri imageUri;
    ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_screen);

            cameraButton = (Button)findViewById(R.id.cameraButton);
            saveButton = (Button)findViewById(R.id.savebtn);
            viewButton = (Button)findViewById(R.id.viewbtn);
            radioGroup = (RadioGroup)findViewById(R.id.radioBtnGroup);
            name = findViewById(R.id.entername);
            age = findViewById(R.id.enterage);
            imageView = findViewById(R.id.image_view_person);

            DB = new DBHelper(this);

        if(ContextCompat.checkSelfPermission(DataScreen.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(DataScreen.this, new String[]{android.Manifest.permission.CAMERA}, 101);
        }

        entryTime = String.valueOf(System.currentTimeMillis());

            cameraButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    Uri imagePath = saveImage();
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imagePath);
                    startActivityForResult(intent, 101);
                }
            });

            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int selectedId = radioGroup.getCheckedRadioButtonId();
                    genderRadioButton = (RadioButton) findViewById(selectedId);



                    if(selectedId==-1){
                        Toast.makeText(DataScreen.this,"Select your gender", Toast.LENGTH_SHORT).show();
                    } else if (name.getText().toString().isEmpty()) {
                        Toast.makeText(DataScreen.this,"Type your name", Toast.LENGTH_SHORT).show();
                    } else if (age.getText().toString().isEmpty()) {
                        Toast.makeText(DataScreen.this,"Type your age", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(DataScreen.this, "Saved user data", Toast.LENGTH_SHORT).show();
                    }

                    String nameText = name.getText().toString();
                    String ageText = age.getText().toString();
                    String genderText = genderRadioButton.getText().toString();


                    Boolean savedData = DB.saveuserdata(nameText,ageText,genderText);

                    if (TextUtils.isEmpty(nameText) || TextUtils.isEmpty(ageText) || TextUtils.isEmpty(gender)){
                        Toast.makeText(DataScreen.this,"Add user details", Toast.LENGTH_SHORT).show();
                        return;
                    } else{
                        if(savedData == true){
                            name.setText("");
                            age.setText("");
                            imageView.setImageResource(R.drawable.person2_image);
                        }
                        else{
                            Toast.makeText(DataScreen.this, "Exists User Details", Toast.LENGTH_SHORT).show();
                        }
                    }
                    
                   startActivity(new Intent(DataScreen.this, UserList.class));
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
                        buffer.append("Age: " + WL.getString(1) + "\n");
                        buffer.append("Gender: " + WL.getString(2) + "\n\n");

                    }

                    AlertDialog.Builder builder = new AlertDialog.Builder(DataScreen.this);
                    builder.setCancelable(true);
                    builder.setTitle("User Data");
                    builder.setMessage(buffer.toString());
                    builder.show();

                }
            });

        }


    private Uri saveImage() {
        Uri uri = null;

        ContentResolver resolver = getContentResolver();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            uri = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY); //DCIM or Pictures
        }else {
            uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        }

        String imgName = entryTime;
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, imgName + ".jpg");
        contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/"+"User Images/");
        Uri finalUri = resolver.insert(uri, contentValues);
        imageUri = finalUri;
        return finalUri;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {
            if(resultCode == Activity.RESULT_OK){
                imageView.setImageURI(imageUri);
            }
        }
    }

    }
