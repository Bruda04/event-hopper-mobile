package com.ftn.eventhopper.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ftn.eventhopper.R;
import com.ftn.eventhopper.models.Service;
import com.github.islamkhsh.CardSliderAdapter;

import java.util.ArrayList;

public class ServiceAdapter extends CardSliderAdapter<ServiceAdapter.ServiceViewHolder> {

    private ArrayList<Service> services;

    public ServiceAdapter(ArrayList<Service> services){
        this.services = services;
    }

    @Override
    public int getItemCount(){
        return services.size();
    }

    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_list_item, parent, false);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        return new ServiceViewHolder(view);
    }
    public void onBindViewHolder(int position, ServiceViewHolder holder) {
        Service service = services.get(position);

        // Bind service data to views within holder
        ImageView imageView = holder.itemView.findViewById(R.id.card_image);
        TextView titleView = holder.itemView.findViewById(R.id.card_title);
        TextView secondayView = holder.itemView.findViewById(R.id.card_secondary);
        TextView descriptionView = holder.itemView.findViewById(R.id.card_description);

    }

    @Override
    public void bindVH(ServiceViewHolder holder, int position) {
        Service service = services.get(position);

        // Bind service data to views within holder
        ImageView imageView = holder.itemView.findViewById(R.id.card_image);
        TextView titleView = holder.itemView.findViewById(R.id.card_title);
        TextView secondaryView = holder.itemView.findViewById(R.id.card_secondary);
        TextView descriptionView = holder.itemView.findViewById(R.id.card_description);

        // Assuming your `service` class has methods to retrieve this data
        // Set image, title, and description, and secondary text
        imageView.setImageDrawable(service.getImage());
        titleView.setText(service.getTitle());
        secondaryView.setText(service.getSecondary());
        descriptionView.setText(service.getDescription());
    }


    public static class ServiceViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView titleView;
        TextView secondaryView;
        TextView descriptionView;

        public ServiceViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.card_image);
            titleView = view.findViewById(R.id.card_title);
            secondaryView = view.findViewById(R.id.card_secondary);
            descriptionView = view.findViewById(R.id.card_description);
        }
    }
}