package com.ftn.eventhopper.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ftn.eventhopper.R;
import com.ftn.eventhopper.clients.ClientUtils;
import com.ftn.eventhopper.fragments.messages.ConversationsListFragment;
import com.ftn.eventhopper.shared.dtos.comments.SimpleCommentDTO;
import com.ftn.eventhopper.shared.dtos.messages.ConversationPreviewDTO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ConversationsAdapter extends RecyclerView.Adapter<ConversationsAdapter.ConversationViewHolder> {
    private List<ConversationPreviewDTO> conversations;
    private ConversationsListFragment conversationsListFragment;

    public ConversationsAdapter(List<ConversationPreviewDTO> conversations, ConversationsListFragment conversationsListFragment) {
        this.conversations = conversations;
        this.conversationsListFragment = conversationsListFragment;
    }

    @NonNull
    @Override
    public ConversationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.conversation_item, parent, false);
        return new ConversationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ConversationViewHolder holder, int position) {
        ConversationPreviewDTO conversation = conversations.get(position);

        if (conversation.getProfilePictureUrl() == null || conversation.getProfilePictureUrl().isEmpty()) {
            holder.profilePicture.setImageDrawable(ContextCompat.getDrawable(conversationsListFragment.requireContext(), R.drawable.profile_pic_temp));
        } else {
            String profilePictureUrl = String.format("%s/%s", ClientUtils.SERVICE_API_IMAGE_PATH, conversation.getProfilePictureUrl());
            Glide.with(holder.itemView.getContext())
                    .load(profilePictureUrl)
                    .circleCrop()
                    .placeholder(R.drawable.baseline_image_placeholder_24)  // Optional: Placeholder
                    .error(R.drawable.baseline_image_not_supported_24)        // Optional: Error image
                    .into(holder.profilePicture);
        }
        holder.name.setText(String.format("%s %s",conversation.getName(), conversation.getSurname()));
        holder.username.setText(conversation.getUsername());
        holder.lastMessage.setText(conversation.getLastMessage());
        LocalDateTime timestamp = conversation.getLastMessageTimestamp();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d.M.yyyy. H:mm");
        String formattedTimestamp = timestamp.format(formatter);
        holder.lastMessageTimestamp.setText(formattedTimestamp);

        holder.itemView.setOnClickListener(v -> {
            conversationsListFragment.openSingleChat(conversation);
        });
    }

    @Override
    public int getItemCount() {
        return conversations.size();
    }

    public static class ConversationViewHolder extends RecyclerView.ViewHolder {
        ImageView profilePicture;
        TextView username;
        TextView name;
        TextView lastMessage;
        TextView lastMessageTimestamp;

        public ConversationViewHolder(View itemView) {
            super(itemView);
            profilePicture = itemView.findViewById(R.id.profile_picture);
            username = itemView.findViewById(R.id.username);
            name = itemView.findViewById(R.id.name);
            lastMessage = itemView.findViewById(R.id.last_message);
            lastMessageTimestamp = itemView.findViewById(R.id.last_message_timestamp);
        }
    }
}
