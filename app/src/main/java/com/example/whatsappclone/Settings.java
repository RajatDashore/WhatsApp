package com.example.whatsappclone;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class Settings extends AppCompatActivity {
    private FirebaseDatabase database;
    private FirebaseStorage storage;
    private CircleImageView imgPerson;

    ActivityResultLauncher<Intent> SettingLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        if (imageUri != null) {
                            final StorageReference reference = storage.getReference()
                                    .child("Profile_Picture")
                                    .child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()));

                            reference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    HashMap<String, Object> map = new HashMap<>();
                                    map.put("proPicture", imageUri);
                                    database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                                            .setValue(map);
                                    Toast.makeText(Settings.this, "Image in fDataBase", Toast.LENGTH_SHORT).show();
                                }
                            });
                            imgPerson.setImageURI(imageUri);
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

        Button saveButton = findViewById(R.id.SaveButton);
        ImageView back = findViewById(R.id.backArrow);
        Objects.requireNonNull(getSupportActionBar()).hide();


        database.getReference().child("Users")
                .child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String img = snapshot.child("proPicture").getValue(String.class);
                        Picasso.get().load(img).placeholder(R.drawable.person).into(imgPerson);
                        Toast.makeText(Settings.this, "Hello Photo", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(Settings.this, "Failed to update the photo", Toast.LENGTH_SHORT).show();
                    }
                });

        imgPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setAction(Intent.ACTION_GET_CONTENT);
                i.setType("image/*");
                SettingLauncher.launch(i);
            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOnBackPressedDispatcher().onBackPressed();
            }
        });
    }
}
