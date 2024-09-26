package com.example.whatsappclone;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whatsappclone.Adapters.ChatAdapter;
import com.example.whatsappclone.Modules.MessageModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class ChatDetailActivity extends AppCompatActivity {


    private final static int REQUEST_CAMERA_PERMISSION = 111;
    ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        int R = result.getResultCode();
        Intent data = result.getData();
        if (R == RESULT_OK && data != null) {
            Bundle extras = Objects.requireNonNull(data).getExtras();
            Bitmap map = (Bitmap) Objects.requireNonNull(extras).get("data");
            Intent i = new Intent(ChatDetailActivity.this, CapturedImage.class);
            i.putExtra("bitMap", map);
            startActivity(i);
        }
    });

    private EditText edtChatting;
    private RecyclerView chatRecyclerView;
    private FirebaseDatabase database;
    private ImageView recording, send;
    private String senderNode, recieverNode;

    @SuppressLint({"UseCompatLoadingForDrawables", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_detail);
        database = FirebaseDatabase.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        edtChatting = findViewById(R.id.edtChatting);
        ImageView imgBackArrow = findViewById(R.id.imgBackArrow);
        TextView userName = findViewById(R.id.userName);
        ImageView proPicture = findViewById(R.id.proPicture);
        recording = findViewById(R.id.recording);
        send = findViewById(R.id.send);
        ImageView imgDoller = findViewById(R.id.imgDolar);
        ImageView imgCamera = findViewById(R.id.imgCamera);
        chatRecyclerView = findViewById(R.id.chatRecylcerView);
        ImageView videoCallBtn = findViewById(R.id.videoCall);
        edtChatting.setBackground(getDrawable(android.R.drawable.screen_background_light_transparent));

        final String senderId = auth.getUid();
        String recieveid = getIntent().getStringExtra("id");
        String username = getIntent().getStringExtra("userName");
        String propicture = getIntent().getStringExtra("proPicture");


        videoCallBtn.setOnClickListener(v -> Toast.makeText(ChatDetailActivity.this, "Work in progress", Toast.LENGTH_SHORT).show());


        userName.setText(username);
        Picasso.get().load(propicture).placeholder(R.drawable.person).into(proPicture);

        senderNode = FirebaseAuth.getInstance().getUid() + recieveid;
        recieverNode = recieveid + FirebaseAuth.getInstance().getUid();

        ImageView call = findViewById(R.id.imageView5);
        call.setOnClickListener(v -> Toast.makeText(ChatDetailActivity.this, "Work in progress", Toast.LENGTH_SHORT).show());


        final ArrayList<MessageModel> messagesModel = new ArrayList<>();
        final ChatAdapter chatAdapter = new ChatAdapter(recieveid, this, messagesModel);
        chatRecyclerView.setAdapter(chatAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        chatRecyclerView.setLayoutManager(linearLayoutManager);
        chatRecyclerView.smoothScrollToPosition(messagesModel.size());


        database.getReference().child("Messages").child(senderNode).addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messagesModel.clear();
                if (snapshot.hasChildren()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        MessageModel model = ds.getValue(MessageModel.class);
                        assert model != null;
                        model.setMessageId(ds.getKey());
                        messagesModel.add(model);
                    }
                }

                chatAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        String randomKey = database.getReference().push().getKey();
        send.setOnClickListener(v -> {
            String message = edtChatting.getText().toString();
            if (!message.trim().isEmpty()) {
                final MessageModel model = new MessageModel(senderId, message);
                model.setTimeStamp(new Date().getTime());
                edtChatting.setText("");
                chatRecyclerView.smoothScrollToPosition(messagesModel.size());
                MediaPlayer player = MediaPlayer.create(getApplicationContext(), R.raw.what_popup);
                player.start();
                if (Objects.equals(FirebaseAuth.getInstance().getUid(), recieveid)) {
                    database.getReference().child("Messages").child(senderNode).child(randomKey).setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(ChatDetailActivity.this, "Only one sided", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    database.getReference().child("Messages").child(senderNode).child(randomKey).setValue(model).addOnSuccessListener(unused -> database.getReference().child("Messages").child(recieverNode).push().setValue(model).addOnSuccessListener(unused1 -> {

                    }));
                }

            } else {
                edtChatting.setError("Message can't be send");
            }
        });

        imgBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChatDetailActivity.this, MainActivity.class));
                finish();
            }
        });


        edtChatting.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!edtChatting.getText().toString().trim().isEmpty()) {
                    send.setVisibility(View.VISIBLE);
                    recording.setVisibility(View.GONE);
                    imgDoller.setVisibility(View.GONE);
                    imgCamera.setVisibility(View.GONE);
                    edtChatting.setEms(12);

                } else {
                    send.setVisibility(View.GONE);
                    recording.setVisibility(View.VISIBLE);
                    imgDoller.setVisibility(View.VISIBLE);
                    imgCamera.setVisibility(View.VISIBLE);
                    edtChatting.setEms(6);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (edtChatting.getLineCount() == edtChatting.getMaxLines()) {
                    edtChatting.setMaxLines(edtChatting.getLineCount() + 1);
                }
            }
        });

        imgCamera.setOnClickListener(this::CaptureImage);

    }


    public void CaptureImage(View View) {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        } else {
            Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            launcher.launch(i);
        }
    }

}