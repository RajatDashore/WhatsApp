package com.example.whatsappclone.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whatsappclone.DataBaseHelper;
import com.example.whatsappclone.Fragments.ChatsFragments;
import com.example.whatsappclone.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class BlockedAdapter extends RecyclerView.Adapter<BlockedAdapter.MyViewHolder> {

    private final Context context;
    private final List<DataBaseHelper> list;

    public BlockedAdapter(Context context, List<DataBaseHelper> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.blocked_contact, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        DataBaseHelper contacts = list.get(position);
        Picasso.get().load(contacts.getImage()).placeholder(R.drawable.person).into(holder.imageView);
        holder.name.setText(contacts.getName());
        holder.gmail.setText(contacts.getEmail());
        ChatsFragments fragments = new ChatsFragments();
        Intent i = new Intent(context, ChatsFragments.class);
        i.putExtra("id", contacts.getUId());
        context.startActivity(i);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        CircleImageView imageView;
        TextView name, gmail;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.blockedImage);
            name = itemView.findViewById(R.id.blockedName);
            gmail = itemView.findViewById(R.id.blockedGmail);
        }
    }
}
