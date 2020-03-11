package com.example.map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
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
    private String URL = "https://fcm.googleapis.com/fcm/send";
    private Spinner mspinnerBus;
    private String url ="https://auggbus.herokuapp.com/";
    private ArrayList<String> busNumber = new ArrayList<String>();
    public String[] countryNames = {"+ 91","+ 92"};
    Loading loading = new Loading(DriverDashBoard.this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_dash_board);

        mspinnerBus = findViewById(R.id.spinnerBus);
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
        /*===========================Request for no of busses=============================================*/
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

                        for(int i = 0; i<data.length();i++){
                            busNumber.add("Bus "+Integer.toString(i));
                        }
                        System.out.println(busNumber);

                    }catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                updateSpinner();


            }
        });
        /*================================================================================================*/

        mRequestQue = Volley.newRequestQueue(this);
        FirebaseMessaging.getInstance().subscribeToTopic("news");


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
                            Toast.makeText(DriverDashBoard.this,"message send"+response,Toast.LENGTH_LONG).show();

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("MUR", "onError: "+error.networkResponse);
                    Toast.makeText(DriverDashBoard.this,"error",Toast.LENGTH_LONG).show();

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
    public void updateSpinner(){

        System.out.println(busNumber);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mspinnerBus.setAdapter(new ArrayAdapter<String>(DriverDashBoard.this, android.R.layout.simple_spinner_dropdown_item,busNumber));
            }
        });
        loading.dismissDialog();

    }



}

