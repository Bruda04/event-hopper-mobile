package com.ftn.eventhopper.adapters;
import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ftn.eventhopper.R;
import com.ftn.eventhopper.filters.MinMaxInputFilter;
import com.ftn.eventhopper.fragments.budgets.viewmodels.BudgetingViewModel;
import com.ftn.eventhopper.shared.dtos.budgets.BudgetItemManagementDTO;
import com.ftn.eventhopper.shared.dtos.budgets.BudgetManagementDTO;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Objects;

import lombok.Getter;

public class BudgetItemsAdapter extends RecyclerView.Adapter<BudgetItemsAdapter.BudgetItemViewHolder>{


    private BudgetManagementDTO budget;
    private Context context;
    @Getter
    private ArrayList<BudgetItemManagementDTO> items;
    private BudgetingViewModel viewModel;

    public BudgetItemsAdapter(Context context,
                              BudgetManagementDTO budget,
                              BudgetingViewModel viewModel) {

        this.budget = budget;
        this.context = context;
        this.items = new ArrayList<>(budget.getBudgetItems());
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public BudgetItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BudgetItemsAdapter.BudgetItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_budget_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BudgetItemViewHolder holder, int position) {
        holder.categoryName.setText(items.get(position).getCategory().getName());

        Objects.requireNonNull(holder.amount.getEditText())
                .setFilters(new InputFilter[]{new MinMaxInputFilter(0.0, 10000000.0)});
        Objects.requireNonNull(holder.amount.getEditText()).setText(String.format("%.2f", items.get(position).getAmount()));
        TextWatcher priceWatcher = setupAmountWatcher(holder.amount, position);
        (holder.amount.getEditText()).addTextChangedListener(priceWatcher);


        if (this.items.get(position).isDeletable()) {
            holder.deleteButton.setOnClickListener(v -> {
                    setupDeleteDialog(position);
            });
            holder.deleteButton.setEnabled(true);
            holder.deleteButton.setBackgroundColor(context.getResources().getColor(R.color.md_theme_error));
        } else {
            holder.deleteButton.setEnabled(false);
            holder.deleteButton.setBackgroundColor(context.getResources().getColor(R.color.grey));

        }

        if (!this.items.get(position).getPurchasedProducts().isEmpty()) {
            holder.productsButton.setOnClickListener(v -> {
            });
            holder.productsButton.setEnabled(true);
            holder.productsButton.setBackgroundColor(context.getResources().getColor(R.color.darker_blue));

        } else {
            holder.productsButton.setEnabled(false);
            holder.productsButton.setBackgroundColor(context.getResources().getColor(R.color.grey));

        }


    }

    private TextWatcher setupAmountWatcher(TextInputLayout amountInputLayout, int position) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                // Get the values from the inputs
                String amount = Objects.requireNonNull(amountInputLayout.getEditText()).getText().toString().trim();

                // Ensure values are not empty before parsing
                double amountDouble = parseDouble(amount);

                items.get(position).setAmount(amountDouble);
                Log.d("BudgetItemsAdapter", "Amount changed to: " + amountDouble);
            }
        };
    }

    private double parseDouble(String input) {
        if (input.isEmpty()) {
            return 0;
        }

        // Normalize input by replacing ',' with '.'
        input = input.replace(',', '.');

        // Handle edge cases like input being only '.' or ',' or invalid numbers
        try {
            return Double.parseDouble(input);
        } catch (NumberFormatException e) {
            return 0; // Return 0 if input cannot be parsed
        }
    }

    private void setupDeleteDialog(int position) {
        MaterialAlertDialogBuilder approveDialog = new MaterialAlertDialogBuilder(context);
        approveDialog.setTitle("Delete Budget Item");
        approveDialog.setMessage("Are you sure you want to Delete this budget item?");
        approveDialog.setPositiveButton("Delete", (dialog, which) -> {
            this.viewModel.removeBudgetItem(this.items.get(position).getId());
        });
        approveDialog.setNegativeButton("Cancel", (dialog, which) -> {
        });
        approveDialog.show();
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    public class BudgetItemViewHolder extends RecyclerView.ViewHolder {

    private final TextView categoryName;
    private final TextInputLayout amount;
    public MaterialButton productsButton;
    public MaterialButton deleteButton;

    public BudgetItemViewHolder(@NonNull View itemView){
        super(itemView);
        this.categoryName = itemView.findViewById(R.id.category_name);
        this.amount = itemView.findViewById(R.id.amount_layout);
        this.productsButton = itemView.findViewById(R.id.products_button);
        this.deleteButton = itemView.findViewById(R.id.delete_button);
    }
}
}
