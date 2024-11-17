package com.ftn.eventhopper.adapters;

import android.content.Context;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.ftn.eventhopper.R;
import com.ftn.eventhopper.models.Service;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class PupServicesAdapter extends RecyclerView.Adapter<PupServicesAdapter.PupsServiceViewHolder> {
    ArrayList<Service> services;
    Context context;
    private final NavController navController; // Add NavController or Fragment
    private final Fragment fragment;

    public PupServicesAdapter(Context context, ArrayList<Service> services, NavController navController, Fragment fragment) {
        this.services = services;
        this.context = context;
        this.navController = navController; // Initialize NavController
        this.fragment = fragment;
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

        holder.deleteButton.setOnClickListener(v -> {
            services.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, services.size());
            Toast.makeText(v.getContext(), "Service deleted", Toast.LENGTH_SHORT).show();
        });

        holder.editButton.setOnClickListener(v -> {
            // Navigate to ServiceEditFragment
            if (navController != null) {
                // Option 1: Use NavController directly
                navController.navigate(R.id.action_to_service_edit_fragment);
            } else if (fragment != null) {
                // Option 2: Use Fragment to navigate (if no NavController passed)
                NavController navCtrl = androidx.navigation.Navigation.findNavController(fragment.requireView());
                navCtrl.navigate(R.id.action_to_service_edit_fragment);
            }
        });

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
        public MaterialButton deleteButton;
        public MaterialButton editButton;

        public PupsServiceViewHolder(@NonNull View itemView) {
            super(itemView);
            this.serviceName = itemView.findViewById(R.id.pups_service_card_title);
            this.serviceDescription = itemView.findViewById(R.id.pups_service_card_description);
            this.servicePrice = itemView.findViewById(R.id.pups_service_card_secondary);
            this.serviceImage = itemView.findViewById(R.id.pups_service_card_image);
            this.deleteButton = itemView.findViewById(R.id.pups_service_card_delete_button);
            this.editButton = itemView.findViewById(R.id.pups_service_card_edit_button);
        }
    }
}
