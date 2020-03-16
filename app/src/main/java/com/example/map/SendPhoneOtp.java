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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.map.CountryData.countryCode;
import static com.example.map.CountryData.countryaNames;


public class SendPhoneOtp extends AppCompatActivity {

    private Spinner spinnerCountryCode;
    private EditText editText;
    private TextView wrongNoText;
    private String url = "https://auggbus.herokuapp.com/login/";
    String userStatus;
    String code;
    String mobileNumber;
    SharedPreferences sharedpreferences;

    ReadStorageData data = new ReadStorageData();//reading user data

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        ErrorMessage errorMessage = new ErrorMessage(this);
        errorMessage.checkInternet();



        wrongNoText = findViewById(R.id.wrongnumber);
        wrongNoText.setVisibility(View.GONE);

//loading intalization
        final Loading loading = new Loading(SendPhoneOtp.this);

//=========================spinner + 91
        spinnerCountryCode = findViewById(R.id.spinnerCountries);
        spinnerCountryCode.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,countryaNames));

        editText = findViewById(R.id.phoneOtp);
        findViewById(R.id.continueButton).setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {

                code = countryCode[spinnerCountryCode.getSelectedItemPosition()];
                mobileNumber = editText.getText().toString().trim();// get number from text view

                 if(mobileNumber.isEmpty()||mobileNumber.length()<10){

                     editText.setError("Valid Number is required");
                     editText.requestFocus();
                     return;
                 }
                 else {

                     loading.startLoading();//start loading

                     OkHttpClient client = new OkHttpClient();
//post methode request
                     MediaType MEDIA_TYPE = MediaType.parse("application/json");
                     JSONObject postdata = new JSONObject();
                     try {
                         postdata.put("mobile_number", mobileNumber);

                     } catch (JSONException e) {
                         e.printStackTrace();
                     }

                     RequestBody body = RequestBody.create(MEDIA_TYPE, postdata.toString());
                     final Request request = new Request.Builder()
                             .url(url)
                             .post(body)
                             .addHeader("Content-Type", "application/json")
                             .build();

// callback for the http request

                     client.newCall(request).enqueue(new Callback() {
                         @Override
                         public void onFailure(Call call, IOException e) {
                             e.printStackTrace();

                         }
                         @Override
                         public void onResponse(Call call, Response response) throws IOException {




                             if (response.isSuccessful()) {

                                 String myResponse = response.body().string();
                                 try {
                                     JSONObject jsonObject = new JSONObject(myResponse);
                                     JSONObject data = jsonObject.getJSONObject("data");

                                     //checking user is driver or guardian


                                     if(data.getString("is_driver")=="true"){

                                         userStatus="driver";

                                     }else if(data.getString("is_guardian")=="true"){

                                         userStatus="guardian";

                                     }


                                     File path = Environment.getExternalStorageDirectory();
                                     String filename = "profile.txt";
                                     // creating file
                                     File file = new File(path, filename);
                                     FileWriter fileWriter = new FileWriter(file.getAbsoluteFile());
                                     BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                                     bufferedWriter.write(data.toString());//writing data
                                     bufferedWriter.close();


                                     // editText.setText(status.toString());
                                     String phoneNumberWithCOde = "+" + code + mobileNumber;

                                     Intent intent = new Intent(SendPhoneOtp.this, VerifyPhoneOtp.class);

                                     intent.putExtra("mobileNumber", phoneNumberWithCOde);
                                     intent.putExtra("userStatus", userStatus);

                                     loading.dismissDialog();//stop loading
                                     startActivity(intent);

                                 } catch (JSONException e) {
                                     e.printStackTrace();
                                 }


                                 // Toast.makeText(SendPhoneOtp.this,"work"+phoneNumber,Toast.LENGTH_LONG).show();
                                 //Intent intent = new Intent(SendPhoneOtp.this,VerifyPhoneOtp.class);
                                 // intent.putExtra("phonenumber",phoneNumber);
                                 // startActivity(intent);

                                 // Response Failure
                             } else {

                                 SendPhoneOtp.this.runOnUiThread(new Runnable() {
                                     @Override
                                     public void run() {

                                         loading.dismissDialog();//stop loading
                                         Toast.makeText(SendPhoneOtp.this, "Phone Number not Regestered", Toast.LENGTH_LONG).show();

                                         wrongNoText.setText("Invalid Mobile number");
                                         wrongNoText.setVisibility(View.VISIBLE);

                                     }
                                 });

                             }

                         }

                     });

                 }
            }
        });


    }

    @Override
    protected void onStart() {

        super.onStart();

        if(FirebaseAuth.getInstance().getCurrentUser() !=null){

                data.read();

                if (data.guardian.equals("true")) {

                    String busNo = data.busID; //getting bus no of guardian
                    FirebaseMessaging.getInstance().subscribeToTopic("Bus" + busNo);

                    Intent intent = new Intent(this, ParentDashBoard.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                } else if (data.driver.equals("true")) {


                    Intent intent = new Intent(this, DriverDashBoard.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
                // Toast.makeText(SendPhoneOtp.this,"work : "+stringBuilder.toString(),Toast.LENGTH_LONG).show();


        }
    }
}
