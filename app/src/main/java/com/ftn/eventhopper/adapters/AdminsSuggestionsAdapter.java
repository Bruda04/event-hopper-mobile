package com.ftn.eventhopper.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.ftn.eventhopper.R;
import com.ftn.eventhopper.fragments.categories.viewmodels.AdminsSuggestionsManagementViewModel;
import com.ftn.eventhopper.shared.dtos.categories.CategoryDTO;
import com.ftn.eventhopper.shared.dtos.categories.CategorySuggestionDTO;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

public class AdminsSuggestionsAdapter extends RecyclerView.Adapter<AdminsSuggestionsAdapter.AdminsCategorySuggestionDTOViewHolder>{
    ArrayList<CategorySuggestionDTO> suggestions;
    Context context;
    private final Fragment fragment;

    private AdminsSuggestionsManagementViewModel viewModel;
    private ArrayList<CategoryDTO> approvedCategories;

    public AdminsSuggestionsAdapter(Context context,
                                    ArrayList<CategorySuggestionDTO> suggestions,
                                    Fragment fragment,
                                    AdminsSuggestionsManagementViewModel viewModel,
                                    ArrayList<CategoryDTO> categories
    ) {
        this.suggestions = suggestions;
        this.context = context;
        this.fragment = fragment;
        this.viewModel = viewModel;
        this.approvedCategories = categories;
    }

    @NonNull
    @Override
    public AdminsSuggestionsAdapter.AdminsCategorySuggestionDTOViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AdminsSuggestionsAdapter.AdminsCategorySuggestionDTOViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_category_suggestion, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdminsSuggestionsAdapter.AdminsCategorySuggestionDTOViewHolder holder, int position) {
        holder.categoryName.setText(suggestions.get(position).getName());
        holder.suggestionForProduct.setText(suggestions.get(position).getProduct().getName());

        holder.approveButton.setOnClickListener(v -> {
            setupApproveDialog(position);
        });

        holder.editButton.setOnClickListener(v -> {
            setupEditDialog(position);
        });
    }

    private void setupEditDialog(int position) {
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_suggestion_edit, null);

        TextInputLayout select = dialogView.findViewById(R.id.category_select);
        AutoCompleteTextView selectCategory = dialogView.findViewById(R.id.category_select_autocomplete);

        ArrayList<String> categoriesNames = new ArrayList<>();
        for (CategoryDTO category : approvedCategories) {
            categoriesNames.add(category.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, categoriesNames);
        selectCategory.setAdapter(adapter);

        MaterialAlertDialogBuilder editDialogBuilder = new MaterialAlertDialogBuilder(context);
        editDialogBuilder.setView(dialogView);
        editDialogBuilder.setTitle("Edit products category");
        editDialogBuilder.setPositiveButton("Save", (dialog, which) -> {
        });
        editDialogBuilder.setNegativeButton("Cancel", (dialog, which) -> {
        });

        AlertDialog editDialog = editDialogBuilder.create();
        editDialog.show();
        editDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            String selectedCategory = selectCategory.getText().toString();
            if (selectedCategory.isEmpty()) {
                select.setError("Please select a category");
            } else {
                CategoryDTO selectedCategoryDTO = approvedCategories.get(categoriesNames.indexOf(selectedCategory));
                viewModel.rejectSuggestion(suggestions.get(position).getId(), selectedCategoryDTO.getId());
                editDialog.dismiss();
            }
        });

    }

    private void setupApproveDialog(int position) {
        MaterialAlertDialogBuilder approveDialog = new MaterialAlertDialogBuilder(context);
        approveDialog.setTitle("Approve suggestion");
        approveDialog.setMessage("Are you sure you want to approve this suggestion?");
        approveDialog.setPositiveButton("Approve", (dialog, which) -> {
            viewModel.approveSuggestion(suggestions.get(position).getId());
        });
        approveDialog.setNegativeButton("Cancel", (dialog, which) -> {
        });
        approveDialog.show();
    }

    @Override
    public int getItemCount() {
        return suggestions.size();
    }

    public class AdminsCategorySuggestionDTOViewHolder extends RecyclerView.ViewHolder {
        private final TextView categoryName;
        private final TextView suggestionForProduct;
        public MaterialButton approveButton;
        public MaterialButton editButton;

        public AdminsCategorySuggestionDTOViewHolder(@NonNull View itemView) {
            super(itemView);
            this.categoryName = itemView.findViewById(R.id.admins_category_suggestion_card_title);
            this.suggestionForProduct = itemView.findViewById(R.id.admins_category_suggestion_for_product);
            this.approveButton = itemView.findViewById(R.id.admins_category_suggestion_card_approve_button);
            this.editButton = itemView.findViewById(R.id.admins_category_suggestions_card_edit_button);
        }
    }
}
