package com.ftn.eventhopper.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
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
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
        return new AdminsEventTypeViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_event_type, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdminsEventTypesAdapter.AdminsEventTypeViewHolder holder, int position) {
        holder.eventTypeName.setText(eventTypes.get(position).getName());

        String description = eventTypes.get(position).getDescription();
        if (!description.isEmpty()) {
            holder.eventTypeDescription.setText("Description: " + description);
        }

        StringBuilder suggestedCategories = new StringBuilder("Suggested categories: ");
        for (SimpleCategoryDTO category : eventTypes.get(position).getSuggestedCategories()) {
            suggestedCategories.append(category.getName()).append(", ");
        }

        if (suggestedCategories.length() > 20) {
            suggestedCategories.setLength(suggestedCategories.length() - 2); // remove trailing comma
        }

        holder.eventTypeSuggestedCategories.setText(suggestedCategories.toString());

        if (!eventTypes.get(position).isDeactivated()) {
            holder.eventTypeActive.setText("Active: Yes");
            holder.deleteButton.setOnClickListener(v -> setupDeleteDialog(position));
            holder.editButton.setOnClickListener(v -> setupEditDialog(position));
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
        confirmDialog.setPositiveButton("Yes", (dialog, which) -> viewmodel.deleteEventType(eventTypes.get(position).getId()));
        confirmDialog.setNegativeButton("No", null);
        confirmDialog.show();
    }

    @Override
    public int getItemCount() {
        return eventTypes.size();
    }

    private void setupEditDialog(int position) {
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_event_type_edit, null);

        TextInputLayout descriptionInput = dialogView.findViewById(R.id.event_type_description_layout);
        AutoCompleteTextView categoryDropdown = dialogView.findViewById(R.id.category_dropdown);
        LinearLayout selectedContainer = dialogView.findViewById(R.id.selected_categories_container);

        Objects.requireNonNull(descriptionInput.getEditText()).setText(eventTypes.get(position).getDescription());

        List<SimpleCategoryDTO> allCategories = this.categories;
        List<SimpleCategoryDTO> selectedCategories = new ArrayList<>(eventTypes.get(position).getSuggestedCategories());


        categoryDropdown.setOnItemClickListener((parent, view, pos, id) -> {
            String selectedName = (String) parent.getItemAtPosition(pos);
            SimpleCategoryDTO selected = allCategories.stream()
                    .filter(cat -> cat.getName().equals(selectedName))
                    .findFirst()
                    .orElse(null);

            if (selected != null && !selectedCategories.contains(selected)) {
                selectedCategories.add(selected);
                refreshSelectedCategoriesUI(selectedContainer, selectedCategories, categoryDropdown, allCategories);
                updateCategoryDropdown(categoryDropdown, allCategories, selectedCategories);
            }

            categoryDropdown.setText("");
        });

        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(context);
        dialogBuilder.setTitle("Edit " + eventTypes.get(position).getName());
        dialogBuilder.setView(dialogView);
        dialogBuilder.setPositiveButton("Save", null);
        dialogBuilder.setNegativeButton("Cancel", null);

        updateCategoryDropdown(categoryDropdown, allCategories, selectedCategories);

        AlertDialog editDialog = dialogBuilder.create();
        editDialog.show();

        refreshSelectedCategoriesUI(selectedContainer, selectedCategories, categoryDropdown, allCategories);

        editDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            String description = descriptionInput.getEditText().getText().toString().trim();

            if (description.isEmpty()) {
                descriptionInput.setError("Event type description is required");
                return;
            } else {
                descriptionInput.setError(null);
            }

            viewmodel.updateEventType(
                    eventTypes.get(position).getId(),
                    description,
                    selectedCategories
            );

            editDialog.dismiss();
        });
    }

    private void updateCategoryDropdown(AutoCompleteTextView dropdown, List<SimpleCategoryDTO> allCategories, List<SimpleCategoryDTO> selectedCategories) {
        List<String> availableNames = allCategories.stream()
                .filter(cat -> selectedCategories.stream()
                        .noneMatch(selected -> selected.getId().equals(cat.getId())))
                .map(SimpleCategoryDTO::getName)
                .collect(Collectors.toList());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                context,
                R.layout.item_dropdown,
                availableNames
        );

        dropdown.setAdapter(adapter);
    }

    private void refreshSelectedCategoriesUI(
            LinearLayout container,
            List<SimpleCategoryDTO> selectedCategories,
            AutoCompleteTextView dropdown,
            List<SimpleCategoryDTO> allCategories
    ) {
        container.removeAllViews();
        for (SimpleCategoryDTO cat : selectedCategories) {
            View catView = LayoutInflater.from(context).inflate(R.layout.item_selected_category, container, false);
            TextView name = catView.findViewById(R.id.category_name);
            TextView remove = catView.findViewById(R.id.remove_button);

            name.setText(cat.getName());
            remove.setOnClickListener(v -> {
                selectedCategories.remove(cat);
                refreshSelectedCategoriesUI(container, selectedCategories, dropdown, allCategories);
                updateCategoryDropdown(dropdown, allCategories, selectedCategories);
            });

            container.addView(catView);
        }
        updateCategoryDropdown(dropdown, allCategories, selectedCategories);

    }

    public static class AdminsEventTypeViewHolder extends RecyclerView.ViewHolder {
        private final TextView eventTypeName;
        private final TextView eventTypeDescription;
        private final TextView eventTypeActive;
        private final TextView eventTypeSuggestedCategories;
        public final MaterialButton deleteButton;
        public final MaterialButton editButton;

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
