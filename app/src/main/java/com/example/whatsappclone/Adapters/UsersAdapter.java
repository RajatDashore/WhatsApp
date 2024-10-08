package com.example.whatsappclone.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
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
import java.util.Calendar;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.MyViewHolder> {
    private static Context context;
    private final ArrayList<Users> list;
    // private final DataBase database;
    // private final DataBaseHelper helper;
    private int lastPosition = -1;

    public UsersAdapter(Context context, ArrayList<Users> list) {
        UsersAdapter.context = context;
        this.list = list;
        //    this.database = database;
        //  this.helper = helper;
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
        if (users.getUserName().length() > 15) {
            holder.name.setText(users.getUserName().substring(15, users.getUserName().length() - 1));
        } else {
            holder.name.setText(users.getUserName());
        }
        RvAnimation(holder.itemView, position);
        if (users.getUserId().equals(FirebaseAuth.getInstance().getUid())) {
            holder.name.setText(users.getUserName() + "(You)");
        }

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
                                Long timestamp = ds.child("timeStamp").getValue(Long.class);
                                Calendar now = Calendar.getInstance();
                                Date date = new Date(timestamp);
                                if (timestamp.longValue() == now.getTimeInMillis()) {
                                    @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat(" h:mm aaa");
                                    String strDate = dateFormat.format(date);
                                    holder.time.setText(strDate);
                                } else if (now.getTimeInMillis() - date.getTime() == 1) {
                                    holder.time.setText("yesterday");
                                } else if (now.get(Calendar.YEAR) - date.getYear() == 1) {
                                    holder.time.setText("MMMM YYYY");
                                } else if (now.get(Calendar.MONTH) - date.getMonth() == 1) {
                                    holder.time.setText(" d MMMM");
                                } else {
                                    @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat(" h:mm aaa");
                                    String strDate = dateFormat.format(date);
                                    holder.time.setText(strDate);
                                }
                            }
                        }
                    }

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        holder.lastMessage.setText("Error");
                    }
                });

        holder.itemView.setOnClickListener(view -> {
            Intent i = new Intent(context, ChatDetailActivity.class);
            i.putExtra("userName", users.getUserName());
            i.putExtra("proPicture", users.getProPicture());
            i.putExtra("id", users.getUserId());
            context.startActivity(i);
        });

        holder.imageView.setOnClickListener(v -> Toast.makeText(context, "Work in Progress", Toast.LENGTH_SHORT).show());

      /*  holder.itemView.setOnLongClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Block contact")
                    .setMessage("Do you want to block the user ?")
                    .setPositiveButton("Block", (dialog, which) -> {
                        // Remove user from list and notify adapter
                        list.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, list.size());

                        // Add user to database
//                        helper.setName(users.getUserName());
//                        helper.setUid(users.getUserId());
//                        helper.setEmail(users.getMail());
//                        helper.setImage(users.getProPicture());
//                        database.UserDao().insert(helper);

                        Toast.makeText(context, users.getUserName() + " has been blocked", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss()
                    ).setIcon(R.drawable.baseline_block_24)
                    .show();

            return true;
        });

       */
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    private void RvAnimation(View view, int position) {
        Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
        if (position > lastPosition) {
            lastPosition = position;
            animation.setDuration(300);
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
