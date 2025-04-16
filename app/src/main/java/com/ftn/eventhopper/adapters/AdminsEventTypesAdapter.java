package com.ftn.eventhopper.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.ftn.eventhopper.R;
import com.ftn.eventhopper.fragments.eventTypes.viewmodels.AdminsEventTypeManagementViewModel;
import com.ftn.eventhopper.shared.dtos.categories.SimpleCategoryDTO;
import com.ftn.eventhopper.shared.dtos.eventTypes.SimpleEventTypeDTO;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Objects;

public class AdminsEventTypesAdapter extends RecyclerView.Adapter<AdminsEventTypesAdapter.AdminsEventTypeViewHolder> {
    ArrayList<SimpleEventTypeDTO> eventTypes;
    ArrayList<SimpleCategoryDTO> categories;
    Context context;
    private final Fragment fragment;

    private final AdminsEventTypeManagementViewModel viewmodel;

    public AdminsEventTypesAdapter(Context context, ArrayList<SimpleEventTypeDTO> eventTypes, ArrayList<SimpleCategoryDTO> categories, Fragment fragment, AdminsEventTypeManagementViewModel viewModel) {
        this.eventTypes = eventTypes;
        this.categories = categories;
        this.context = context;
        this.fragment = fragment;
        this.viewmodel = viewModel;
    }

    @NonNull
    @Override
    public AdminsEventTypesAdapter.AdminsEventTypeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AdminsEventTypesAdapter.AdminsEventTypeViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_event_type, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdminsEventTypesAdapter.AdminsEventTypeViewHolder holder, int position) {
        holder.eventTypeName.setText(eventTypes.get(position).getName());
        String description = eventTypes.get(position).getDescription();
        if(!description.isEmpty()){
            holder.eventTypeDescription.setText(String.format("%s%s", "Description: ", description));
        }

        StringBuilder suggestedCategories = new StringBuilder("Suggested categories: ");
        for (SimpleCategoryDTO category : eventTypes.get(position).getSuggestedCategories()) {
            suggestedCategories.append(category.getName()).append(", ");
        }

        if (suggestedCategories.length() > 20) { // 20 = "Suggested categories: ".length()
            suggestedCategories.setLength(suggestedCategories.length() - 2); // remove last ", "
        }

        holder.eventTypeSuggestedCategories.setText(suggestedCategories.toString());

        if (!eventTypes.get(position).isDeactivated()) {
            holder.eventTypeActive.setText("Active: Yes");
            holder.deleteButton.setOnClickListener(v -> {
                setupDeleteDialog(position);
            });

            holder.editButton.setOnClickListener(v -> {
                setupEditDialog(position);
            });
        } else {
            holder.eventTypeActive.setText("Active: No");
            holder.deleteButton.setActivated(false);
            holder.deleteButton.setBackgroundColor(context.getResources().getColor(R.color.grey));


            holder.editButton.setActivated(false);
            holder.editButton.setBackgroundColor(context.getResources().getColor(R.color.grey));
        }


    }

    private void setupDeleteDialog(int position) {
        MaterialAlertDialogBuilder confirmDialog = new MaterialAlertDialogBuilder(context);
        confirmDialog.setTitle("Delete event type");
        confirmDialog.setMessage("Are you sure you want to delete this event type?");
        confirmDialog.setPositiveButton("Yes", (dialog, which) -> {
            viewmodel.deleteEventType(eventTypes.get(position).getId());
        });
        confirmDialog.setNegativeButton("No", (dialog, which) -> {
        });
        confirmDialog.show();
    }

    @Override
    public int getItemCount() {
        return eventTypes.size();
    }

    private void setupEditDialog(int position) {
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_event_type_edit, null);

        TextInputLayout nameInput = dialogView.findViewById(R.id.event_type_name_layout);
        TextInputLayout descriptionInput = dialogView.findViewById(R.id.event_type_description_layout);

        Objects.requireNonNull(nameInput.getEditText()).setText(eventTypes.get(position).getName());
        Objects.requireNonNull(descriptionInput.getEditText()).setText(eventTypes.get(position).getDescription());

        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(context);
        dialogBuilder.setTitle("Edit event type");
        dialogBuilder.setView(dialogView);
        dialogBuilder.setPositiveButton("Save", (dialogInterface, i) -> {
        });
        dialogBuilder.setNegativeButton("Cancel", (dialogInterface, i) -> {
        });
        AlertDialog editDialog = dialogBuilder.create();
        editDialog.show();
        editDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v1 -> {
            if (nameInput.getEditText().getText().toString().trim().isEmpty()) {
                nameInput.setError("Event type name is required");
            } else {
                nameInput.setError(null);
            }

            if (descriptionInput.getEditText().getText().toString().trim().isEmpty()) {
                descriptionInput.setError("Event type description is required");
            } else {
                descriptionInput.setError(null);
            }

            String name = nameInput.getEditText().getText().toString().trim();
            String description = descriptionInput.getEditText().getText().toString().trim();


            if (nameInput.getError() == null && descriptionInput.getError() == null) {
                viewmodel.updateEventType(
                        eventTypes.get(position).getId(),
                        name
                );

                editDialog.dismiss();
            }
        });
    }

    public class AdminsEventTypeViewHolder extends RecyclerView.ViewHolder {
        private final TextView eventTypeName;
        private final TextView eventTypeDescription;
        private TextView eventTypeActive;
        private TextView eventTypeSuggestedCategories;
        public MaterialButton deleteButton;
        public MaterialButton editButton;

        public AdminsEventTypeViewHolder(@NonNull View itemView) {
            super(itemView);
            this.eventTypeName = itemView.findViewById(R.id.admins_event_type_card_title);
            this.eventTypeDescription = itemView.findViewById(R.id.admins_event_type_card_description);
            this.eventTypeActive = itemView.findViewById(R.id.admins_event_type_card_active);
            this.eventTypeSuggestedCategories = itemView.findViewById(R.id.admins_event_type_card_suggested_categories);
            this.deleteButton = itemView.findViewById(R.id.admins_event_type_card_delete_button);
            this.editButton = itemView.findViewById(R.id.admins_event_type_card_edit_button);
        }
    }

}
