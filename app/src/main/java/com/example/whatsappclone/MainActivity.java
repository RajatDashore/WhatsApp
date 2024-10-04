package com.example.whatsappclone;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.whatsappclone.Adapters.FragmentAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewPager viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager()));
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        Objects.requireNonNull(tabLayout.getTabAt(0)).setIcon(R.drawable.baseline_chat_24);
        Objects.requireNonNull(tabLayout.getTabAt(1)).setIcon(R.drawable.search);
        Objects.requireNonNull(tabLayout.getTabAt(2)).setIcon(R.drawable.baseline_call_24);

        FirebaseUser user = auth.getCurrentUser();
        Objects.requireNonNull(getSupportActionBar()).setTitle("Chatter");


        if (user == null) {
            Intent i = new Intent(MainActivity.this, SignUpActivity.class);
            startActivity(i);
            finish();
        }
        FloatingActionButton ftButton = findViewById(R.id.floatButton);
        ftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Working on it", Toast.LENGTH_SHORT).show();
            }
        });

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
//            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().requestIdToken(String.valueOf(R.string.default_web_client_id)).build();
//            client = GoogleSignIn.getClient(getApplicationContext(), gso);
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setCancelable(false);
            dialog.setIcon(R.drawable.baseline_logout_24);
            dialog.setMessage("Do you want to logout ?")
                    .setIcon(R.drawable.logout_big)
                    .setTitle("Logout")
                    .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            FirebaseAuth.getInstance().signOut();
                            Intent i = new Intent(MainActivity.this, SignUpActivity.class);
                            startActivity(i);
                            finish();
                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
        }

        return true;
    }
}