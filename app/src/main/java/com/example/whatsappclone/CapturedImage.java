package com.example.whatsappclone;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

public class CapturedImage extends AppCompatActivity {
    private TextView txtSend;
    private Uri captureImage;
    private FirebaseAuth auth;
    private String recieverUid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_captured_image);
        Objects.requireNonNull(getSupportActionBar()).hide();

        auth = FirebaseAuth.getInstance();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), this::onApplyWindowInsets);

        Button btnBack = findViewById(R.id.backbtn);
        recieverUid = getIntent().getStringExtra("uId"); // Correctly fetching the UID
        captureImage = getIntent().getParcelableExtra("uri"); // Fetching the bitmap

        if (captureImage == null) {
            Toast.makeText(this, "Image not found!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        init();
        setRecieverUid(recieverUid);

        btnBack.setOnClickListener(v -> {
            getOnBackPressedDispatcher().onBackPressed();
            finish();
        });
    }

    private WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets) {
        Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
        v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);

        ImageView image = findViewById(R.id.CapturedImage);

        image.setImageURI(captureImage); // Displaying the captured image
        return insets;
    }

    private void init() {
        txtSend = findViewById(R.id.txtSend);
        txtSend.setOnClickListener(view -> sendImagestoFirebase());
    }

    private void sendImagestoFirebase() {
        try {
            StorageReference reference = FirebaseStorage.getInstance().getReference()
                    .child("Users")
                    .child(Objects.requireNonNull(auth.getUid()))
                    .child("SentImages")
                    .child(System.currentTimeMillis() + ".jpg");

            reference.putFile(captureImage).addOnSuccessListener(taskSnapshot ->
                            Toast.makeText(CapturedImage.this, "Image uploaded successfully!", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> {
                        Toast.makeText(CapturedImage.this, "Failed to upload image!", Toast.LENGTH_SHORT).show();
                        Log.e("CapturedImage", "Upload failed", e);
                    });

        } catch (Exception e) {
            Log.e("CapturedImage", "Error during image upload", e);
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }

    public String getRecieverUid() {
        return recieverUid;
    }

    public void setRecieverUid(String recieverUid) {
        this.recieverUid = recieverUid;
    }
}
