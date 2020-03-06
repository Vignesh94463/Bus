package com.example.map.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.map.MapsActivityParent;
import com.example.map.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;


public class HomeFragment extends Fragment implements OnMapReadyCallback {

    GoogleMap mMap;
    SupportMapFragment mapFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_home,container,false);
        mapFragment=(SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.map);

        if (mapFragment==null){
            FragmentManager fm=getFragmentManager();
            FragmentTransaction ft=fm.beginTransaction();
            mapFragment=SupportMapFragment.newInstance();
            ft.replace(R.id.map,mapFragment).commit();
        }
        mapFragment.getMapAsync(this);
        return v;

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.animateCamera(CameraUpdateFactory.zoomTo(17));
        getDriverLocation();
    }
    private Marker mDriverMarker;
    private void getDriverLocation() {
        DatabaseReference driverLocation = FirebaseDatabase.getInstance().getReference().child("Location").child("Bus1").child("l");
        driverLocation.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){
                    List<Object> map = (List<Object>) dataSnapshot.getValue();
                    double LocationLat = 0;
                    double LocationLan = 0;
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
                    mDriverMarker = mMap.addMarker(new MarkerOptions().position(driverLatLang).title("Bus 1").icon(BitmapDescriptorFactory.fromResource(R.drawable.bus2)));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(driverLatLang));


                    //Toast toast = Toast.makeText(MapsActivityParent.this,driverLatLang.toString(),Toast.LENGTH_LONG);
                   // toast.show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    // TODO: Rename method, update argument and hook method into UI event





}
