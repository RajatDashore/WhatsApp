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

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class SignInActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 20;
    private FirebaseDatabase databaseIn;
    private ProgressBar progressBar;
    private FirebaseAuth authIn;
    private EditText eMail, passWord;
    private DatabaseReference databaseReference;
    private GoogleSignInClient mGoogleSignInClient;

    public SignInActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        Objects.requireNonNull(getSupportActionBar()).hide();
        authIn = FirebaseAuth.getInstance();
        databaseIn = FirebaseDatabase.getInstance();
        progressBar = findViewById(R.id.progressBar);
        TextView txtSignUp = findViewById(R.id.txtSignUp);
        Button btnSignIn = findViewById(R.id.btnSignIn);
        eMail = findViewById(R.id.edtEmail);
        passWord = findViewById(R.id.edtPassword);
        Button btnGoogle = findViewById(R.id.btnGoogle);
        databaseReference = FirebaseDatabase.getInstance().getReference();

        if (authIn.getCurrentUser() != null) {
            Intent i = new Intent(SignInActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInWithEmail();
            }
        });


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(getResources().getString(R.string.default_web_client_id))
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        txtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(i);
                finish();
            }
        });


        btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                handleSignInResult(account.getIdToken());
            } catch (ApiException e) {
                Toast.makeText(this, "Login failed ", Toast.LENGTH_LONG).show();
            }

        }
    }


    private void handleSignInResult(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        authIn.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(SignInActivity.this, "Logined successfylly", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SignInActivity.this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(SignInActivity.this, "Login failed", Toast.LENGTH_LONG).show();
                }
            }
        });


    }


    private void signInWithEmail() {
        progressBar.setVisibility(View.VISIBLE);
        String email = eMail.getText().toString().trim();
        String password = passWord.getText().toString().trim();

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
        if (TextUtils.isEmpty(email) && TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Enter email & Password", Toast.LENGTH_SHORT).show();
        }

        authIn.signInWithEmailAndPassword(email.trim(), password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        String username = authIn.getCurrentUser().getDisplayName();
                        Intent i = new Intent(SignInActivity.this, MainActivity.class);
                        i.putExtra("username", username);
                        startActivity(i);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

    }


}
