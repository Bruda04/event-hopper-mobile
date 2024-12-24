package com.ftn.eventhopper.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ftn.eventhopper.R;
import com.ftn.eventhopper.shared.dtos.events.SimpleEventDTO;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    ArrayList<SimpleEventDTO> events;
    Context context;
    private final Fragment fragment;

    public EventAdapter(Context context, ArrayList<SimpleEventDTO> events, Fragment fragment){
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

        holder.titleView.setText(events.get(position).getName());
        holder.descriptionView.setText(events.get(position).getDescription());
        holder.secondaryView.setText(events.get(position).getLocation().getAddress() + " ,"+ events.get(position).getLocation().getCity() );
        holder.imageView.setImageDrawable(Drawable.createFromPath(events.get(position).getPicture()));

        String imageUrl = events.get(position).getPicture();
        Glide.with(holder.imageView.getContext())
                .load(imageUrl)
//                .placeholder(R.drawable.placeholder_image) // Prikaz slike dok se učitava
//                .error(R.drawable.error_image) // Prikaz slike u slučaju greške
                .into(holder.imageView);
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
