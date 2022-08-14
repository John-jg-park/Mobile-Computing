package com.example.smarthomegesturecontrol;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.smarthomegesturecontrol.R;


public class MainActivity extends AppCompatActivity {

    private Spinner spinner;
    public static String gestureName;
    public static int practice_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinner = (Spinner) findViewById(R.id.spinner);
        //tv_result = (TextView) findViewById(R.id.tv_result);
        Button next_btn = (Button) findViewById(R.id.nextPagebutton);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //tv_result.setText(adapterView.getItemAtPosition(i).toString());
                gestureName = adapterView.getItemAtPosition(i).toString(); // store "selected gesture" into global variable

                next_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent next = new Intent(MainActivity.this, MainActivity2.class);
                        startActivity(next);
                    }
                });


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }
}