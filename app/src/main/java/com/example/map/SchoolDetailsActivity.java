package com.example.map;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class SchoolDetailsActivity extends AppCompatActivity {
    ImageView backButton;
    RelativeLayout dialNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_details);
        backButton=(ImageView)findViewById(R.id.backButton);
        dialNumber=(RelativeLayout)findViewById(R.id.dialNumber);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SchoolDetailsActivity.this,TabBottomParent.class);
                startActivity(intent);
            }
        });
        dialNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  Toast.makeText(SchoolDetailsActivity.this,"work",Toast.LENGTH_LONG).show();
                Intent intent=new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:0123456789"));
                startActivity(intent);
            }
        });
    }
}
