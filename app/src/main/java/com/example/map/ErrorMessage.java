package com.example.map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.net.InetAddress;


public class ErrorMessage {


        Activity activity;
        AlertDialog dialog;
        Dialog dialogError;
        ImageView closeError;
        Button retryButton;
        Boolean errorStatus=false;

    ErrorMessage(Activity myActivity) {activity = myActivity;}



        void checkInternet() {
            System.out.println("sassi"+isNetworkAvailable(activity));

            if(isNetworkAvailable(activity)==false) {

                dialogError = new Dialog(activity);
                dialogError.setContentView(R.layout.error_popup);
                dialogError.setCancelable(false);

                retryButton = (Button) dialogError.findViewById(R.id.retryButton);
                retryButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        dialogError.dismiss();
                        Intent intent = new Intent(activity, activity.getClass());
                        activity.startActivity(intent);
                        activity.finish();

                    }

                });

                closeError = (ImageView) dialogError.findViewById(R.id.errorclose);
                closeError.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        dialogError.dismiss();
                        errorStatus=false;
                    }
                });

                dialogError.show();
                errorStatus=true;

            }else if (isNetworkAvailable(activity)==true) {

                if (errorStatus ==true) {
                    dialogError.dismiss();
                }
            }
        }
        void dismiss(){
            dialogError.dismiss();
            errorStatus=false;
        }

    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));

        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}




