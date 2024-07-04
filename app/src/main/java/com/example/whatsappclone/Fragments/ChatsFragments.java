package com.example.whatsappclone.Fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.whatsappclone.Adapters.UsersAdapter;
import com.example.whatsappclone.DataBase;
import com.example.whatsappclone.DataBaseHelper;
import com.example.whatsappclone.Modules.Users;
import com.example.whatsappclone.databinding.FragmentChatsFragmentsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Objects;


public class ChatsFragments extends Fragment {


    private final ArrayList<Users> list = new ArrayList<>();
    private UsersAdapter usersAdapter;

    public ChatsFragments() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        com.example.whatsappclone.databinding.FragmentChatsFragmentsBinding binding = FragmentChatsFragmentsBinding.inflate(inflater, container, false);
        DataBase database1 = DataBase.getInstance(ChatsFragments.this);
        DataBaseHelper helper = (DataBaseHelper) database1.UserDao().getlist();
        usersAdapter = new UsersAdapter(getContext(), list, database1, helper);
        binding.chatRecyclerViewFragments.setAdapter(usersAdapter);
        FirebaseDatabase Fdatabase = FirebaseDatabase.getInstance();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.chatRecyclerViewFragments.setLayoutManager(layoutManager);
        Fdatabase.getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot datasnapshot : snapshot.getChildren()) {
                    Users users = datasnapshot.getValue(Users.class);
                    assert users != null;

                    // Blocked user will be gone from the Recycle View
                    String Uid;
                    try {
                        Uid = String.valueOf(Intent.getIntent("Uid"));
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }

                    if (!users.getUserId().equals(FirebaseAuth.getInstance().getUid()) || !Objects.equals(Uid, users.getUserId())) {
                        users.setUserId(datasnapshot.getKey());
                        list.add(users);
                    }
                }
                usersAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        });


        return binding.getRoot();

    }
}