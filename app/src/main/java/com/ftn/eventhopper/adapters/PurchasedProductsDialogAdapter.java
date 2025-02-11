package com.ftn.eventhopper.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.ftn.eventhopper.R;

import com.ftn.eventhopper.shared.dtos.solutions.SimpleProductDTO;


import java.util.ArrayList;
import java.util.Collection;


public class PurchasedProductsDialogAdapter extends RecyclerView.Adapter<PurchasedProductsDialogAdapter.PurchasedProductViewHolder> {
    private Context context;
    private ArrayList<SimpleProductDTO> products;
    private NavController navController;
    private AlertDialog dialog;

    public PurchasedProductsDialogAdapter(Context context,
                                          Collection<SimpleProductDTO> products,
                                          NavController navController,
                                          AlertDialog dialog
    ) {

        this.context = context;
        this.products = new ArrayList<>(products);
        this.navController = navController;
        this.dialog = dialog;
    }

    @NonNull
    @Override
    public PurchasedProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PurchasedProductsDialogAdapter.PurchasedProductViewHolder(View.inflate(context, R.layout.card_purchased_product, null));
    }

    @Override
    public void onBindViewHolder(@NonNull PurchasedProductViewHolder holder, int position) {
        holder.productName.setText(products.get(position).getName());
        String description = products.get(position).getDescription();
        if (description == null || description.isEmpty()) {
            holder.productDescription.setVisibility(View.GONE);
        } else if (description.length() > 100) {
            description = description.substring(0, 100) + "...";
        }
        holder.productDescription.setText(description);

        holder.detailsButton.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("id", products.get(position).getId().toString());
            navController.navigate(R.id.action_to_solution_page, bundle);
            dialog.dismiss();
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class PurchasedProductViewHolder extends RecyclerView.ViewHolder {

        private final TextView productName;
        private final TextView productDescription;
        private final Button detailsButton;


        public PurchasedProductViewHolder(@NonNull View itemView){
            super(itemView);
            this.productName = itemView.findViewById(R.id.product_name);
            this.productDescription = itemView.findViewById(R.id.product_description);
            this.detailsButton = itemView.findViewById(R.id.details_button);
        }
    }
}
