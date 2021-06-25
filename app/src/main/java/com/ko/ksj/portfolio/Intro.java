package com.ko.ksj.portfolio;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class Intro extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        start();
    }
    void start(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Intro.this, Login.class);
                startActivity(intent);
                finish();
            }
        },5000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}