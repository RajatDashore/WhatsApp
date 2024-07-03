package com.example.whatsappclone;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whatsappclone.Adapters.BlockedAdapter;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class BlockedContacts extends AppCompatActivity {
    private CircleImageView image;
    private TextView Username;
    private RecyclerView blockedRecyView;
    private DataBase database;
    private DataBaseHelper helper;
    private BlockedAdapter blockedAdapter;

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


            blockedRecyView = findViewById(R.id.blockedRecyclerView);
            updadeRecyView();
            Picasso.get().load(pro).placeholder(R.drawable.person).into(image);


            return insets;
        });
    }

    private void updadeRecyView() {
        blockedRecyView.setHasFixedSize(true);
        blockedAdapter = new BlockedAdapter(this);
        blockedRecyView.setAdapter(blockedAdapter);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        blockedRecyView.setLayoutManager(lm);
    }
}