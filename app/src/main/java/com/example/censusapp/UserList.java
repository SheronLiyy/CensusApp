package com.example.censusapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;

public class UserList extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList <String> name, age, gender;
    DBHelper DB;
    MyAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        DB = new DBHelper(this);
        name = new ArrayList<>();
        age = new ArrayList<>();
        gender = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerview);
        adapter = new MyAdapter(this, name, age, gender);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        displaydata();
    }

    private void displaydata() {

        Cursor cursor = DB.gettext();
        if (cursor.getCount()==0) {
            Toast.makeText(UserList.this, "No data entered", Toast.LENGTH_SHORT).show();
            return;
        }else{
                while (cursor.moveToNext()){
                    name.add(cursor.getString(0));
                    age.add(cursor.getString(1));
                    gender.add(cursor.getString(2));
                }
            }
        }
    }
