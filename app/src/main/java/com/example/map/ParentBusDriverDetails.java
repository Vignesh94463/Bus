package com.example.map;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;

public class ParentBusDriverDetails extends AppCompatActivity {
    ImageView backButton;
    TextView name;
    TextView phone;

    String nameDriver;
    String phoneDriver;

    private String urlDriverDetails = "https://auggbus.herokuapp.com/driver_details/";

    ReadStorageData data = new ReadStorageData();//reading user data
    String busId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_details);

        data.read();
        busId=data.busID;

        getDriver();

        findViewById(R.id.phone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:+91"+phone.getText()));
                startActivity(intent);

            }
        });


        backButton=(ImageView)findViewById(R.id.backButton);
        name=findViewById(R.id.profileid);
        phone=findViewById(R.id.phoneid);


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    public void getDriver(){


        final Loading loading = new Loading(ParentBusDriverDetails.this);
        loading.startLoading();


        OkHttpClient client = new OkHttpClient();
        final okhttp3.Request request = new okhttp3.Request.Builder().url(urlDriverDetails+busId).build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("sasi0"+e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                System.out.println("sasi1"+response);
                if (response.isSuccessful()) {

                    String myResponse = response.body().string();
                    try {
                        JSONObject jsonObject = new JSONObject(myResponse);
                        JSONObject data = jsonObject.getJSONObject("data");

                        nameDriver=data.getString("name");
                        phoneDriver=data.getString("phone");
                        System.out.println("sasi1"+nameDriver);


                        //checking user is driver or guardian
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                name.setText(nameDriver);
                                phone.setText(phoneDriver);
                                loading.dismissDialog();

                            }
                        });
//                        phone.setText(data.getString("phone"));

//                     loading.dismissDialog();


                    } catch (JSONException e) {
                        System.out.println("sasi2"+e);

                        e.printStackTrace();
                    }
                }
            }
        });

    }

}
