package com.example.whatsappclone.Adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whatsappclone.ChatDetailActivity;
import com.example.whatsappclone.DataBase;
import com.example.whatsappclone.DataBaseHelper;
import com.example.whatsappclone.Modules.Users;
import com.example.whatsappclone.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.MyViewHolder> {
    private final Context context;
    private final ArrayList<Users> list;
    private final DataBase database;
    private final DataBaseHelper helper;
    private int lastPosition = -1;

    public UsersAdapter(Context context, ArrayList<Users> list) {
        this.context = context;
        this.list = list;
        this.database = DataBase.getInstance(context);
        this.helper = new DataBaseHelper();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_show_user, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Users users = list.get(position);
        holder.name.setText(users.getUserName());
        RvAnimation(holder.itemView, position);

        Picasso.get().load(users.getProPicture()).placeholder(R.drawable.person).into(holder.imageView);

        FirebaseDatabase.getInstance().getReference().child("Messages")
                .child(FirebaseAuth.getInstance().getUid() + users.getUserId())
                .orderByChild("timeStamp")
                .limitToLast(1)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChildren()) {
                            for (DataSnapshot ds : snapshot.getChildren()) {
                                holder.lastMessage.setText(ds.child("message").getValue(String.class));
                                SimpleDateFormat sd = new SimpleDateFormat("h:MM aaa");
                                Long times = ds.child("timeStamp").getValue(Long.class);
                                holder.time.setText(sd.format(times));
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        holder.lastMessage.setText("Error");
                    }
                });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ChatDetailActivity.class);
                i.putExtra("userName", users.getUserName());
                i.putExtra("proPicture", users.getProPicture());
                i.putExtra("UserId", users.getUserId());
                context.startActivity(i);
            }
        });

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Work in Progress", Toast.LENGTH_SHORT).show();
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new AlertDialog.Builder(context)
                        .setTitle("Block contact")
                        .setMessage("Do you want to block?")
                        .setPositiveButton("Block", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Remove user from list and notify adapter
                                list.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, list.size());

                                // Add user to database
                                helper.setName(users.getUserName());
                                helper.setEmail(users.getMail());
                                helper.setImage(users.getProPicture());
                                database.UserDao().insert(helper);
                                Toast.makeText(context, "User added in DataBasThe", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();

                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void RvAnimation(View view, int position) {
        Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.fade_in);
        if (position > lastPosition) {
            lastPosition = position;
            animation.setDuration(500);  // Reduced duration
            view.startAnimation(animation);
        }
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        CircleImageView imageView;
        ImageView CapImage;
        TextView lastMessage, time;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.SampleUserName);
            imageView = itemView.findViewById(R.id.SampleproPicture);
            lastMessage = itemView.findViewById(R.id.lastMessage);
            time = itemView.findViewById(R.id.SampleTime);
            CapImage = itemView.findViewById(R.id.CapturedImage);
        }
    }
}
