package com.ftn.eventhopper.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import com.ftn.eventhopper.R;
import com.ftn.eventhopper.fragments.categories.viewmodels.AdminsSuggestionsManagementViewModel;
import com.ftn.eventhopper.shared.dtos.categories.CategoryDTO;
import com.ftn.eventhopper.shared.dtos.categories.CategorySuggestionDTO;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
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
            MaterialAlertDialogBuilder confirmDialog = new MaterialAlertDialogBuilder(v.getContext());
            confirmDialog.setTitle("Approve category");
            confirmDialog.setMessage("Are you sure you want to approve this category?");
            confirmDialog.setPositiveButton("Yes", (dialog, which) -> {
                viewModel.rejectSuggestion(suggestions.get(position).getId());
            });
            confirmDialog.setNegativeButton("No", (dialog, which) -> {
            });
            confirmDialog.show();
        });

        holder.editButton.setOnClickListener(v -> {
            View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_suggestion_edit, null);

            TextInputLayout select = dialogView.findViewById(R.id.category_select);
            AutoCompleteTextView selectCategory = dialogView.findViewById(R.id.category_select_autocomplete);

            ArrayList<String> categoriesNames = new ArrayList<>();
            for (CategoryDTO category : approvedCategories) {
                categoriesNames.add(category.getName());
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, categoriesNames);
            selectCategory.setAdapter(adapter);

            MaterialAlertDialogBuilder editDialog = new MaterialAlertDialogBuilder(context);
            editDialog.setView(dialogView);
            editDialog.setTitle("Edit products category");
            editDialog.setPositiveButton("Save", (dialog, which) -> {
            });
            editDialog.setNegativeButton("Cancel", (dialog, which) -> {
            });
            editDialog.show();
        });
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
