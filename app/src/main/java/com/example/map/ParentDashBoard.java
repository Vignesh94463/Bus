package com.example.map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class ParentDashBoard extends AppCompatActivity {
    CardView trackBus,profile,driverDetails,schoolDetails,logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_bottom_parent);


        final ErrorMessage errorMessage = new ErrorMessage(this);
        errorMessage.checkInternet();

      profile = (CardView)findViewById(R.id.profile);
      logout=(CardView)findViewById(R.id.logout);
      driverDetails=(CardView)findViewById(R.id.driverdetails);
      schoolDetails=(CardView)findViewById(R.id.schooldetails);
      trackBus=(CardView)findViewById(R.id.trackbus);

      trackBus.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              if(errorMessage.isNetworkAvailable(ParentDashBoard.this)==true) {
                  Intent intent = new Intent(ParentDashBoard.this, MapsActivityParent.class);
                  startActivity(intent);
              }else{
                  errorMessage.checkInternet();}
          }
      });
      driverDetails.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              if(errorMessage.isNetworkAvailable(ParentDashBoard.this)==true) {

                  Intent intent = new Intent(ParentDashBoard.this, ParentBusDriverDetails.class);
                  startActivity(intent);
              }else{
                  errorMessage.checkInternet();}
          }
      });
        schoolDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(ParentDashBoard.this, ParentSchoolDetailsActivity.class);
                startActivity(intent);
            }
        });
      profile.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent intent=new Intent(ParentDashBoard.this, ParentProfileActivity.class);
              startActivity(intent);
          }
      });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent=new Intent(ParentDashBoard.this,SendPhoneOtp.class);
                startActivity(intent);
                finish();
            }
        });

   }
}
