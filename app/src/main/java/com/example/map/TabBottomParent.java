package com.example.map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.map.Fragments.HomeFragment;
import com.example.map.Fragments.Notification;
import com.google.android.material.tabs.TabLayout;

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
    CardView profile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_bottom_parent);
      profile = (CardView)findViewById(R.id.profile);
      profile.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent intent=new Intent(TabBottomParent.this,ProfileActivity.class);
              startActivity(intent);
          }
      });
   }
}
