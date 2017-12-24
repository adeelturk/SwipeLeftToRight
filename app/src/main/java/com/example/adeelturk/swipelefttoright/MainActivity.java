package com.example.adeelturk.swipelefttoright;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    private RelativeLayout containerWhichWillDetectGesture;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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


    }


}
