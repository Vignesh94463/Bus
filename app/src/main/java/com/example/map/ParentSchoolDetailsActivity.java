package com.example.map;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ParentSchoolDetailsActivity extends AppCompatActivity {
    ImageView backButton;
    ImageView SchoolNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_details);
        backButton=(ImageView)findViewById(R.id.backButton);

        SchoolNumber=findViewById(R.id.phone);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        SchoolNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  Toast.makeText(ParentSchoolDetailsActivity.this,"work",Toast.LENGTH_LONG).show();
                Intent intent=new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:+918065341211"));
                startActivity(intent);
            }
        });
    }
}
