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

public class DriverProfileActivity extends AppCompatActivity {
    private TextView driverProfileName;
    private TextView driverMobileNo;
    ImageView backButton;

    ReadStorageData data = new ReadStorageData();//reading user data

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_profile);

        data.read();

        backButton=(ImageView)findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(DriverProfileActivity.this,DriverDashBoard.class);
                startActivity(intent);
                finish();
            }
        });

        driverProfileName = findViewById(R.id.profileid);
        driverMobileNo = findViewById(R.id.phone_no);

        driverProfileName.setText(data.name);
        driverMobileNo.setText(data.mobileNo);


    }

}
