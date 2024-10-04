package com.example.whatsappclone.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whatsappclone.Adapters.TopStatusAdapter;
import com.example.whatsappclone.Modules.Status;
import com.example.whatsappclone.Modules.UserStatus;
import com.example.whatsappclone.Modules.Users;
import com.example.whatsappclone.R;
import com.example.whatsappclone.databinding.FragmentStatusBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


public class StatusFragment extends Fragment {
    private ProgressDialog dialog;
    private TopStatusAdapter statusAdapter;
    private RecyclerView statusRecyclerView;
    private ArrayList<UserStatus> list;
    private FragmentStatusBinding binding;
    private FirebaseDatabase database;
    private Users users;
    ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result != null) {
                if (result.getData() != null) {
                    Uri uri = result.getData().getData();

                    Date date = new Date();
                    dialog.show();
                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    StorageReference referance = storage.getReference().child("Status").child(FirebaseAuth.getInstance().getUid()).child(String.valueOf(date.getTime()));

                    assert uri != null;
                    referance.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()) {
                                referance.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String imageUrl = uri.toString();
                                        UserStatus userStatus = new UserStatus();
                                        userStatus.setStatusName(users.getUserName());
                                        userStatus.setStatusProfileImage(users.getProPicture());
                                        userStatus.setLastUpdated(date.getTime());


                                        Status status = new Status(imageUrl, userStatus.getLastUpdated());

                                        HashMap<String, Object> obj = new HashMap<>();
                                        obj.put("StatusName", userStatus.getStatusName());
                                        obj.put("StatusProfileImage", userStatus.getStatusProfileImage());
                                        obj.put("lastUpdated", userStatus.getLastUpdated());
                                        obj.put("statuses", status);

                                        //  database.getReference().child("Status").child(FirebaseAuth.getInstance().getUid()).updateChildren(obj);
                                        database.getReference().child("Status").child(FirebaseAuth.getInstance().getUid()).child("statuses").push().setValue(status);


                                        binding.imageView4.setVisibility(View.GONE);
                                        Picasso.get().load(uri).placeholder(R.drawable.person).into(binding.myStatus);
                                        dialog.dismiss();
                                    }
                                });
                            }
                        }
                    });


                    //

                }
            }
        }
    });


    public StatusFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentStatusBinding.inflate(getLayoutInflater());
        list = new ArrayList<>();
        dialog = new ProgressDialog(getContext());
        dialog.setMessage("Uploading...");
        database = FirebaseDatabase.getInstance();
        dialog.setCancelable(false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        binding.StatusRecyclerView.setLayoutManager(linearLayoutManager);
        statusAdapter = new TopStatusAdapter(getContext(), list);
        binding.StatusRecyclerView.setAdapter(statusAdapter);
        binding.myStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                launcher.launch(intent);
            }
        });


        database.getReference().child("Status").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        UserStatus status = new UserStatus();
                        status.setStatusName(dataSnapshot.child("StatusName").getValue(String.class));
                        status.setStatusProfileImage(dataSnapshot.child("StatusProfileImageStatusProfileImage").getValue(String.class));
                        status.setLastUpdated(dataSnapshot.child("lastUpdated").getValue(Long.class));

                        ArrayList<Status> arr = new ArrayList<>();
                        for (DataSnapshot arrSnapshot : dataSnapshot.child("statuses").getChildren()) {
                            Status s1 = arrSnapshot.getValue(Status.class);
                            arr.add(s1);
                        }
                        status.setStatusArrayList(arr);

                        list.add(status);

                    }
                    statusAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return binding.getRoot();
    }
}