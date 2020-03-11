package com.example.map.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.map.SendPhoneOtp;
import com.example.map.R;
import com.google.firebase.auth.FirebaseAuth;


public class Notification extends Fragment {

    Button button;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_notification,container,false);

        return v;


    }

    // TODO: Rename method, update argument and hook method into UI event

}
