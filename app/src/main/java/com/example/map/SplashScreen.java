package com.example.map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

public class SplashScreen extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //check background notification

//        if(getIntent().hasExtra("status"))
//
//        {
//            System.out.println("xxxxxx"+getIntent().getExtras());
//
//            Intent intent = new Intent(this, MapsActivityParent.class);
//            startActivity(intent);
//
//        }
//        if (getIntent().getExtras() != null) {
////            for (String key : getIntent().getExtras().keySet()) {
////                String value = getIntent().getExtras().getString(key);
////                System.out.println("pappu00"+ "Key: " + key + " Value: " + value);
////            }
//        }


            requestPermission(); // request permission function

    }



    private void requestPermission() {
        final String[] PERMISSIONS = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.INTERNET


        };
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.CAMERA)){
            new AlertDialog.Builder(this).setTitle("Permission needed").setMessage("This permission is needed because of this and that").setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(SplashScreen.this,
                                    PERMISSIONS, 1);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();
        }
        else{
//            Toast.makeText(this, "Permission GRANTED", Toast.LENGTH_SHORT).show();

            ActivityCompat.requestPermissions(SplashScreen.this,PERMISSIONS,1);
        }



    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == 1)  {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                Toast.makeText(this, "Permission GRANTED", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Intent homeIntent = new Intent(SplashScreen.this,SendPhoneOtp.class);
                        startActivity(homeIntent);
                        finish();

                    }
                },SPLASH_TIME_OUT);
            } else {
                Toast.makeText(this, "Permission Denied Application wont work", Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // kill app
                        android.os.Process.killProcess(android.os.Process.myPid());
                        System.exit(1);

                    }
                },4000);

            }
        }
    }



    }

