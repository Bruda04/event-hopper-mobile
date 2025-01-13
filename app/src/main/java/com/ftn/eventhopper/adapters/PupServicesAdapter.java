package com.ftn.eventhopper.adapters;

import android.content.Context;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ftn.eventhopper.R;
import com.ftn.eventhopper.clients.ClientUtils;
import com.ftn.eventhopper.fragments.solutions.services.PupsServicesFragment;
import com.ftn.eventhopper.fragments.solutions.services.viewmodels.PupsServicesViewModel;
import com.ftn.eventhopper.shared.dtos.solutions.ServiceManagementDTO;
import com.ftn.eventhopper.shared.models.Service;
import com.ftn.eventhopper.shared.models.solutions.ProductStatus;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class PupServicesAdapter extends RecyclerView.Adapter<PupServicesAdapter.PupsServiceViewHolder> {
    ArrayList<ServiceManagementDTO> services;
    Context context;
    private final NavController navController;
    private final PupsServicesFragment fragment;
    private PupsServicesViewModel viewmodel;

    public PupServicesAdapter(Context context, ArrayList<ServiceManagementDTO> services, NavController navController, PupsServicesFragment fragment, PupsServicesViewModel viewmodel) {
        this.services = services;
        this.context = context;
        this.navController = navController;
        this.fragment = fragment;
        this.viewmodel = viewmodel;
    }

    @NonNull
    @Override
    public PupServicesAdapter.PupsServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PupsServiceViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_pups_service, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PupServicesAdapter.PupsServiceViewHolder holder, int position) {
        ServiceManagementDTO service = services.get(position);

        holder.serviceName.setText(service.getName());
        holder.serviceDescription.setText(service.getDescription());
        holder.servicePrice.setText(String.format("%.2f€", service.getPrice().getFinalPrice()));

        if (service.getStatus() == ProductStatus.PENDING) {
            holder.syncImage.setVisibility(View.VISIBLE);
        }

        ArrayList<String> pictures = new ArrayList<>(service.getPictures());
        if (pictures.size() == 0) {
            holder.serviceImage.setImageResource(R.drawable.baseline_image_not_supported_24);
        } else {
            String profilePictureUrl = String.format("%s/%s", ClientUtils.SERVICE_API_IMAGE_PATH, pictures.get(0));
            Glide.with(holder.itemView.getContext())
                    .load(profilePictureUrl)
                    .placeholder(R.drawable.baseline_image_placeholder_24)  // Optional: Placeholder
                    .error(R.drawable.baseline_image_not_supported_24)        // Optional: Error image
                    .into(holder.serviceImage);
        }

        if (service.getStatus() == ProductStatus.PENDING) {
            holder.editButton.setActivated(false);
            holder.viewMoreButton.setActivated(false);
            holder.deleteButton.setActivated(false);
            holder.deleteButton.setBackgroundColor(context.getResources().getColor(R.color.grey));
            holder.editButton.setBackgroundColor(context.getResources().getColor(R.color.grey));
            holder.viewMoreButton.setBackgroundColor(context.getResources().getColor(R.color.grey));
        } else {
            holder.viewMoreButton.setOnClickListener(v -> {
                Bundle bundle = new Bundle();
                bundle.putString("id", services.get(position).getId().toString());
                // Navigate to ServiceDetailsFragment
                navController.navigate(R.id.action_to_solution_page_fragment, bundle);
            });

            holder.deleteButton.setOnClickListener(v -> {
                setupDeleteDialog(position);
            });

            holder.editButton.setOnClickListener(v -> {
                Bundle bundle = new Bundle();
                bundle.putString("serviceId", services.get(position).getId().toString());
                bundle.putString("name", services.get(position).getName());
                bundle.putString("description", services.get(position).getDescription());
                bundle.putInt("duration", services.get(position).getDurationMinutes());
                bundle.putInt("reservationWindow", services.get(position).getReservationWindowDays());
                bundle.putInt("cancellationWindow", services.get(position).getCancellationWindowDays());
                bundle.putBoolean("visibility", services.get(position).isVisible());
                bundle.putBoolean("availability", services.get(position).isAvailable());
                bundle.putBoolean("autoAccept", services.get(position).isAutoAccept());
                bundle.putStringArrayList("eventTypes", new ArrayList<String>(services.get(position).getEventTypes()
                        .stream().map(e -> e.getId().toString())
                        .collect(Collectors.toCollection(ArrayList::new))
                ));
                bundle.putStringArrayList("pictures", new ArrayList<>(services.get(position).getPictures()));
                bundle.putString("categoryId", services.get(position).getCategory().getId().toString());


                navController.navigate(R.id.action_to_edit_service1, bundle);
            });
        }

    }

    private void setupDeleteDialog(int position) {
        MaterialAlertDialogBuilder confirmDialog = new MaterialAlertDialogBuilder(context);
        confirmDialog.setTitle("Delete service");
        confirmDialog.setMessage("Are you sure you want to delete this service?");
        confirmDialog.setPositiveButton("Yes", (dialog, which) -> {
            fragment.deleteService(services.get(position).getId());
        });
        confirmDialog.setNegativeButton("No", (dialog, which) -> {
        });
        confirmDialog.show();
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

        private final ImageView syncImage;
        public MaterialButton deleteButton;
        public MaterialButton editButton;
        public MaterialButton viewMoreButton;

        public PupsServiceViewHolder(@NonNull View itemView) {
            super(itemView);
            this.serviceName = itemView.findViewById(R.id.pups_service_card_title);
            this.serviceDescription = itemView.findViewById(R.id.pups_service_card_description);
            this.servicePrice = itemView.findViewById(R.id.pups_service_card_secondary);
            this.serviceImage = itemView.findViewById(R.id.pups_service_card_image);
            this.deleteButton = itemView.findViewById(R.id.pups_service_card_delete_button);
            this.editButton = itemView.findViewById(R.id.pups_service_card_edit_button);
            this.viewMoreButton = itemView.findViewById(R.id.pups_service_card_view_more_button);
            this.syncImage = itemView.findViewById(R.id.pups_service_card_title_icon);
        }
    }
}
