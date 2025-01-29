package com.ftn.eventhopper.adapters.events;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ftn.eventhopper.R;
import com.ftn.eventhopper.clients.ClientUtils;
import com.ftn.eventhopper.fragments.events.EventPageFragment;
import com.ftn.eventhopper.shared.dtos.events.SimpleEventDTO;
import com.google.android.material.button.MaterialButton;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavHost;
import androidx.navigation.fragment.NavHostFragment;
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
        holder.secondaryView.setText(String.format("%s, %s", events.get(position).getLocation().getAddress(), events.get(position).getLocation().getCity()));
        //holder.imageView.setImageDrawable(Drawable.createFromPath(events.get(position).getPicture()));

        //String imageUrl = events.get(position).getPicture();

        Glide.with(holder.imageView.getContext())
                .load(String.format("%s/%s", ClientUtils.SERVICE_API_IMAGE_PATH, events.get(position).getPicture()))
                .placeholder(R.drawable.baseline_image_placeholder_24) // Prikaz slike dok se učitava
                .error(R.drawable.baseline_image_not_supported_24) // Prikaz slike u slučaju greške
                .into(holder.imageView);

        holder.viewMoreButton.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("event_id", events.get(position).getId().toString());
            NavController navController = NavHostFragment.findNavController(this.fragment);
            navController.navigate(R.id.action_to_event_page, bundle);
        });
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
        public MaterialButton viewMoreButton;

        public EventViewHolder(View view) {
            super(view);
            this.viewMoreButton = view.findViewById(R.id.card_button);

            imageView = view.findViewById(R.id.card_image);
            titleView = view.findViewById(R.id.card_title);
            secondaryView = view.findViewById(R.id.card_secondary);
            descriptionView = view.findViewById(R.id.card_description);
        }
    }


}
