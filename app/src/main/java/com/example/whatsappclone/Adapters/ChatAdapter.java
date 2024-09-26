package com.example.whatsappclone.Adapters;

import static android.icu.text.DateFormat.ABBR_MONTH_DAY;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whatsappclone.Modules.MessageModel;
import com.example.whatsappclone.R;
import com.github.pgreze.reactions.ReactionPopup;
import com.github.pgreze.reactions.ReactionsConfig;
import com.github.pgreze.reactions.ReactionsConfigBuilder;
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
    private final String RecId;


    public ChatAdapter(String recId, Context context, ArrayList<MessageModel> list) {
        this.RecId = recId;
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
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        MessageModel model = list.get(position);
        // to delete the chats from the ChatRecyclerView

          /* holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new AlertDialog.Builder(context).setIcon(R.drawable.baseline_delete_24)
                        .setCancelable(false)
                        .setTitle("Delete the message")
                        .setMessage("Are you sure you want to delete ?")
                        .setPositiveButton("Delete for me", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                String senderID = FirebaseAuth.getInstance().getUid() + RecId;
                                database.getReference().child("Messages").child(senderID).child(model.getMessageId()).setValue(null);
                                Toast.makeText(context, "Message has been deleted", Toast.LENGTH_SHORT).show();
                            }
                        }).setNegativeButton("Delete for everyone", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String senderId = FirebaseAuth.getInstance().getUid() + RecId;
                                String recId = RecId + FirebaseAuth.getInstance().getUid();
                                FirebaseDatabase.getInstance().getReference().child("Messages").child(senderId).child(model.getMessageId()).setValue(null);
                                FirebaseDatabase.getInstance().getReference().child("Messages").child(recId).child(model.getMessageId()).setValue(null);
                                dialog.dismiss();
                            }
                        })
                        .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
                return false;
            }
        });
           */
        //        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                MessageModel model1;
//                isSelected = true;
//                if (selected.contains(list.get(position))) {
//                    selected.remove(list.get(position));
//                    VideoCall.setVisibility(View.GONE);
//                    Call.setVisibility(View.GONE);
//                    Dots.setVisibility(View.GONE);
//                } else {
//                    holder.itemView.setBackgroundColor(Color.BLUE);
//                    selected.add(list.get(position));
//                    VideoCall.setVisibility(View.VISIBLE);
//                    Call.setVisibility(View.GONE);
//                    Dots.setVisibility(View.GONE);
//                }
//                if (selected.size() > 0) {
//
//                }
//
//                if (selected.size() == 0) {
//                    isSelected = false;
//                    return true;
//                }
//                return false;
//            }
//        });

        int[] reaction = (new int[]{R.drawable.baseline_emoji_emotions_24, R.drawable.emoji_laugh, R.drawable.emoji_red_eye, R.drawable.confused, R.drawable.emozy_sad,});
        ReactionsConfig config = new ReactionsConfigBuilder(context).withReactions(reaction).build();

        ReactionPopup popup = new ReactionPopup(context, config, (pos) -> {
            if (holder.getClass() == SenderViewHolder.class) {
                SenderViewHolder viewHolder = (SenderViewHolder) holder;
                viewHolder.SendReation.setImageResource(reaction[pos]);
                viewHolder.SendReation.setVisibility(View.VISIBLE);
            } else {
                RecieverViewHolder viewHolder = (RecieverViewHolder) holder;
                viewHolder.RecieverReaction.setImageResource(reaction[pos]);
                viewHolder.RecieverReaction.setVisibility(View.VISIBLE);
            }

            model.setFeeling(pos);
            FirebaseDatabase.getInstance().getReference().child("Messages").child(FirebaseAuth.getInstance().getUid()).child(FirebaseAuth.getInstance().getUid() + RecId).child(model.getMessageId()).setValue(model.getFeeling());

            FirebaseDatabase.getInstance().getReference().child("Messages").child(FirebaseAuth.getInstance().getUid()).child(RecId + FirebaseAuth.getInstance().getUid()).child(model.getMessageId()).setValue(model.getFeeling());

            return true; // true is closing popup, false is requesting a new selection
        });


        if (holder.getClass() == SenderViewHolder.class) {
            ((SenderViewHolder) holder).SenderMSg.setText(model.getMessage());
            Date date = new Date(model.getTimeStamp());
            SimpleDateFormat dateFormat = new SimpleDateFormat(ABBR_MONTH_DAY + " KK:mm aaa");
            String strDate = dateFormat.format(date);
            ((SenderViewHolder) holder).STimeStamp.setText(strDate);
            SenderViewHolder viewHolder = (SenderViewHolder) holder;
            viewHolder.itemView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent event) {
                    popup.onTouch(view, event);
                    return false;
                }
            });
            if (model.getFeeling() >= 1) {
                model.setFeeling(reaction[(int) model.getFeeling()]);
                viewHolder.SendReation.setVisibility(View.VISIBLE);
            } else {
                viewHolder.SendReation.setVisibility(View.GONE);
            }

        } else {
            ((RecieverViewHolder) holder).RecieverMSg.setText(model.getMessage());
            Date date = new Date(model.getTimeStamp());
            SimpleDateFormat dateFormat = new SimpleDateFormat(ABBR_MONTH_DAY + " KK:mm aa");
            String strDate = dateFormat.format(date);
            ((RecieverViewHolder) holder).rTimeStamp.setText(strDate);
            RecieverViewHolder viewHolder = (RecieverViewHolder) holder;
            viewHolder.itemView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    popup.onTouch(v, event);
                    return false;
                }
            });
            if (model.getFeeling() >= 1) {
                model.setFeeling(reaction[(int) model.getFeeling()]);
                viewHolder.RecieverReaction.setVisibility(View.VISIBLE);
            } else {
                viewHolder.RecieverReaction.setVisibility(View.GONE);
            }
        }
    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class SenderViewHolder extends RecyclerView.ViewHolder {
        TextView SenderMSg, STimeStamp;
        ImageView SendReation;

        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            SenderMSg = itemView.findViewById(R.id.SenderMessage);
            STimeStamp = itemView.findViewById(R.id.TimeStampSender);
            SendReation = itemView.findViewById(R.id.SendReation);
        }
    }


    public static class RecieverViewHolder extends RecyclerView.ViewHolder {
        TextView RecieverMSg, rTimeStamp;
        ImageView RecieverReaction;

        public RecieverViewHolder(@NonNull View itemView) {
            super(itemView);

            RecieverMSg = itemView.findViewById(R.id.RecievedMessage);
            rTimeStamp = itemView.findViewById(R.id.TimeStampReciever);
            RecieverReaction = itemView.findViewById(R.id.recieverReaction);
        }
    }

}
