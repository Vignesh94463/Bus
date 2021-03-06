package com.example.map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

//import com.example.map.Fragments.Notification;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DriverHome extends AppCompatActivity {

    private RequestQueue mRequestQue;
    private String URL = "https://fcm.googleapis.com/fcm/send";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRequestQue = Volley.newRequestQueue(this);
        FirebaseMessaging.getInstance().subscribeToTopic("news");


        setContentView(R.layout.activity_driver_home);

        findViewById(R.id.startRide).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                notification();
                sendNotification();


//                Intent homeIntent = new Intent(DriverHome.this, MapsActivityDriver.class);
//                startActivity(homeIntent);



            }
        });

        findViewById(R.id.logoutDriver).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
//                Intent intent=new Intent(DriverHome.this, SendPhoneOtp.class);
//                startActivity(intent);
//                notification();

            }
        });
    }
//    public void notification(){
//
//
//
//        final String CHANNEL_1_ID="channel1";
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
//        {
//            NotificationChannel channel1 =  new NotificationChannel(CHANNEL_1_ID,"channel",NotificationManager.IMPORTANCE_HIGH);
//            channel1.setDescription("this is channel");
//
//            NotificationManager manager = getSystemService(NotificationManager.class);
//            manager.createNotificationChannel(channel1);
//
//        }
//
//        NotificationManagerCompat notficationManager = NotificationManagerCompat.from(this);
//
//        Notification notification = new NotificationCompat.Builder(this,CHANNEL_1_ID).setSmallIcon(R.drawable.bus2).setContentTitle("AUgBus").setContentText("bus").setPriority(NotificationCompat.PRIORITY_HIGH)
//                .setCategory(NotificationCompat.CATEGORY_MESSAGE).build();
//        notficationManager.notify(1,notification);
//
//
//        Toast toast = Toast.makeText(DriverHome.this,"pappus",Toast.LENGTH_LONG);
//        toast.show();
//    }
    public void sendNotification(){

        JSONObject jason = new JSONObject();
        try {
            jason.put("to", "/topics/" + "news");
            JSONObject notificationObj = new JSONObject();
            notificationObj.put("title","hhhh");
            notificationObj.put("body","anybody");

            JSONObject extraData = new JSONObject();
            extraData.put("brandId","puma");
            extraData.put("category","Shoes");

            jason.put("notification",notificationObj);
            jason.put("data",extraData);


            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL, jason,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Toast.makeText(DriverHome.this,"message send"+response,Toast.LENGTH_LONG).show();

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("MUR", "onError: "+error.networkResponse);
                    Toast.makeText(DriverHome.this,"error",Toast.LENGTH_LONG).show();

                }
            }
            ){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
//                    return super.getHeaders();
                    Map<String,String> header = new HashMap<>();
                    header.put("content-type","application/json");
                    header.put("authorization","key=AIzaSyBAH7N6bkdVT3Xs6WPjhXkxV7j3dUCS-w8");
                    return header;

                }
            };

            mRequestQue.add(request);

        }catch (JSONException e){
            e.printStackTrace();
        }
        Toast toast = Toast.makeText(DriverHome.this,"pappus",Toast.LENGTH_LONG);
        toast.show();


    }

    }
