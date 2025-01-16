package com.ftn.eventhopper.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ftn.eventhopper.R;
import com.ftn.eventhopper.shared.dtos.messages.ChatMessageDTO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ChatMessagesAdapter extends RecyclerView.Adapter<ChatMessagesAdapter.MessageViewHolder> {
    private List<ChatMessageDTO> messages;

    public ChatMessagesAdapter(List<ChatMessageDTO> messages) {
        this.messages = messages;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        ChatMessageDTO message = messages.get(position);

        // Set message content and timestamp
        holder.content.setText(message.getContent());
        LocalDateTime timestamp = message.getTimestamp();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d.M.yyyy. H:mm");
        String formattedTimestamp = timestamp.format(formatter);
        holder.timestamp.setText(formattedTimestamp);

        // Apply conditional styling based on sender
        if (message.isSentByMe()) {
            holder.messageContainer.setBackgroundResource(R.drawable.message_background_sent);
            holder.content.setTextColor(holder.itemView.getResources().getColor(R.color.white));
            holder.timestamp.setTextColor(holder.itemView.getResources().getColor(R.color.white));
            holder.messageRow.setLayoutDirection(View.LAYOUT_DIRECTION_RTL); // Align to right
        } else {
            holder.messageContainer.setBackgroundResource(R.drawable.message_background_received);
            holder.content.setTextColor(holder.itemView.getResources().getColor(R.color.text_dark_blue));
            holder.timestamp.setTextColor(holder.itemView.getResources().getColor(R.color.text_dark_blue));
            holder.messageRow.setLayoutDirection(View.LAYOUT_DIRECTION_LTR); // Align to left
        }
        holder.messageContainer.setLayoutDirection(View.LAYOUT_DIRECTION_INHERIT);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView content;
        TextView timestamp;
        LinearLayout messageContainer;
        LinearLayout messageRow;

        public MessageViewHolder(View itemView) {
            super(itemView);
            content = itemView.findViewById(R.id.message_content);
            timestamp = itemView.findViewById(R.id.message_timestamp);
            messageRow = itemView.findViewById(R.id.message_row);
            messageContainer = itemView.findViewById(R.id.message_container);
        }
    }
}
