package com.ftn.eventhopper.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.ftn.eventhopper.R;
import com.ftn.eventhopper.fragments.solutions.prices.viewmodels.PUPPricesManagementViewModel;
import com.ftn.eventhopper.shared.dtos.eventTypes.SimpleEventTypeDTO;
import com.ftn.eventhopper.shared.dtos.prices.PriceManagementDTO;
import com.ftn.eventhopper.shared.models.Service;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

public class PupPricesAdapter extends RecyclerView.Adapter<PupPricesAdapter.PriceManagementViewHolder>{
    ArrayList<PriceManagementDTO> prices;
    Context context;
    private final Fragment fragment;
    private final PUPPricesManagementViewModel viewmodel;

    public PupPricesAdapter(Context context, ArrayList<PriceManagementDTO> prices, Fragment fragment, PUPPricesManagementViewModel viewModel) {
        this.prices = prices;
        this.context = context;
        this.fragment = fragment;
        this.viewmodel = viewModel;
    }


    @NonNull
    @Override
    public PupPricesAdapter.PriceManagementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PupPricesAdapter.PriceManagementViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.price_management_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PupPricesAdapter.PriceManagementViewHolder holder, int position) {
        holder.productName.setText(prices.get(position).getProductName());
        holder.basePrice.setText(String.format("Base price: %.2f€", prices.get(position).getBasePrice()));
        holder.discount.setText(String.format("Discount: %.2f%%", prices.get(position).getDiscount()));
        holder.finalPrice.setText(String.format("Final price: %.2f€", prices.get(position).getFinalPrice()));

        holder.editButton.setOnClickListener(v -> {
            setupEditDialog(position);
        });
    }


    @Override
    public int getItemCount() {
        return prices.size();
    }
    private void setupEditDialog(int position) {
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_price_edit, null);

        TextInputLayout basePriceInput = dialogView.findViewById(R.id.base_price_layout);
        TextInputLayout discountInput = dialogView.findViewById(R.id.discount_layout);

        Objects.requireNonNull(basePriceInput.getEditText()).setText(String.format("%.2f", prices.get(position).getBasePrice()));
        Objects.requireNonNull(discountInput.getEditText()).setText(String.format("%.2f",prices.get(position).getDiscount()));

        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(context);
        dialogBuilder.setTitle("Edit price for " + prices.get(position).getProductName());
        dialogBuilder.setView(dialogView);
        dialogBuilder.setPositiveButton("Save", (dialogInterface, i) -> {
        });
        dialogBuilder.setNegativeButton("Cancel", (dialogInterface, i) -> {
        });
        AlertDialog editDialog = dialogBuilder.create();
        editDialog.show();
        editDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v1 -> {
            if (basePriceInput.getEditText().getText().toString().trim().isEmpty()) {
                basePriceInput.setError("Base price required");
            } else {
                basePriceInput.setError(null);
            }

            if (discountInput.getEditText().getText().toString().trim().isEmpty()) {
                discountInput.setError("Discount is required");
            } else {
                discountInput.setError(null);
            }

            double basePriceValue = Double.parseDouble(basePriceInput.getEditText().getText().toString().trim());
            double discountValue = Double.parseDouble(discountInput.getEditText().getText().toString().trim());

            if (basePriceInput.getError() == null && discountInput.getError() == null) {
                viewmodel.editPrice(
                        prices.get(position).getProductId(),
                        basePriceValue,
                        discountValue
                );

                editDialog.dismiss();
            }
        });
    }

    public class PriceManagementViewHolder extends RecyclerView.ViewHolder {
        private final TextView productName;
        private final TextView basePrice;
        private final TextView discount;
        private final TextView finalPrice;
        public MaterialButton editButton;

        public PriceManagementViewHolder(@NonNull View itemView) {
            super(itemView);
            this.productName = itemView.findViewById(R.id.price_management_product_name);
            this.basePrice = itemView.findViewById(R.id.price_management_base_price);
            this.discount = itemView.findViewById(R.id.price_management_discount);
            this.finalPrice = itemView.findViewById(R.id.price_management_final_price);
            this.editButton = itemView.findViewById(R.id.price_management_edit_button);
        }
    }
}
