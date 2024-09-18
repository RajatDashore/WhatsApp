package com.example.whatsappclone.Fragments;

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
import com.example.whatsappclone.BlockedContacts;
import com.example.whatsappclone.DataBase;
import com.example.whatsappclone.DataBaseHelper;
import com.example.whatsappclone.Modules.Users;
import com.example.whatsappclone.databinding.FragmentChatsFragmentsBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class ChatsFragments extends Fragment {


    private final ArrayList<Users> list = new ArrayList<>();
    private UsersAdapter usersAdapter;

    public ChatsFragments() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentChatsFragmentsBinding binding = FragmentChatsFragmentsBinding.inflate(inflater, container, false);
        DataBase database1 = DataBase.getInstance(ChatsFragments.this);
        final DataBaseHelper helper = new DataBaseHelper();
        usersAdapter = new UsersAdapter(getContext(), list, database1, helper);
        Intent i = new Intent(getContext(), BlockedContacts.class);
        i.putExtra("list", list);


        binding.chatRecyclerViewFragments.setAdapter(usersAdapter);
        FirebaseDatabase Fdatabase = FirebaseDatabase.getInstance();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.chatRecyclerViewFragments.setLayoutManager(layoutManager);


        Fdatabase.getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                if (snapshot.hasChildren()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Users users = dataSnapshot.getValue(Users.class);
                        if (users != null) {
                            users.setUserId(dataSnapshot.getKey());
                            list.add(users);
                        }
                    }
                    usersAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();

            }
        });

        return binding.getRoot();

    }
}