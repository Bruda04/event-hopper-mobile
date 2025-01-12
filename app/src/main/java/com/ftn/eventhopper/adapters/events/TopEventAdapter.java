package com.ftn.eventhopper.adapters.events;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ftn.eventhopper.R;
import com.ftn.eventhopper.clients.ClientUtils;
import com.ftn.eventhopper.shared.dtos.events.SimpleEventDTO;
import com.github.islamkhsh.CardSliderAdapter;

import java.util.ArrayList;

public class TopEventAdapter extends CardSliderAdapter<TopEventAdapter.EventViewHolder> {

    private ArrayList<SimpleEventDTO> events;

    public TopEventAdapter(ArrayList<SimpleEventDTO> events){
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
        SimpleEventDTO event = events.get(position);

        // Bind event data to views within holder
        ImageView imageView = holder.itemView.findViewById(R.id.card_image);
        TextView titleView = holder.itemView.findViewById(R.id.card_title);
        TextView secondayView = holder.itemView.findViewById(R.id.card_secondary);
        TextView descriptionView = holder.itemView.findViewById(R.id.card_description);

    }


    @Override
    public void bindVH(EventViewHolder holder, int position) {
        SimpleEventDTO event = events.get(position);

        ImageView imageView = holder.itemView.findViewById(R.id.card_image);
        TextView titleView = holder.itemView.findViewById(R.id.card_title);
        TextView secondaryView = holder.itemView.findViewById(R.id.card_secondary);
        TextView descriptionView = holder.itemView.findViewById(R.id.card_description);

        titleView.setText(event.getName());
        secondaryView.setText(events.get(position).getLocation().getAddress() + " ,"+ events.get(position).getLocation().getCity() );
        imageView.setImageDrawable(Drawable.createFromPath(events.get(position).getPicture()));

        Glide.with(holder.imageView.getContext())
                .load(String.format("%s/%s", ClientUtils.SERVICE_API_IMAGE_PATH, events.get(position).getPicture()))
                .placeholder(R.drawable.baseline_image_placeholder_24) // Prikaz slike dok se učitava
                .error(R.drawable.baseline_image_not_supported_24) // Prikaz slike u slučaju greške
                .into(holder.imageView);
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