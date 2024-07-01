package com.example.whatsappclone;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.whatsappclone.Modules.Users;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 65;
    private static final int REQ_ONE_TAP = 2;
    private final boolean showOneTapUI = true;
    private BeginSignInRequest SignInRequest;
    private FirebaseDatabase database;
    private EditText userName, eMail, passWord;
    private FirebaseAuth mAuth;
    private com.google.android.gms.auth.api.identity.SignInClient SignInClient;
    private ProgressBar progressBar;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        Button btnSignUp = findViewById(R.id.btnSignUp);
        userName = findViewById(R.id.edtUsername);
        eMail = findViewById(R.id.edtEmail);
        passWord = findViewById(R.id.edtPassword);
        progressBar = findViewById(R.id.progressBar);
        TextView txtSignIn = findViewById(R.id.txtSignIn);
        Button btnGoogle = findViewById(R.id.btnGoogle);
        SignInClient = Identity.getSignInClient(this);
        getSupportActionBar().hide();


        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String username = userName.getText().toString();
                String email = eMail.getText().toString();
                String password = passWord.getText().toString();

                if (TextUtils.isEmpty(username)) {
                    userName.setError("Please enter Name");
                    passWord.requestFocus();
                    progressBar.setVisibility(View.GONE);
                    return;
                }
                if (TextUtils.isEmpty(email)) {
                    eMail.setError("Please enter Email");
                    eMail.requestFocus();
                    progressBar.setVisibility(View.GONE);
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    passWord.setError("Please enter Password");
                    passWord.requestFocus();
                    progressBar.setVisibility(View.GONE);
                    return;
                }
                if (TextUtils.isEmpty(password) && TextUtils.isEmpty(email)) {
                    Toast.makeText(SignUpActivity.this, "Enter Email & Password", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }
                mAuth.createUserWithEmailAndPassword(email.trim(), password)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(username).build();
                                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                                firebaseUser.updateProfile(userProfileChangeRequest);

                                Users users = new Users(mAuth.getUid(), username, email, password);
                                databaseReference.child(mAuth.getUid()).setValue(users);
                                Intent i = new Intent(SignUpActivity.this, MainActivity.class);
                                i.putExtra("username", username);
                                startActivity(i);
                                finish();

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(SignUpActivity.this, "Sign up Failed", Toast.LENGTH_SHORT).show();
                            }
                        });


            }
        });

        txtSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(i);
                finish();
            }
        });


        btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //signIn();
            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(SignUpActivity.this, MainActivity.class));
            finish();
        }
    }
}

