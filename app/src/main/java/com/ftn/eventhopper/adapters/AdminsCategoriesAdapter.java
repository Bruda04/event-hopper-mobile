package com.ftn.eventhopper.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import com.ftn.eventhopper.R;
import com.ftn.eventhopper.fragments.categories.viewmodels.AdminsCategoriesManagementViewModel;
import com.ftn.eventhopper.shared.dtos.categories.CategoryDTO;
import com.ftn.eventhopper.shared.dtos.eventTypes.SimpleEventTypeDTO;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

public class AdminsCategoriesAdapter extends RecyclerView.Adapter<AdminsCategoriesAdapter.AdminsCategoryViewHolder> {
    ArrayList<CategoryDTO> categories;
    Context context;
    private final Fragment fragment;

    private final AdminsCategoriesManagementViewModel viewmodel;

    public AdminsCategoriesAdapter(Context context, ArrayList<CategoryDTO> categories, Fragment fragment, AdminsCategoriesManagementViewModel viewModel) {
        this.categories = categories;
        this.context = context;
        this.fragment = fragment;
        this.viewmodel = viewModel;
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

        if (categories.get(position).getEventTypes().isEmpty()) {
            holder.deleteButton.setOnClickListener(v -> {
                setupDeleteDialog(position);
            });
        } else {
            holder.deleteButton.setActivated(false);
            holder.deleteButton.setBackgroundColor(context.getResources().getColor(R.color.grey));
        }

        holder.editButton.setOnClickListener(v -> {
                setupEditDialog(position);
        });
    }

    private void setupDeleteDialog(int position) {
            MaterialAlertDialogBuilder confirmDialog = new MaterialAlertDialogBuilder(context);
            confirmDialog.setTitle("Delete category");
            confirmDialog.setMessage("Are you sure you want to delete this category?");
            confirmDialog.setPositiveButton("Yes", (dialog, which) -> {
                viewmodel.deleteCategory(categories.get(position).getId());
            });
            confirmDialog.setNegativeButton("No", (dialog, which) -> {
            });
            confirmDialog.show();
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    private void setupEditDialog(int position) {
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_category_edit, null);

        TextInputLayout nameInput = dialogView.findViewById(R.id.category_name_layout);
        TextInputLayout descriptionInput = dialogView.findViewById(R.id.category_description_layout);

        Objects.requireNonNull(nameInput.getEditText()).setText(categories.get(position).getName());
        Objects.requireNonNull(descriptionInput.getEditText()).setText(categories.get(position).getDescription());

        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(context);
        dialogBuilder.setTitle("Edit category");
        dialogBuilder.setView(dialogView);
        dialogBuilder.setPositiveButton("Save", (dialogInterface, i) -> {
        });
        dialogBuilder.setNegativeButton("Cancel", (dialogInterface, i) -> {
        });
        AlertDialog editDialog = dialogBuilder.create();
        editDialog.show();
        editDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v1 -> {
            if (nameInput.getEditText().getText().toString().trim().isEmpty()) {
                nameInput.setError("Category name is required");
            } else {
                nameInput.setError(null);
            }

            if (descriptionInput.getEditText().getText().toString().trim().isEmpty()) {
                descriptionInput.setError("Category description is required");
            } else {
                descriptionInput.setError(null);
            }

            String name = nameInput.getEditText().getText().toString().trim();
            String description = descriptionInput.getEditText().getText().toString().trim();
            ArrayList<UUID> eventTypesIds = categories.get(position).getEventTypes()
                    .stream()
                    .map(SimpleEventTypeDTO::getId)
                    .collect(Collectors.toCollection(ArrayList::new));

            if (nameInput.getError() == null && descriptionInput.getError() == null) {
                viewmodel.editCategory(
                        categories.get(position).getId(),
                        name,
                        description,
                        eventTypesIds,
                        categories.get(position).getStatus()
                );

                editDialog.dismiss();
            }
        });
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
