package com.example.whatsappclone.VideoCall;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.whatsappclone.Modules.Users;
import com.example.whatsappclone.R;
import com.example.whatsappclone.VideoCall.ForgroundService.MainServiceRepository;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.FirebaseDatabase;

import javax.inject.Inject;

public class VideoCallOutgoing extends AppCompatActivity {
    private ImageView imageView;
    private FloatingActionButton declineBtn;
    private TextView TvName;
    private String RecieverName, RecieverToken, ReciverUrl, RecieverUid;
    private FirebaseDatabase database;
    @Inject
    private MainServiceRepository mainServiceRepository;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_video_call_outgoing);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            String proPicture = null;
            imageView = findViewById(R.id.imgVcOut);
            TvName = findViewById(R.id.nameVcOut);
            declineBtn = findViewById(R.id.decline_VideoOut);
            database = FirebaseDatabase.getInstance();
            Users users = new Users();
            Intent i = getIntent();
            RecieverUid = i.getStringExtra("Uid");
            RecieverName = i.getStringExtra("Name");
            proPicture = i.getStringExtra("ProPicture");
             startservice();

            String finalProPicture = proPicture;
          /*  database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChildren()) {
                                RecieverName = snapshot.child("userName").getValue(String.class);
                                TvName.setText(RecieverName);
                                Picasso.get().load(finalProPicture).placeholder(R.drawable.person).into(imageView);

                            } else {
                                Toast.makeText(VideoCallOutgoing.this, "Can not make call", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
           */


            return insets;
        });
    }


    private void startservice() {
        mainServiceRepository.startService("Rohit Pawar");
    }


}



