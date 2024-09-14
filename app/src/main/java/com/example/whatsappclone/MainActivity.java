package com.example.whatsappclone;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.whatsappclone.Adapters.FragmentAdapter;
import com.example.whatsappclone.databinding.ActivityMainBinding;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private RecyclerView recyclerView;
    private String YourName;
    private FirebaseUser user;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private FirebaseAuth auth;
    private FragmentAdapter fragmentAdapter;
    private DatabaseReference databaseReference;
    private GoogleSignInClient client;


    public static String TimeStampToString(long timestamp) {
        return new SimpleDateFormat("HH:mm").format(timestamp);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager()));
        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
        auth = FirebaseAuth.getInstance();
        tabLayout.getTabAt(0).setIcon(R.drawable.baseline_chat_24);
        tabLayout.getTabAt(1).setIcon(R.drawable.search);
        tabLayout.getTabAt(2).setIcon(R.drawable.baseline_call_24);
        databaseReference = FirebaseDatabase.getInstance().getReference();

        user = auth.getCurrentUser();
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setIcon(R.drawable.whatapp_svg_splash);
        getSupportActionBar().show();
        String userName = getIntent().getStringExtra("username");
        getSupportActionBar().setTitle(userName);

        if (user == null) {
            Intent i = new Intent(MainActivity.this, SignUpActivity.class);
            startActivity(i);
            finish();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.setting) {
            Toast.makeText(this, "Setting opened", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(MainActivity.this, Settings.class);
            startActivity(i);
        } else if (item.getItemId() == R.id.Logout) {
//            Dialog dialog = new Dialog(getApplicationContext());
//            dialog.setContentView(R.layout.logout);
//            dialog.show();
//            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().requestIdToken(String.valueOf(R.string.default_web_client_id)).build();
//            client = GoogleSignIn.getClient(getApplicationContext(), gso);
            Intent i = new Intent(MainActivity.this, SignUpActivity.class);
            startActivity(i);
            finish();
        }

        return true;
    }
}