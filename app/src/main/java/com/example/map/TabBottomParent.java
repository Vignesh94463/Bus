package com.example.map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.map.Fragments.HomeFragment;
import com.example.map.Fragments.Notification;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;

public class TabBottomParent extends AppCompatActivity {
    CardView trackBus,profile,driverDetails,schoolDetails,logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_bottom_parent);
      profile = (CardView)findViewById(R.id.profile);
      logout=(CardView)findViewById(R.id.logout);
      driverDetails=(CardView)findViewById(R.id.driverdetails);
      schoolDetails=(CardView)findViewById(R.id.schooldetails);
      trackBus=(CardView)findViewById(R.id.trackbus);
      trackBus.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent intent =new Intent(TabBottomParent.this,MapsActivityParent.class);
              startActivity(intent);
          }
      });
      driverDetails.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent intent =new Intent(TabBottomParent.this,DriverDetailsActivity.class);
              startActivity(intent);
          }
      });
        schoolDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(TabBottomParent.this,SchoolDetailsActivity.class);
                startActivity(intent);
            }
        });
      profile.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent intent=new Intent(TabBottomParent.this,ProfileActivity.class);
              startActivity(intent);
          }
      });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent=new Intent(TabBottomParent.this,SendPhoneOtp.class);
                startActivity(intent);
            }
        });

   }
}
