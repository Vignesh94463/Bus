package com.example.map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;

public class DriverDashBoard extends AppCompatActivity {
    private RequestQueue mRequestQue;
    CardView schoolDetails,profile;
    Dialog dialog;
    Button btnAccept,startRide;
    ImageView closePopup;
    private String URL = "https://fcm.googleapis.com/fcm/send";
    private Spinner mspinnerBus;
    private String url ="https://auggbus.herokuapp.com/";
    private ArrayList<String> busNumber = new ArrayList<String>();
    public String[] countryNames = {"+ 91","+ 92"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_dash_board);


        dialog=new Dialog(this);
        dialog.setContentView(R.layout.custom_popup);
        closePopup=(ImageView)dialog.findViewById(R.id.closePopup);
        btnAccept = (Button)dialog.findViewById(R.id.btnAccept);
        mspinnerBus=(Spinner)dialog.findViewById(R.id.spinnerBus);


        schoolDetails=(CardView)findViewById(R.id.schooldetails);
        profile=(CardView)findViewById(R.id.profile);

        schoolDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(DriverDashBoard.this,SchoolDetailsActivity.class);
                startActivity(intent);
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DriverDashBoard.this,ProfileActivity.class);
                startActivity(intent);
            }
        });


        findViewById(R.id.startRide).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getBusNumber();// addes the number os bus in  busNumber array
//                notification();


                closePopup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                btnAccept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent=new Intent(DriverDashBoard.this,MapsActivityDriver.class);
                        startActivity(intent);

                        sendNotification();//push notification

                    }
                });
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            }
        });


        findViewById(R.id.logoutDriver).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent=new Intent(DriverDashBoard.this, SendPhoneOtp.class);
                startActivity(intent);
//                notification();

            }
        });
    }


    public void getBusNumber(){

        final Loading loading = new Loading(DriverDashBoard.this);

        loading.startLoading();

        OkHttpClient client = new OkHttpClient();
        okhttp3.Request request = new okhttp3.Request.Builder().url(url+"bus_details").build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {


                if(response.isSuccessful())
                {
                    String myResponse = response.body().string();
                    try {
                        JSONObject jsonObject = new JSONObject(myResponse);
                        JSONArray data = jsonObject.getJSONArray("data");

                        for(int i = 1; i<=data.length();i++){
                            busNumber.add("Bus"+Integer.toString(i));

                        }
                        System.out.println("sasi1" + busNumber);
                        loading.dismissDialog();
                        updateSpinner();


                    runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog.show();
                    }
                });



                    }catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                updateSpinner();


            }

            public void updateSpinner(){

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mspinnerBus.setAdapter(new ArrayAdapter<String>(DriverDashBoard.this, android.R.layout.simple_spinner_dropdown_item,busNumber));
                    }
                });
            }
        });
    }

    public void sendNotification(){

        String currentBus = mspinnerBus.getSelectedItem().toString();


        mRequestQue = Volley.newRequestQueue(this);
        FirebaseMessaging.getInstance().subscribeToTopic(currentBus);


        JSONObject jason = new JSONObject();

        try {
            jason.put("to", "/topics/" + currentBus);
            JSONObject notificationObj = new JSONObject();
            notificationObj.put("title","Aug Bus");
            notificationObj.put("body",currentBus+"Started trip from  school");

            jason.put("notification",notificationObj);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL, jason,new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
//                            Toast.makeText(DriverDashBoard.this,"message send"+response,Toast.LENGTH_LONG).show();

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("MUR", "onError: "+error.networkResponse);
//                    Toast.makeText(DriverDashBoard.this,"error",Toast.LENGTH_LONG).show();

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
        Toast toast = Toast.makeText(DriverDashBoard.this,"pappus",Toast.LENGTH_LONG);
        toast.show();


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





}

