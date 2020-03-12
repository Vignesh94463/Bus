package com.example.map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.List;


public class MapsActivityDriver extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    LocationRequest mLocationRequest;
    //Button logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_maps_driver);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
//        logoutButton=(Button)findViewById(R.id.logout);
//        logoutButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast toast = Toast.makeText(MapsActivityDriver.this,"sasi",Toast.LENGTH_LONG);
//                FirebaseAuth.getInstance().signOut();
//                Intent intent=new Intent(MapsActivityDriver.this, SendPhoneOtp.class);
//                startActivity(intent);
//            }
//        });
//        findViewById(R.id.stopTripButton).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onStopButton();
//                Intent DriverIntent = new Intent(MapsActivityDriver.this,DriverDashBoard.class);
//                startActivity(DriverIntent);
//
//            }
//        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }}
        buildGoogleApiClient();
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                if (mGoogleApiClient != null) {


//                    Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
//                    longitude = location.getLongitude();
//                    latitude = location.getLatitude();
//                    LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
                    Toast toast = Toast.makeText(MapsActivityDriver.this,"sasi",Toast.LENGTH_LONG);
                    toast.show();
                }
                return false;
            }
        });

    }
    protected synchronized void buildGoogleApiClient()
    {
        mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {


//        int bearing = (int) mMap.getMyLocation().getBearing();
        mLastLocation = location;
        LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
//        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
//        mMap.animateCamera(CameraUpdateFactory.zoomTo(13));
//        mMap.animateCamera(CameraUpdateFactory.zoomIn());
        CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(13f).bearing(0)/*.tilt(60)*/.build();
        CameraUpdate cu = CameraUpdateFactory.newCameraPosition(cameraPosition);
//        mMap.animateCamera(cu);


        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Bus1");


        ref.child("status").setValue("running");

        GeoFire geoFire = new GeoFire(ref);
        geoFire.setLocation("Location", new GeoLocation(location.getLatitude(),location.getLongitude()));

    }
//    @Override
//    public void onStatusChanged(String s, int i, Bundle bundle) {
//
//    }
//
//    @Override
//    public void onProviderEnabled(String s) {
//
//    }
//
//    @Override
//    public void onProviderDisabled(String s) {
//
//    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(2000);
        mLocationRequest.setFastestInterval(2000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);//high accurate location drains battery more

        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        }

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,mLocationRequest, this);

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    @Override
    protected void onStop()
    {
        super.onStop();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Bus1");
//        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);

        GeoFire geoFire = new GeoFire(ref);
        geoFire.removeLocation("Location");

    }
    public void onStopButton(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Bus1");
        ref.child("status").setValue("stopped");
    LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);


    }


}