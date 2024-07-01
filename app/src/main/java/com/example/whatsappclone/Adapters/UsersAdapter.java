package com.example.whatsappclone.Adapters;

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
import com.example.whatsappclone.Modules.MessageModel;
import com.example.whatsappclone.Modules.Users;
import com.example.whatsappclone.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.MyViewHolder> {
    private final Context context;
    private final ArrayList<Users> list;
    private int lastPosition = -1;

    public UsersAdapter(Context context, ArrayList<Users> list) {
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_show_user, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Users users = list.get(position);
        MessageModel model = new MessageModel();
        holder.name.setText(users.getUserName());
        //holder.time.setText(model.getTimeStamp().toString());
        RvAnimation(holder.itemView, position);

        Picasso.get().load(users.getProPicture()).placeholder(R.drawable.person).into(holder.imageView);
        // holder.lastMessage.setText(users.getLastMessage());
        FirebaseDatabase.getInstance().getReference().child("Messages").child(FirebaseAuth.getInstance().getUid() + users.getUserId()).orderByChild("timeStamp").limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChildren()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        holder.lastMessage.setText(ds.child("message").getValue(String.class));
//                        Date date = new Date(ds.child("timeStamp").getValue(Long.class));
//                        SimpleDateFormat f = new SimpleDateFormat("h:mm a");
//                        holder.time.setText(f.format(date));
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
//                Dialog dialog = new Dialog(context);
//                  dialog.setContentView(R.layout.activity_captured_image);
//                dialog.setContentView(R.layout.activity_captured_image);
//                Picasso.get().load(users.getProPicture()).placeholder(R.drawable.person).into(holder.CapImage);
//                Animation au = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
//                au.setDuration(2000);
//                holder.CapImage.startAnimation(au);
//                dialog.setCancelable(true);
//                holder.CapImage.setAnimation(null);
//                dialog.show();
                Toast.makeText(context, "Work in Progress", Toast.LENGTH_SHORT).show();
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new AlertDialog.Builder(context).
                        setTitle("Block contect")
                        .setMessage("Do you want to block").setPositiveButton("Block", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                holder.itemView.setVisibility(View.GONE);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();

                return false;
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
            animation.setDuration(1500);
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
