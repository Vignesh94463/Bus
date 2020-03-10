package com.example.map;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.example.map.CountryData.countryCode;
import static com.example.map.CountryData.countryaNames;

public class SendPhoneOtp extends AppCompatActivity {

    private Spinner spinner;
    private EditText editText;
    private String url = "https://auggbus.herokuapp.com/login/";
    String userstatus;
    String code;
    String number;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final Loading loading = new Loading(SendPhoneOtp.this);

        spinner = findViewById(R.id.spinnerCountries);
        spinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,countryaNames));

        editText = findViewById(R.id.phoneOtp);
        findViewById(R.id.continueButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                 code = countryCode[spinner.getSelectedItemPosition()];



                 number = editText.getText().toString().trim();
                 if(number.isEmpty()||number.length()<10){
                     editText.setError("Valid Number is required");
                     editText.requestFocus();
                     return;
                 }

                 else{
                     loading.startLoading();
                 }

                 OkHttpClient client = new OkHttpClient();
                 Request request = new Request.Builder().url(url+number).build();

                 client.newCall(request).enqueue(new Callback() {
                     @Override
                     public void onFailure(Call call, IOException e) {
                         e.printStackTrace();


                     }

                     @Override
                     public void onResponse(Call call, Response response) throws IOException {

                         if( response.isSuccessful()){
                             String myResponse = response.body().string();
                             try {
                                 JSONObject jsonObject=new JSONObject(myResponse);
                                 JSONObject data = jsonObject.getJSONObject("data");

                                 userstatus = data.getString("is_driver");
                                 File path = Environment.getExternalStorageDirectory();
                                 String filename = "Augbusfile.txt";
                                 File file = new File(path,filename);
                                 FileWriter fileWriter = new FileWriter(file.getAbsoluteFile());
                                 BufferedWriter bufferedWriter=new BufferedWriter(fileWriter);
                                 bufferedWriter.write(userstatus);
                                 bufferedWriter.close();
                                // editText.setText(status.toString());
                                 String phoneNumber ="+"+code+number;
                                 Intent intent = new Intent(SendPhoneOtp.this,VerifyPhoneOtp.class);
                                 intent.putExtra("phonenumber",phoneNumber);
                                 intent.putExtra("userstatus",userstatus);

                                 loading.dismissDialog();//stop loading
                                 startActivity(intent);

                             } catch (JSONException e) {
                                 e.printStackTrace();
                             }



                            // Toast.makeText(SendPhoneOtp.this,"work"+phoneNumber,Toast.LENGTH_LONG).show();
                 //Intent intent = new Intent(SendPhoneOtp.this,VerifyPhoneOtp.class);
                // intent.putExtra("phonenumber",phoneNumber);
                // startActivity(intent);


                         }else{
                             SendPhoneOtp.this.runOnUiThread(new Runnable() {
                                 @Override
                                 public void run() {
                                     loading.dismissDialog();//stop loading
//                                     editText.setText(response.body().string());
                                     Toast.makeText(SendPhoneOtp.this,"Phone Number not Regestered",Toast.LENGTH_LONG).show();
                                 }
                             });

                         }

                     }

                 });



                 String phoneNumber ="+"+code+number;
//                 Intent intent = new Intent(SendPhoneOtp.this,VerifyPhoneOtp.class);
//                 intent.putExtra("phonenumber",phoneNumber);
//                 startActivity(intent);
            }
        });



        getSupportActionBar().hide();

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser() !=null){
            StringBuilder stringBuilder = new StringBuilder();
            try {
               File textFile = new File(Environment.getExternalStorageDirectory(),"Augbusfile.txt");
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
               String p=stringBuilder.toString();
               String s = "false";
                if (p.equals(s)) {
                    Intent intent = new Intent(this,TabBottomParent.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                    startActivity(intent);
                }
                else {
                    Intent intent = new Intent(this,MapsActivityDriver.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                    startActivity(intent);
                }
               // Toast.makeText(SendPhoneOtp.this,"work : "+stringBuilder.toString(),Toast.LENGTH_LONG).show();
            }catch (IOException e){}





        }
    }
}
