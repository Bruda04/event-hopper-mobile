package com.ftn.eventhopper.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.ftn.eventhopper.R;
import com.ftn.eventhopper.shared.dtos.notifications.NotificationDTO;
import com.ftn.eventhopper.shared.models.Notification;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.NotificationViewHolder>{


    public ArrayList<NotificationDTO> notifications;
    Context context;
    private final Fragment fragment;

    public NotificationsAdapter(Context context, ArrayList<NotificationDTO> notifications, Fragment fragment) {
        this.notifications = notifications;
        this.context = context;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public NotificationsAdapter.NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NotificationsAdapter.NotificationViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.single_notification, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationsAdapter.NotificationViewHolder holder, int position) {

        holder.notificationTitle.setText(notifications.get(position).getContent());
        holder.notificationTimestamp.setText(notifications.get(position).getTimestamp().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));


    }


    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public class NotificationViewHolder extends RecyclerView.ViewHolder {
        private final TextView notificationTitle;
        private final TextView notificationText;
        private final TextView notificationTimestamp;


        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            this.notificationTitle = itemView.findViewById(R.id.notification_title);
            this.notificationText = itemView.findViewById(R.id.notification_content);
            this.notificationTimestamp = itemView.findViewById(R.id.notification_timestamp);
        }
    }
}
