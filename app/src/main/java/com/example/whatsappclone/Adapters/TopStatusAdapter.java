/*

package com.example.whatsappclone.Adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whatsappclone.Modules.UserStatus;
import com.example.whatsappclone.R;
import com.example.whatsappclone.databinding.ItemStatusViewBinding;

import java.util.ArrayList;

public class TopStatusAdapter extends RecyclerView.Adapter<TopStatusAdapter.ViewHolder> {

    private final Context context;
    private final ArrayList<UserStatus> userStatuses;

    public TopStatusAdapter(Context context, ArrayList<UserStatus> userStatuses) {
        this.context = context;
        this.userStatuses = userStatuses;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_status_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        UserStatus userStatus = userStatuses.get(position);
        /* holder.binding.circularStatusView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<MyStory> myStories = new ArrayList<>();
                for (Status status : userStatus.getStatusArrayList()) {
                    myStories.add(new MyStory(status.getImageUrl()));
                }
                StoryView.Builder builder = new StoryView.Builder((StatusFragment)context));
                builder.setStoriesList(myStories);
                builder.setStoryDuration(5000);
                builder.setTitleText(userStatus.getStatusName());
                builder.setSubtitleText("");
                builder.setTitleLogoUrl(userStatus.getStatusProfileImage());
                builder.setStoryClickListeners(new StoryClickListeners() {
                    @Override
                    public void onDescriptionClickListener(int position) {
                        //your action
                    }

                    @Override
                    public void onTitleIconClickListener(int position) {
                        //your action
                    }
                });
                builder.build();
                builder.show();// Required
// Default is 2000 Millis (2 Seconds)
// Default is Hidden
// Default is Hidden
// Default is Hidden
// Optional Listeners
// Must be called before calling show method

            }
        });

    }

    @Override
    public int getItemCount() {
        return userStatuses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemStatusViewBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemStatusViewBinding.bind(itemView);
        }
    }
}
*/