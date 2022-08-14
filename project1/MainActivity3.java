package com.example.smarthomegesturecontrol;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.smarthomegesturecontrol.R;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity3 extends AppCompatActivity {


    private static final int PERMISSION_REQUEST_CODE = 1;
    //private static int VIDEO_RECORD_CODE = 101;
    private static int CAMERA_PERMISSION_CODE = 100;
    private String filePath;

    //private Uri videoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        //check if the camera is available
        if (isCameraPresentInPhone()) {
            Log.i("VIDEO_RECORD_TAG", "Camera is detected");
            getCameraPermission();
        } else {
            Log.i("VIDEO_RECORD_TAG", "Camera is NOT detected");
        }

        //start the record
        Button record_btn = (Button) findViewById(R.id.record_button);
        record_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent camera_intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                camera_intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 5);
                //startActivityForResult(camera_intent, VIDEO_RECORD_CODE);
                resultActivity.launch(camera_intent);
            }
        });
        //upload the video to flask local server
        Button upload_file = (Button) findViewById(R.id.upload_button);
        upload_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (filePath != null){
                    if(Build.VERSION.SDK_INT >= 23){
                        if(checkPermission()) {
                            //ActivityCompat.requestPermissions(MainActivity3.this, new String[]{Manifest.permission.INTERNET}, 2);
                            uploadfile();
                        }else{
                            requestPermission();
                        }
                    }

                }
            }
        });

    }
    private void requestPermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity3.this, Manifest.permission.READ_EXTERNAL_STORAGE)){
            Toast.makeText(MainActivity3.this, "Please give permission to Upload file", Toast.LENGTH_SHORT).show();
        }
        else {
            ActivityCompat.requestPermissions(MainActivity3.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }
    private boolean checkPermission(){
        int result = ContextCompat.checkSelfPermission(MainActivity3.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED){
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Access to Storage Permission Granted. Thanks.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Access to Storage Permission Denied.", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            case 2: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Access to Internet Permission Granted. Thanks.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Access to Internet Permission Denied.", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }
    private boolean isCameraPresentInPhone(){
        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)){
            return true;
        }
        else{
            return false;
        }
    }
    private void getCameraPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        }

    }
    // Create lanucher variable inside onAttach or onCreate or global
    ActivityResultLauncher<Intent> resultActivity = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                Intent data = result.getData();
                // get real file path
                Uri videoUri = data.getData();
                filePath = getRealPathFromUri (data.getData(), MainActivity3.this);
                System.out.println(videoUri);
                System.out.println(filePath);
                Log.i("VIDEO_RECORD_TAG", "Video has been recorded and saved at path "+ filePath);
                Toast.makeText(getApplicationContext(), "Video has been recorded and saved at path "+ filePath, Toast.LENGTH_LONG).show();
                //video play
                VideoView videoView = findViewById(R.id.videoView2);
                videoView.setVideoPath(filePath);
                videoView.start();

            }
        }
    });

    private String getRealPathFromUri(Uri uri, MainActivity3 mainActivity3) {
        Cursor cursor= mainActivity3.getContentResolver().query(uri, null, null, null,null);
        if(cursor==null){
            return uri.getPath();
        }
        else{
            cursor.moveToFirst();
            int id = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(id);
        }
    }
    private void uploadfile() {

        OkHttpClient client = new OkHttpClient();

        File file = new File(filePath);
        System.out.println(filePath);
        System.out.println(file.getName());
        MainActivity.practice_number += 1;
        String file_name =  MainActivity.gestureName + "_PRACTICE_" +  Integer.toString(MainActivity.practice_number) +".mp4";
        System.out.println(file_name);

        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(),RequestBody.create(file,MediaType.parse("video/mp4")))
                .addFormDataPart("filename", file_name)
                .build();

        Request request = new Request.Builder()
                .url("http://10.0.0.93:5000/mp4")
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d("MY", "failure", e);
                System.out.println("error Cannot connect to Flask server");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Log.d("MY", "sucessful!!");
                System.out.println("connected and posted to Flask server");

                String get_from_flask = response.body().string();

                MainActivity3.this.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(MainActivity3.this, get_from_flask, Toast.LENGTH_LONG).show();
                        //TextView textView = findViewById(R.id.textview);
                        //textView.setText(get_from_flask);
                    }
                });
                Intent next = new Intent(MainActivity3.this, MainActivity.class);
                startActivity(next);

            }
        });
    }





}




