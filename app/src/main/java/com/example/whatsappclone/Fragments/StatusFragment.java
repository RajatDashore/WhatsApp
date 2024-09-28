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
import com.example.whatsappclone.Modules.UserStatus;
import com.example.whatsappclone.R;
import com.example.whatsappclone.databinding.FragmentStatusBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;


public class StatusFragment extends Fragment {
    ProgressDialog dialog;
    TopStatusAdapter statusAdapter;
    RecyclerView statusRecyclerView;
    ArrayList<UserStatus> list;
    FragmentStatusBinding binding;
    ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result != null) {
                if (result.getData() != null) {
                    Uri uri = result.is getData().getData();
                    dialog.show();
                    Date date = new Date();

                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    storage.getReference().child("Status").child(FirebaseAuth.getInstance().getUid())
                            .child(String.valueOf(date.getTime())).putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        storage.getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                Picasso.get().load(uri).placeholder(R.drawable.person).into(binding.myStatus);
                                                dialog.dismiss();
                                            }
                                        });
                                    }
                                }
                            });
                }
                dialog.dismiss();
            }
        }
    });

    public StatusFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentStatusBinding.inflate(getLayoutInflater());
        list = new ArrayList<>();
        dialog = new ProgressDialog(getContext());
        dialog.setMessage("Uploading");
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

        return binding.getRoot();
    }
}