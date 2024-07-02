package com.example.whatsappclone;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class BlockedContacts extends AppCompatActivity {
    private CircleImageView image;
    private TextView Username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_blocked_contacts);
        getSupportActionBar().hide();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            Username = findViewById(R.id.blockedName);
            image = findViewById(R.id.blockedImage);
            Intent i = getIntent();
            String name = i.getStringExtra("name");
            String pro = i.getStringExtra("pro");
            Username.setText(name);
            Picasso.get().load(pro).placeholder(R.drawable.person).into(image);
            return insets;
        });
    }
}