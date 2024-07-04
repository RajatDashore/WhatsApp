package com.example.whatsappclone;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whatsappclone.Adapters.BlockedAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BlockedContacts extends AppCompatActivity {
    private final List<DataBaseHelper> list = new ArrayList<>();
    private RecyclerView blockedRecyView;
    private BlockedAdapter blockedAdapter;

    public BlockedContacts() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_blocked_contacts);
        Objects.requireNonNull(getSupportActionBar()).hide();

        // Find the root view and apply window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return WindowInsetsCompat.CONSUMED;
        });

        // Initialize RecyclerView and Adapter
        blockedRecyView = findViewById(R.id.blockedRecyclerView);

        // Getting data from the DataBase and sending it to BlockedAdapter to show on RecyclerView
        // list = DataBase.getInstance(BlockedContacts.this).UserDao().getlist();
        blockedAdapter = new BlockedAdapter(this, list);
        blockedRecyView.setAdapter(blockedAdapter);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        lm.setOrientation(RecyclerView.VERTICAL);
        blockedRecyView.setLayoutManager(lm);
    }
}
