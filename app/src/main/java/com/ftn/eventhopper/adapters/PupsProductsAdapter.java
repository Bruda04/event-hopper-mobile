package com.ftn.eventhopper.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ftn.eventhopper.R;
import com.ftn.eventhopper.clients.ClientUtils;
import com.ftn.eventhopper.fragments.solutions.products.PupsProductsFragment;
import com.ftn.eventhopper.fragments.solutions.products.viewmodels.PupsProductsViewModel;
import com.ftn.eventhopper.shared.dtos.solutions.ProductForManagementDTO;
import com.ftn.eventhopper.shared.models.solutions.ProductStatus;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class PupsProductsAdapter extends RecyclerView.Adapter<PupsProductsAdapter.PupsProductViewHolder> {
    ArrayList<ProductForManagementDTO> products;
    Context context;
    private final NavController navController;
    private final PupsProductsFragment fragment;
    private PupsProductsViewModel viewmodel;

    public PupsProductsAdapter(Context context, ArrayList<ProductForManagementDTO> products, NavController navController, PupsProductsFragment fragment, PupsProductsViewModel viewmodel) {
        this.products = products;
        this.context = context;
        this.navController = navController;
        this.fragment = fragment;
        this.viewmodel = viewmodel;
    }

    @NonNull
    @Override
    public PupsProductsAdapter.PupsProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PupsProductsAdapter.PupsProductViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_pups_product, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PupsProductsAdapter.PupsProductViewHolder holder, int position) {
        ProductForManagementDTO product = products.get(position);

        holder.productName.setText(product.getName());
        holder.productDescription.setText(product.getDescription());
        holder.productPrice.setText(String.format("%.2f€", product.getPrice().getFinalPrice()));

        if (product.getStatus() == ProductStatus.PENDING) {
            holder.syncImage.setVisibility(View.VISIBLE);
        }

        ArrayList<String> pictures = new ArrayList<>(product.getPictures());
        if (pictures.size() == 0) {
            holder.productImage.setImageResource(R.drawable.baseline_image_not_supported_24);
        } else {
            String profilePictureUrl = String.format("%s/%s", ClientUtils.SERVICE_API_IMAGE_PATH, pictures.get(0));
            Glide.with(holder.itemView.getContext())
                    .load(profilePictureUrl)
                    .placeholder(R.drawable.baseline_image_placeholder_24)  // Optional: Placeholder
                    .error(R.drawable.baseline_image_not_supported_24)        // Optional: Error image
                    .into(holder.productImage);
        }

        if (product.getStatus() == ProductStatus.PENDING) {
            holder.editButton.setActivated(false);
            holder.viewMoreButton.setActivated(false);
            holder.deleteButton.setActivated(false);
            holder.deleteButton.setBackgroundColor(context.getResources().getColor(R.color.grey));
            holder.editButton.setBackgroundColor(context.getResources().getColor(R.color.grey));
            holder.viewMoreButton.setBackgroundColor(context.getResources().getColor(R.color.grey));
        } else {
            holder.viewMoreButton.setOnClickListener(v -> {
                Bundle bundle = new Bundle();
                bundle.putString("id", products.get(position).getId().toString());
                // Navigate to ProductDetailsFragment
                navController.navigate(R.id.action_to_solution_page_fragment, bundle);
            });

            holder.deleteButton.setOnClickListener(v -> {
                setupDeleteDialog(position);
            });

            holder.editButton.setOnClickListener(v -> {
                Bundle bundle = new Bundle();
                bundle.putString("productId", products.get(position).getId().toString());
                bundle.putString("name", products.get(position).getName());
                bundle.putString("description", products.get(position).getDescription());
                bundle.putBoolean("visibility", products.get(position).isVisible());
                bundle.putBoolean("availability", products.get(position).isAvailable());
                bundle.putStringArrayList("eventTypes", new ArrayList<String>(products.get(position).getEventTypes()
                        .stream().map(e -> e.getId().toString())
                        .collect(Collectors.toCollection(ArrayList::new))
                ));
                bundle.putStringArrayList("pictures", new ArrayList<>(products.get(position).getPictures()));
                bundle.putString("categoryId", products.get(position).getCategory().getId().toString());


                navController.navigate(R.id.action_to_edit_product1, bundle);
            });
        }

    }

    private void setupDeleteDialog(int position) {
        MaterialAlertDialogBuilder confirmDialog = new MaterialAlertDialogBuilder(context);
        confirmDialog.setTitle("Delete product");
        confirmDialog.setMessage("Are you sure you want to delete this product?");
        confirmDialog.setPositiveButton("Yes", (dialog, which) -> {
            fragment.deleteProduct(products.get(position).getId());
        });
        confirmDialog.setNegativeButton("No", (dialog, which) -> {
        });
        confirmDialog.show();
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class PupsProductViewHolder extends RecyclerView.ViewHolder {
        private final TextView productName;
        private final TextView productDescription;
        private final TextView productPrice;
        private final ImageView productImage;
        private final ImageView syncImage;
        public MaterialButton deleteButton;
        public MaterialButton editButton;
        public MaterialButton viewMoreButton;

        public PupsProductViewHolder(@NonNull View itemView) {
            super(itemView);
            this.productName = itemView.findViewById(R.id.pups_product_card_title);
            this.productDescription = itemView.findViewById(R.id.pups_product_card_description);
            this.productPrice = itemView.findViewById(R.id.pups_product_card_secondary);
            this.productImage = itemView.findViewById(R.id.pups_product_card_image);
            this.deleteButton = itemView.findViewById(R.id.pups_product_card_delete_button);
            this.editButton = itemView.findViewById(R.id.pups_product_card_edit_button);
            this.viewMoreButton = itemView.findViewById(R.id.pups_product_card_view_more_button);
            this.syncImage = itemView.findViewById(R.id.pups_product_card_title_icon);
        }
    }
}
