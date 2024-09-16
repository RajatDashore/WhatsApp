package com.example.whatsappclone.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whatsappclone.Modules.MessageModel;
import com.example.whatsappclone.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ChatAdapter extends RecyclerView.Adapter {

    private final Context context;
    private final ArrayList<MessageModel> list;
    private final int SENDER = 1;
    private final int RECIEVER = 2;
    private String RecId;


    public ChatAdapter(String recId, Context context, ArrayList<MessageModel> list) {
        this.RecId = recId;
        this.context = context;
        this.list = list;
    }

    public ChatAdapter(Context context, ArrayList<MessageModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == SENDER) {
            View view = LayoutInflater.from(context).inflate(R.layout.sample_sender, parent, false);
            return new SenderViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.sample_reciever, parent, false);
            return new RecieverViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (list.get(position).getuID().equals(FirebaseAuth.getInstance().getUid())) {
            return SENDER;
        } else {
            return RECIEVER;
        }
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MessageModel model = list.get(position);
        // to delete the chats from the ChatRecyclerView
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                new AlertDialog.Builder(context).setIcon(R.drawable.baseline_delete_24).setTitle("Delete").setMessage("Delete message").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        String senderID = FirebaseAuth.getInstance().getUid() + RecId;
                        database.getReference().child("Messages").child(senderID).child(model.getMessageId()).setValue(null);
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();

                return false;
            }
        });

        if (holder.getClass() == SenderViewHolder.class) {
            ((SenderViewHolder) holder).SenderMSg.setText(model.getMessage());
            Date date = new Date(model.getTimeStamp());
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy KK:mm aaa");
            String strDate = dateFormat.format(date);
            ((SenderViewHolder) holder).STimeStamp.setText(strDate);

        } else {
            ((RecieverViewHolder) holder).RecieverMSg.setText(model.getMessage());
            Date date = new Date(model.getTimeStamp());
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy KK:mm aaa");
            String strDate = dateFormat.format(date);
            ((RecieverViewHolder) holder).rTimeStamp.setText(strDate);
        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class SenderViewHolder extends RecyclerView.ViewHolder {
        TextView SenderMSg, STimeStamp;

        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            SenderMSg = itemView.findViewById(R.id.SenderMessage);
            STimeStamp = itemView.findViewById(R.id.TimeStampSender);
        }
    }


    public static class RecieverViewHolder extends RecyclerView.ViewHolder {
        TextView RecieverMSg, rTimeStamp;

        public RecieverViewHolder(@NonNull View itemView) {
            super(itemView);

            RecieverMSg = itemView.findViewById(R.id.RecievedMessage);
            rTimeStamp = itemView.findViewById(R.id.TimeStampReciever);
        }
    }

}
