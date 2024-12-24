package com.ftn.eventhopper.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ftn.eventhopper.R;
import com.ftn.eventhopper.shared.models.Event;
import com.github.islamkhsh.CardSliderAdapter;

import java.util.ArrayList;

public class TopEventAdapter extends CardSliderAdapter<TopEventAdapter.EventViewHolder> {

    private ArrayList<Event> events;

    public TopEventAdapter(ArrayList<Event> events){
        this.events = events;
    }

    @Override
    public int getItemCount(){
        return events.size();
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_list_item, parent, false);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        return new EventViewHolder(view);
    }
    public void onBindViewHolder(int position, EventViewHolder holder) {
        Event event = events.get(position);

        // Bind event data to views within holder
        ImageView imageView = holder.itemView.findViewById(R.id.card_image);
        TextView titleView = holder.itemView.findViewById(R.id.card_title);
        TextView secondayView = holder.itemView.findViewById(R.id.card_secondary);
        TextView descriptionView = holder.itemView.findViewById(R.id.card_description);

    }


    @Override
    public void bindVH(EventViewHolder holder, int position) {
        Event event = events.get(position);

        // Bind event data to views within holder
        ImageView imageView = holder.itemView.findViewById(R.id.card_image);
        TextView titleView = holder.itemView.findViewById(R.id.card_title);
        TextView secondaryView = holder.itemView.findViewById(R.id.card_secondary);
        TextView descriptionView = holder.itemView.findViewById(R.id.card_description);

        // Assuming your `Event` class has methods to retrieve this data
        // Set image, title, and description, and secondary text
        imageView.setImageDrawable(event.getImage()); // Postavite Drawable
        titleView.setText(event.getTitle());
        secondaryView.setText(event.getSecondary());
        descriptionView.setText(event.getDescription());
    }


    public static class EventViewHolder extends RecyclerView.ViewHolder {

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