package com.example.whatsappclone;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import com.example.whatsappclone.Modules.Users;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;
import java.util.Map;

public class FirebaseNotificationService extends FirebaseMessagingService {

    @SuppressLint("RestrictedApi")
    Users users = new Users();

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
    }

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
    }

    private void updateToken(String token) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(users.getUserId());

        Map<String, Object> map = new HashMap<>();
        map.put("token", token);
        reference.updateChildren(map);


    }
}
