package com.example.map;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class ParentProfileActivity extends AppCompatActivity {

    private TextView profileName;
    private TextView phoneNo;

    ReadStorageData data = new ReadStorageData();//reading user data

    ImageView backButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        backButton = (ImageView) findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        profileName = findViewById(R.id.profileid);
        phoneNo = findViewById(R.id.phone_no);

        data.read();

        profileName.setText(data.name);
        phoneNo.setText(data.mobileNo);




    }
}
