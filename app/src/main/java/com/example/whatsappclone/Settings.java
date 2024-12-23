package com.example.whatsappclone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class Settings extends AppCompatActivity {
    private FirebaseDatabase database;
    private FirebaseStorage storage;
    private ProgressDialog progressDialog;
    private CircleImageView imgPerson;

    ActivityResultLauncher<Intent> SettingLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        if (imageUri != null) {
                            progressDialog.show();
                            final StorageReference reference = storage.getReference()
                                    .child("Profile_Picture")
                                    .child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()));

                            // Upload the image to Firebase Storage
                            reference.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
                                // Get the download URL of the image
                                reference.getDownloadUrl().addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        Uri downloadUrl = task.getResult();
                                        if (downloadUrl != null) {
                                            // Save the download URL in the database
                                            HashMap<String, Object> map = new HashMap<>();
                                            map.put("proPicture", downloadUrl.toString());
                                            database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).updateChildren(map);

                                            // Update the UI with the image
                                            Picasso.get().load(downloadUrl).placeholder(R.drawable.person).into(imgPerson);
                                            progressDialog.dismiss();
                                            //Toast.makeText(Settings.this, "Profile Picture Updated", Toast.LENGTH_SHORT).show();

                                        }
                                    } else {
                                        progressDialog.dismiss();
                                        Toast.makeText(Settings.this, "Failed to get image URL", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            });
                        }
                    }

                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        imgPerson = findViewById(R.id.person);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();
        progressDialog = new ProgressDialog(Settings.this);
        progressDialog.setMax(100);
        progressDialog.setMessage("Loading...");
        progressDialog.setTitle("Updating the profile images");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);


        Button saveButton = findViewById(R.id.SaveButton);
        ImageView back = findViewById(R.id.backArrow);
        Objects.requireNonNull(getSupportActionBar()).hide();

        // Load the existing profile picture from the database
        database.getReference().child("Users")
                .child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String img = snapshot.child("proPicture").getValue(String.class);
                        if (img != null && !img.isEmpty()) {
                            Picasso.get().load(img).placeholder(R.drawable.person).into(imgPerson);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(Settings.this, "Failed to load profile picture", Toast.LENGTH_SHORT).show();
                    }
                });

        // Set an OnClickListener for the profile picture
        imgPerson.setOnClickListener(v -> {
            Intent i = new Intent();
            i.setAction(Intent.ACTION_GET_CONTENT);
            i.setType("image/*");

            SettingLauncher.launch(i);
        });

        // Set an OnClickListener for the back button
        back.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());
    }
}
