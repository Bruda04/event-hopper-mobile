package com.ftn.eventhopper.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ftn.eventhopper.R;
import com.ftn.eventhopper.shared.models.Event;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    ArrayList<Event> events;
    Context context;
    private final Fragment fragment;

    public EventAdapter(Context context, ArrayList<Event> events, Fragment fragment){
     this.events = events;
     this.context = context;
     this.fragment = fragment;
    }

    @NonNull
    @Override
    public EventAdapter.EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        return new EventAdapter.EventViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull EventAdapter.EventViewHolder holder, int position) {

        holder.titleView.setText(events.get(position).getTitle());
        holder.descriptionView.setText(events.get(position).getDescription());
        holder.secondaryView.setText(events.get(position).getSecondary());
        holder.imageView.setImageDrawable(events.get(position).getImage());
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public class EventViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView titleView;
        TextView secondaryView;
        TextView descriptionView;

        public EventViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.card_image);
            titleView = view.findViewById(R.id.card_title);
            secondaryView = view.findViewById(R.id.card_secondary);
            descriptionView = view.findViewById(R.id.card_description);
        }
    }


}
