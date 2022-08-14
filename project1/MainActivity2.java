package com.example.smarthomegesturecontrol;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.smarthomegesturecontrol.MainActivity3;
import com.example.smarthomegesturecontrol.R;

public class MainActivity2 extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        //Text
        TextView tv_result = (TextView) findViewById(R.id.tv_result);
        tv_result.setText(MainActivity.gestureName); //print gestureName on MainActivity2 screen
        //Button
        Button practice_btn = (Button) findViewById(R.id.nextPagebutton2);
        practice_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent practice = new Intent(MainActivity2.this, MainActivity3.class);
                startActivity(practice);
            }
        });
        //video
        //System.out.println(MainActivity.gestureName);

        VideoView videoView = findViewById(R.id.videoView);

        if (MainActivity.gestureName.equals("Number_0")){
            videoView.setVideoPath("android.resource://"+getPackageName()+"/"+R.raw.h_0);
        } else if (MainActivity.gestureName.equals("Number_1")){
            videoView.setVideoPath("android.resource://"+getPackageName()+"/"+R.raw.h_1);
        } else if (MainActivity.gestureName.equals("Number_2")){
            videoView.setVideoPath("android.resource://"+getPackageName()+"/"+R.raw.h_2);
        } else if (MainActivity.gestureName.equals("Number_3")){
            videoView.setVideoPath("android.resource://"+getPackageName()+"/"+R.raw.h_3);
        } else if (MainActivity.gestureName.equals("Number_4")){
            videoView.setVideoPath("android.resource://"+getPackageName()+"/"+R.raw.h_4);
        } else if (MainActivity.gestureName.equals("Number_5")){
            videoView.setVideoPath("android.resource://"+getPackageName()+"/"+R.raw.h_5);
        } else if (MainActivity.gestureName.equals("Number_6")){
            videoView.setVideoPath("android.resource://"+getPackageName()+"/"+R.raw.h_6);
        } else if (MainActivity.gestureName.equals("Number_7")){
            videoView.setVideoPath("android.resource://"+getPackageName()+"/"+R.raw.h_7);
        } else if (MainActivity.gestureName.equals("Number_8")){
            videoView.setVideoPath("android.resource://"+getPackageName()+"/"+R.raw.h_8);
        } else if (MainActivity.gestureName.equals("Number_9")){
            videoView.setVideoPath("android.resource://"+getPackageName()+"/"+R.raw.h_9);
        } else if (MainActivity.gestureName.equals("TurnOnLight")){
            videoView.setVideoPath("android.resource://"+getPackageName()+"/"+R.raw.h_lighton);
        } else if (MainActivity.gestureName.equals("TurnOffLight")){
            videoView.setVideoPath("android.resource://"+getPackageName()+"/"+R.raw.h_lightoff);
        } else if (MainActivity.gestureName.equals("TurnOnFan")){
            videoView.setVideoPath("android.resource://"+getPackageName()+"/"+ R.raw.h_fanon);
        } else if (MainActivity.gestureName.equals("TurnOffFan")){
            videoView.setVideoPath("android.resource://"+getPackageName()+"/"+R.raw.h_fanoff);
        } else if (MainActivity.gestureName.equals("IncreaseFanSpeed")){
            videoView.setVideoPath("android.resource://"+getPackageName()+"/"+R.raw.h_increasefanspeed);
        } else if (MainActivity.gestureName.equals("DecreaseFanSpeed")){
            videoView.setVideoPath("android.resource://"+getPackageName()+"/"+R.raw.h_decreasefanspeed);
        } else {
            videoView.setVideoPath("android.resource://"+getPackageName()+"/"+R.raw.h_setthermo);
        }

        videoView.start();

        Button replay_btn = (Button) findViewById(R.id.replay_button);
        replay_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                videoView.start();
            }
        });

        /* replay infinite
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });
        */

    }
}