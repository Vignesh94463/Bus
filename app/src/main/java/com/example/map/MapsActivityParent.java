package com.example.map;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.FragmentActivity;
//import fragment from android.support.v4.app.Fragment;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class MapsActivityParent extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ImageView gpsButton;
    boolean track = true;
    double LocationLat = 0;
    double LocationLan = 0;
    ImageView backButton;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_parent);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        gpsButton = findViewById(R.id.locIcon);
        backButton=(ImageView)findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapsActivityParent.this,TabBottomParent.class);
                startActivity(intent);
            }
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        getDriverLocation();
        getDriverStatus();
        mMap.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
            @Override
            public void onCameraMoveStarted(int i) {


                if (i == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE) {
//                    Toast.makeText(MapsActivityParent.this, "The user gestured on the map.", Toast.LENGTH_SHORT).show();
                    track=false;
                } else if (i == GoogleMap.OnCameraMoveStartedListener.REASON_API_ANIMATION) {
//                    Toast.makeText(MapsActivityParent.this, "The user tapped something on the map.",
//                            Toast.LENGTH_SHORT).show();
                } else if (i == GoogleMap.OnCameraMoveStartedListener.REASON_DEVELOPER_ANIMATION) {
//                    Toast.makeText(MapsActivityParent.this, "The app moved the camera.",
//                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latLng) {

                track=false;
                Toast toast = Toast.makeText(MapsActivityParent.this,Boolean.toString(track),Toast.LENGTH_LONG);
                toast.show();
            }
        });

    }
    private Marker mDriverMarker;
    private void getDriverLocation(){


        DatabaseReference driverLocation = FirebaseDatabase.getInstance().getReference().child("Bus1").child("Location").child("l");

        driverLocation.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){
                    List<Object> map = (List<Object>) dataSnapshot.getValue();
                    if (map.get(0) != null){
                         LocationLat = Double.parseDouble(map.get(0).toString());
                    }
                    if (map.get(1) != null){
                        LocationLan = Double.parseDouble(map.get(1).toString());

                    }
                    LatLng driverLatLang = new LatLng(LocationLat,LocationLan);
                    if(mDriverMarker !=null){
                        mDriverMarker.remove();
                    }

                    mDriverMarker = mMap.addMarker(new MarkerOptions().position(driverLatLang).title("Bus 1").icon(BitmapDescriptorFactory.fromResource(R.drawable.busgps2)));
                    if (track == true) {
                        CameraPosition cameraPosition = new CameraPosition.Builder().target(driverLatLang).zoom(14f).bearing(45)/*.tilt(60)*/.build();
                        CameraUpdate cu = CameraUpdateFactory.newCameraPosition(cameraPosition);
                        mMap.animateCamera(cu);

//                        mMap.moveCamera(CameraUpdateFactory.newLatLng(driverLatLang));
//                        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
                    }

//                    Toast toast = Toast.makeText(MapsActivityParent.this,driverLatLang.toString(),Toast.LENGTH_LONG);
//                    toast.show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        gpsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                track=true;
            }
        });
    }

    private void getDriverStatus(){
        DatabaseReference driverService = FirebaseDatabase.getInstance().getReference().child("Bus1").child("status");
        driverService.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

//                Notification when the app is active(foreground?)

//                final String CHANNEL_1_ID="channel1";
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
//                {
//                    NotificationChannel channel1 =  new NotificationChannel(CHANNEL_1_ID,"channel", NotificationManager.IMPORTANCE_HIGH);
//                    channel1.setDescription("this is channel");
//
//                    NotificationManager manager = getSystemService(NotificationManager.class);
//                    manager.createNotificationChannel(channel1);
//
//                }
//
//                NotificationManagerCompat notficationManager = NotificationManagerCompat.from(MapsActivityParent.this);
//
//                Notification notification = new NotificationCompat.Builder(MapsActivityParent.this,CHANNEL_1_ID).setSmallIcon(R.drawable.bus2).setContentTitle("AUgBus").setContentText("bus").setPriority(NotificationCompat.PRIORITY_HIGH)
//                        .setCategory(NotificationCompat.CATEGORY_MESSAGE).build();
//                notficationManager.notify(1,notification);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


}


