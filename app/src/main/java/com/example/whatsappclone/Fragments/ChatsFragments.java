package com.example.whatsappclone.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.whatsappclone.Adapters.UsersAdapter;
import com.example.whatsappclone.Modules.Users;
import com.example.whatsappclone.databinding.FragmentChatsFragmentsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class ChatsFragments extends Fragment {


    private final ArrayList<Users> list = new ArrayList<Users>();
    String YourName;
    private FragmentChatsFragmentsBinding binding;
    private FirebaseDatabase database;
    private UsersAdapter usersAdapter;

    public ChatsFragments() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentChatsFragmentsBinding.inflate(inflater, container, false);
        usersAdapter = new UsersAdapter(getContext(), list);
        binding.chatRecyclerViewFragments.setAdapter(usersAdapter);
        database = FirebaseDatabase.getInstance();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.chatRecyclerViewFragments.setLayoutManager(layoutManager);

        database.getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot datasnapshot : snapshot.getChildren()) {
                    Users users = datasnapshot.getValue(Users.class);
                    if (!users.getUserId().equals(FirebaseAuth.getInstance().getUid())) {
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