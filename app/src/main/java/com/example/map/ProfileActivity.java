package com.example.map;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class ProfileActivity extends AppCompatActivity {

    private TextView profileName;
    private TextView phoneNo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profileName = findViewById(R.id.profileid);
        phoneNo = findViewById(R.id.phone_no);

        StringBuilder stringBuilder = new StringBuilder();
        try {
            File textFile = new File(Environment.getExternalStorageDirectory(),"profile.txt");
            FileInputStream fileInputStream = new FileInputStream(textFile);



            if (fileInputStream!=null){
                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);


                String line = null;
                while ((line=bufferedReader.readLine())!=null){
                    stringBuilder.append(line);
                }
                fileInputStream.close();
            }
            String profile_data=stringBuilder.toString();
            try {
                JSONObject jsonObject=new JSONObject(profile_data);
                String name = jsonObject.getString("name");
                String phone = jsonObject.getString("phone");

                profileName.setText(name);
                phoneNo.setText(phone);
//                System.out.println("name"+name);
//                System.out.println("phone"+phone);

            } catch (JSONException e) {
                e.printStackTrace();

            }
            // Toast.makeText(SendPhoneOtp.this,"work : "+stringBuilder.toString(),Toast.LENGTH_LONG).show();
        }catch (IOException e){}

    }
}
