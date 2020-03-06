package com.example.map;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

import com.example.map.R;

public class Loading {

    Activity activity;
    AlertDialog dialog;

    Loading(Activity myActivity)
    {
        activity=myActivity;
    }
    void startLoading()
    {
         AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.custom_dialouge,null));
        builder.setCancelable(true);

        dialog = builder.create();
        dialog.show();
    }
    void dismissDialog(){
        dialog.dismiss();
    }

}
