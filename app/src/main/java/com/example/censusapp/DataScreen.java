package com.example.censusapp;

import androidx.annotation.NonNull;
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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class DataScreen extends AppCompatActivity {

    private static final String TAG = "DataScreen";


    private static final String KEY_NAME = "name";
    private static final String KEY_AGE = "age";
    private static final String KEY_GENDER = "gender";
    private static final String KEY_PHOTO = "photo";


    EditText name,age;
    Button cameraButton;
    Button saveButton;
    Button viewButton;
    Button sendToCloudButton;
    DBHelper DB;
    RadioGroup radioGroup;
    RadioButton genderRadioButton;
    String gender,entryTime;
    Uri imageUri;
    ImageView imageView;

    private FirebaseFirestore DB2 =  FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_screen);

            cameraButton = (Button)findViewById(R.id.cameraButton);
            saveButton = (Button)findViewById(R.id.savebtn);
            viewButton = (Button)findViewById(R.id.viewbtn);
            sendToCloudButton = (Button)findViewById(R.id.sendtocloudbtn);
            radioGroup = (RadioGroup)findViewById(R.id.radioBtnGroup);
            name = (EditText) findViewById(R.id.entername);
            age = (EditText) findViewById(R.id.enterage);
            imageView = (ImageView) findViewById(R.id.image_view_person);

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

                    String nameText = name.getText().toString();
                    String ageText = age.getText().toString();
                    String genderText = genderRadioButton.getText().toString();

                    Boolean savedData = DB.saveuserdata(nameText,ageText,genderText);

                    if(selectedId==-1){
                        Toast.makeText(DataScreen.this,"Select your gender", Toast.LENGTH_SHORT).show();
                    } else if (name.getText().toString().isEmpty()) {
                        Toast.makeText(DataScreen.this,"Type your name", Toast.LENGTH_SHORT).show();
                    } else if (age.getText().toString().isEmpty()) {
                        Toast.makeText(DataScreen.this,"Type your age", Toast.LENGTH_SHORT).show();
                    } else {
                        if(savedData == true){
                            Toast.makeText(DataScreen.this, "Saved user data", Toast.LENGTH_SHORT).show();
                        } else{
                            Toast.makeText(DataScreen.this, "User data not saved", Toast.LENGTH_SHORT).show();
                        }

                    }

                }
            });

        viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DataScreen.this, UserList.class));
            }
        });


        sendToCloudButton.setOnClickListener(new View.OnClickListener() {
//            String nameText = name.getText().toString();
//            String ageText = age.getText().toString();
//            String genderText = genderRadioButton.getText().toString();
            @Override
            public void onClick(View view) {

                Cursor cursor = DB.gettext();
                if (cursor.getCount()==0) {
                    Toast.makeText(DataScreen.this, "No data entered", Toast.LENGTH_SHORT).show();
                    return;
                }else{

                    Map<String,Object> data = new HashMap<>();
                    while (cursor.moveToNext()){
                        String username = cursor.getString(0);
                        data.put(KEY_NAME,cursor.getString(0)+"\n");
                        data.put(KEY_AGE,cursor.getString(1)+"\n");
                        data.put(KEY_GENDER,cursor.getString(2)+"\n");


                        DB2.collection("Cencus APP").document(username).set(data)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(DataScreen.this, "Saved to cloud", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(DataScreen.this, "Error in saving to cloud!", Toast.LENGTH_SHORT).show();
                                        Log.d(TAG,e.toString());
                                    }
                                });
                    }
                }



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
