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

import java.util.ArrayList;

public class PupServicesAdapter extends RecyclerView.Adapter<PupServicesAdapter.PupsServiceViewHolder> {
    ArrayList<Service> services;

    public PupServicesAdapter(ArrayList<Service> services) {
        this.services = services;
    }

    @NonNull
    @Override
    public PupServicesAdapter.PupsServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PupsServiceViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_pups_service, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PupServicesAdapter.PupsServiceViewHolder holder, int position) {
        holder.serviceName.setText(services.get(position).getTitle());
        holder.serviceDescription.setText(services.get(position).getDescription());
        holder.servicePrice.setText("Price: " + services.get(position).getSecondary());
        holder.serviceImage.setImageDrawable(services.get(position).getImage());
    }

    @Override
    public int getItemCount() {
        return services.size();
    }

    public class PupsServiceViewHolder extends RecyclerView.ViewHolder {
        private final TextView serviceName;
        private final TextView serviceDescription;
        private final TextView servicePrice;
        private final ImageView serviceImage;

        public PupsServiceViewHolder(@NonNull View itemView) {
            super(itemView);
            this.serviceName = itemView.findViewById(R.id.pups_service_card_title);
            this.serviceDescription = itemView.findViewById(R.id.pups_service_card_description);
            this.servicePrice = itemView.findViewById(R.id.pups_service_card_secondary);
            this.serviceImage = itemView.findViewById(R.id.pups_service_card_image);
        }
    }
}
