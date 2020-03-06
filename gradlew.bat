package com.example.camera_app;

import androidx.appcompat.app.AppCompatActivity;


import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.FrameLayout;

import java.io.File;

public class MainActivity extends AppCompatActivity {


    Camera camera;
    FrameLayout frameLayout;
    ShowCamera showCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        frameLayout = (FrameLayout) findViewById(R.id.frameLayout);


        camera = Camera.open();

        showCamera = new ShowCamera(this, camera);
        frameLayout.addView(showCamera);
    }
    Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] bytes, Camera camera) {
            File picture_file =getOutputMediaFile();


        }
    };
    private  File getOutputMediaFile()
    {
    String stat = Environment.getExternalStorageState();
    if (!stat.equals(Environment.MEDIA_MOUNTED))
    {
        return null;
    }
    else
    {
        File folder_gui = new File()
    }
    }

    public void captureImage(View v) {
        if(camera!=null)
        {
            camera.takePicture(null,null,mPictureCallback);
        }
    }
}

                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           