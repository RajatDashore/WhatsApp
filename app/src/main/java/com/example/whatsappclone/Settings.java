package com.example.whatsappclone;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.whatsappclone.Modules.Users;
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
                                    .child("ProPicture")
                                    .child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()));

                            reference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            database.getReference().child("Users")
                                                    .child(FirebaseAuth.getInstance().getUid())
                                                    .child("proPicture")
                                                    .setValue(uri.toString());
                                        }
                                    });
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
        getSupportActionBar().hide();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Settings.this, MainActivity.class));
                finish();
            }
        });

        EditText Name = findViewById(R.id.Name);
        EditText About = findViewById(R.id.aboutMe);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = Name.getText().toString();
                String about = About.getText().toString();

                if (name.trim().length() > 0 && about.trim().length() > 0) {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("userName", name);
                    map.put("aboutMe", about);

                    database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                            .updateChildren(map);
                    Toast.makeText(Settings.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Settings.this, "Field can't be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

        database.getReference().child("Users")
                .child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Users users = snapshot.getValue(Users.class);
                        Picasso.get().load(Objects.requireNonNull(users).getProPicture()).placeholder(R.drawable.person).into(imgPerson);

                        About.setText(users.getAboutMe());
                        Name.setText(users.getUserName());

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
    }
}
