package com.example.adeelturk.swipelefttoright;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private RelativeLayout containerWhichWillDetectGesture;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkAndRequestPermissions(this);
        containerWhichWillDetectGesture = (RelativeLayout) findViewById(R.id.containerWhichWillDetectGesture);


        containerWhichWillDetectGesture.setOnTouchListener(new OnSwipeTouchListener(MainActivity.this,
                findViewById(R.id.textViewToMove),
                findViewById(R.id.pointToBeNoted), new OnExitListener() {
            @Override
            public void onExit() {

                Toast.makeText(MainActivity.this," on Exit",Toast.LENGTH_SHORT).show();

            }


            @Override
            public void onCancel() {

                Toast.makeText(MainActivity.this," on cancel",Toast.LENGTH_SHORT).show();

            }
        }));

        findViewById(R.id.pointToBeNoted).setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {

                Intent i=new Intent(MainActivity.this,CameraActivity.class);
                startActivity(i);

            }
        });

    }



    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 101;

    public static boolean checkAndRequestPermissions(final Activity context) {
        int ExtstorePermission = ContextCompat.checkSelfPermission(context,
                Manifest.permission.READ_EXTERNAL_STORAGE);
        int cameraPermission = ContextCompat.checkSelfPermission(context,
                Manifest.permission.CAMERA);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (ExtstorePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded
                    .add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(context, listPermissionsNeeded
                            .toArray(new String[listPermissionsNeeded.size()]),
                    REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS:
                if (ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(),
                            "FlagUp Requires Access to Camara.", Toast.LENGTH_SHORT)
                            .show();
                    finish();
                } else if (ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(),
                            "FlagUp Requires Access to Your Storage.",
                            Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    /*doWork();*/
                }
                break;
        }
    }

}
