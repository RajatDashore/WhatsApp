package com.example.whatsappclone;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();

        ImageView view = findViewById(R.id.WhatsLogo);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashActivity.this, SignUpActivity.class);
//                Animation anim = AnimationUtils.loadAnimation(SplashActivity.this, android.R.anim.fade_out);
//                anim.setDuration(2000);
//                view.startAnimation(anim);
                startActivity(i);
                //  anim.start();
                finish();
            }
        }, 1000);
    }
}