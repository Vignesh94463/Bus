package com.example.map;

import android.os.Environment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class ReadStorageData {

    public String name;
    public String mobileNo;
    public String driver;
    public String guardian;
    public String busID;



    public void read(){
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

                name = jsonObject.getString("name");
                mobileNo = jsonObject.getString("phone");
                driver=jsonObject.getString("is_driver");
                guardian=jsonObject.getString("is_guardian");

                if(guardian.equals("true")){
                    busID=jsonObject.getString("bus_id");
                }


            } catch (JSONException e) {
                e.printStackTrace();

            }
            // Toast.makeText(SendPhoneOtp.this,"work : "+stringBuilder.toString(),Toast.LENGTH_LONG).show();
        }catch (IOException e){}

    }
}
