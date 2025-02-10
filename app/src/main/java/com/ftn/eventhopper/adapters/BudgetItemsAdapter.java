package com.ftn.eventhopper.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ftn.eventhopper.R;
import com.ftn.eventhopper.shared.dtos.budgets.BudgetItemManagementDTO;
import com.ftn.eventhopper.shared.dtos.budgets.BudgetManagementDTO;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

public class BudgetItemsAdapter extends RecyclerView.Adapter<BudgetItemsAdapter.BudgetItemViewHolder>{


    private BudgetManagementDTO budget;
    private Context context;
    private ArrayList<BudgetItemManagementDTO> items;

    public BudgetItemsAdapter(Context context,
                                      BudgetManagementDTO budget){

        this.budget = budget;
        this.context = context;
        this.items = new ArrayList<>(budget.getBudgetItems());
    }

    @NonNull
    @Override
    public BudgetItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BudgetItemsAdapter.BudgetItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_budget_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BudgetItemViewHolder holder, int position) {
        holder.categoryName.setText(items.get(position).getCategory().getName());
        holder.amount.getEditText().setText(String.valueOf(items.get(position).getAmount()));


        holder.deleteButton.setOnClickListener(v -> {
            setupDeleteDialog(position);
        });


    }

    private void setupDeleteDialog(int position) {
        MaterialAlertDialogBuilder approveDialog = new MaterialAlertDialogBuilder(context);
        approveDialog.setTitle("Delete comment");
        approveDialog.setMessage("Are you sure you want to Delete this comment?");
        approveDialog.setPositiveButton("Delete", (dialog, which) -> {
            this.items.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, this.items.size());
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
