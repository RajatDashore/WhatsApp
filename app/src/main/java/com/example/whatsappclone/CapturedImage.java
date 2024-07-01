package com.example.whatsappclone;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class CapturedImage extends AppCompatActivity {

    private ImageView image;
    private Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_captured_image);
        getSupportActionBar().hide();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), this::onApplyWindowInsets);
        btnBack = findViewById(R.id.backbtn);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CapturedImage.this, ChatDetailActivity.class));
                Animation anim = AnimationUtils.loadAnimation(CapturedImage.this, android.R.anim.fade_in);
                anim.setDuration(1000);
                finish();
                anim.start();
                anim.cancel();
            }
        });
    }

    private WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets) {
        Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
        v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);

        image = findViewById(R.id.CapturedImage);
        Intent i = getIntent();
        Bitmap map = i.getParcelableExtra("bitMap");
        image.setImageBitmap(map);
        return insets;

    }
}