package com.example.whatsappclone;

import static androidx.core.content.PermissionChecker.PERMISSION_GRANTED;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whatsappclone.Adapters.ChatAdapter;
import com.example.whatsappclone.Modules.MessageModel;
import com.example.whatsappclone.Utills.NoInternet;
import com.example.whatsappclone.Utills.NoInternetDialog;
import com.example.whatsappclone.VideoCall.VideoCallOutgoing;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class ChatDetailActivity extends AppCompatActivity {

    private static final int REQUEST_GALLERY_PERMISSION = 111;
    private String recieveId;
    private Helper helper;
    private String username;
    private FirebaseStorage storage;
    private FirebaseDatabase database;
    private EditText edtChatting;
    private RecyclerView chatRecyclerView;
    private ImageView recording, send;
    private String senderNode, receiverNode;

    // Activity result launcher for capturing image
    private final ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
            Uri imageUri = result.getData().getData();
            if (imageUri != null) {
                uploadImageToFirebase(imageUri);
            }
        }
    });

    @SuppressLint({"UseCompatLoadingForDrawables", "MissingInflatedId", "ResourceAsColor"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!NoInternet.isNetworkAvailable(this)) {
            NoInternetDialog.showNoInternetDialog(this);
            ConstraintLayout layout = new ConstraintLayout(this);
            layout.setBackgroundColor(R.color.white);
            return;
        } else {
            //  setAllContent();
            setContentView(R.layout.activity_chat_detail);
            database = FirebaseDatabase.getInstance();
            storage = FirebaseStorage.getInstance();
            FirebaseAuth auth = FirebaseAuth.getInstance();

            edtChatting = findViewById(R.id.edtChatting);
            ImageView imgBackArrow = findViewById(R.id.imgBackArrow);
            TextView userName = findViewById(R.id.userName);
            ImageView proPicture = findViewById(R.id.proPicture);
            //recording = findViewById(R.id.recording);
            send = findViewById(R.id.send);
            ImageView imgDoller = findViewById(R.id.imgDolar);
            ImageView imgCamera = findViewById(R.id.imgCamera);
            chatRecyclerView = findViewById(R.id.chatRecylcerView);
            ImageView videoCallBtn = findViewById(R.id.videoCall);

            String senderId = auth.getUid();
            recieveId = getIntent().getStringExtra("id");
            username = getIntent().getStringExtra("userName");
            String profilePicture = getIntent().getStringExtra("proPicture");

            userName.setText(username);
            if (profilePicture != null && !profilePicture.isEmpty()) {
                Picasso.get().load(profilePicture).placeholder(R.drawable.person).into(proPicture);
            } else {
                proPicture.setImageResource(R.drawable.person);
            }

            senderNode = senderId + recieveId;
            receiverNode = recieveId + senderId;

            final ArrayList<MessageModel> messagesModel = new ArrayList<>();
            final ChatAdapter chatAdapter = new ChatAdapter(recieveId, this, messagesModel);
            chatRecyclerView.setAdapter(chatAdapter);
            chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));

            database.getReference().child("Messages").child(senderNode).addValueEventListener(new ValueEventListener() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    messagesModel.clear();
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        MessageModel model = ds.getValue(MessageModel.class);
                        if (model != null) {
                            model.setMessageId(ds.getKey());
                            messagesModel.add(model);
                        }
                    }
                    chatAdapter.notifyDataSetChanged();
                    chatRecyclerView.scrollToPosition(messagesModel.size() - 1);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(ChatDetailActivity.this, "Failed to load messages!", Toast.LENGTH_SHORT).show();
                }
            });

            send.setOnClickListener(v -> sendMessage(senderId));
            imgBackArrow.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());
            imgCamera.setOnClickListener(v -> captureImage());

            edtChatting.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    boolean hasText = !edtChatting.getText().toString().trim().isEmpty();
                    //send.setVisibility(hasText ? View.VISIBLE : View.GONE);
                    // recording.setVisibility(hasText ? View.GONE : View.VISIBLE);
                    imgDoller.setVisibility(hasText ? View.GONE : View.VISIBLE);
                    imgCamera.setVisibility(hasText ? View.GONE : View.VISIBLE);
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });

            videoCallBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(ChatDetailActivity.this, VideoCallOutgoing.class);
                    i.putExtra("recName", username);
                    Log.d("RajatDashore", username);
                    startActivity(i);
                }
            });
        }
    }
    // showSystemUI();


    private void captureImage() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_GALLERY_PERMISSION);
        } else {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            imagePickerLauncher.launch(intent);
        }
    }


    private void showSystemUI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowInsetsController insetsController = getWindow().getInsetsController();
            if (insetsController != null) {
                insetsController.show(WindowInsets.Type.systemBars());
            }
        } else {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
    }


    private void uploadImageToFirebase(Uri imageUri) {
        final StorageReference reference = storage.getReference().child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).child("SentImage");
        Intent i = new Intent(this, CapturedImage.class);
        i.putExtra("uri", imageUri);
        i.putExtra("uId", recieveId);
        startActivity(i);
     /*   reference.putFile(imageUri).addOnSuccessListener(taskSnapshot ->
                reference.getDownloadUrl().addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        Uri downloadUrl = task.getResult();
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("proPicture", downloadUrl.toString());
                        database.getReference().child("Users")
                                .child(FirebaseAuth.getInstance().getUid())
                                .child("SentImages").setValue(map);

                        Toast.makeText(ChatDetailActivity.this, "Image sent successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ChatDetailActivity.this, "Failed to upload image!", Toast.LENGTH_SHORT).show();
                    }
                })
        );
      */
    }

    private void sendMessage(String senderId) {
        String message = edtChatting.getText().toString().trim();
        if (!message.isEmpty()) {
            MessageModel model = new MessageModel(senderId, message);
            model.setTimeStamp(new Date().getTime());
            edtChatting.setText("");

            database.getReference().child("Messages").child(senderNode).push().setValue(model).addOnSuccessListener(unused -> database.getReference().child("Messages").child(receiverNode).push().setValue(model)).addOnFailureListener(e -> Toast.makeText(ChatDetailActivity.this, "Failed to send message!", Toast.LENGTH_SHORT).show());

            MediaPlayer player = MediaPlayer.create(this, R.raw.what_popup);
            player.start();
        } else {
            edtChatting.setError("Message cannot be empty");
        }
    }
}
