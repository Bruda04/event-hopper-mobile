package com.ftn.eventhopper.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.ftn.eventhopper.R;
import com.ftn.eventhopper.models.Category;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class AdminsCategoriesAdapter extends RecyclerView.Adapter<AdminsCategoriesAdapter.AdminsCategoryViewHolder> {
    ArrayList<Category> categories;
    Context context;
    private final Fragment fragment;

    public AdminsCategoriesAdapter(Context context, ArrayList<Category> categories, Fragment fragment) {
        this.categories = categories;
        this.context = context;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public AdminsCategoriesAdapter.AdminsCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AdminsCategoriesAdapter.AdminsCategoryViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_category, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdminsCategoriesAdapter.AdminsCategoryViewHolder holder, int position) {
        holder.categoryName.setText(categories.get(position).getName());
        holder.categoryDescription.setText(categories.get(position).getDescription());

        if (categories.get(position).isDeletable()) {
            holder.deleteButton.setOnClickListener(v -> {
                MaterialAlertDialogBuilder confirmDialog = new MaterialAlertDialogBuilder(v.getContext());
                confirmDialog.setTitle("Delete category");
                confirmDialog.setMessage("Are you sure you want to delete this category?");
                confirmDialog.setPositiveButton("Yes", (dialog, which) -> {
                    categories.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, categories.size());
                });
                confirmDialog.setNegativeButton("No", (dialog, which) -> {
                });
                confirmDialog.show();
            });
        } else {
            holder.deleteButton.setActivated(false);
            holder.deleteButton.setBackgroundColor(context.getResources().getColor(R.color.md_theme_onSurfaceVariant));
        }

        holder.editButton.setOnClickListener(v -> {
                View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_category_creation, null);

                TextInputEditText nameInput = dialogView.findViewById(R.id.category_name);
                TextInputEditText descriptionInput = dialogView.findViewById(R.id.category_description);
                nameInput.setText(categories.get(position).getName());
                descriptionInput.setText(categories.get(position).getDescription());

                MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(context);
                dialog.setTitle("Edit category");
                dialog.setView(dialogView);
                dialog.setPositiveButton("Save", (dialogInterface, i) -> {
                });
                dialog.setNegativeButton("Cancel", (dialogInterface, i) -> {
                });
                dialog.show();
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }


    public class AdminsCategoryViewHolder extends RecyclerView.ViewHolder {
        private final TextView categoryName;
        private final TextView categoryDescription;
        public MaterialButton deleteButton;
        public MaterialButton editButton;

        public AdminsCategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            this.categoryName = itemView.findViewById(R.id.admins_category_card_title);
            this.categoryDescription = itemView.findViewById(R.id.admins_category_card_description);
            this.deleteButton = itemView.findViewById(R.id.admins_category_card_delete_button);
            this.editButton = itemView.findViewById(R.id.admins_category_card_edit_button);
        }
    }

}
