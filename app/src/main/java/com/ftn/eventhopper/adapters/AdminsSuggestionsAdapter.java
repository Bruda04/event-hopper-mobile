package com.ftn.eventhopper.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.ftn.eventhopper.R;
import com.ftn.eventhopper.models.Category;
import com.ftn.eventhopper.models.CategorySuggestion;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class AdminsSuggestionsAdapter extends RecyclerView.Adapter<AdminsSuggestionsAdapter.AdminsCategorySuggestionViewHolder>{
    ArrayList<CategorySuggestion> suggestions;
    Context context;
    private final Fragment fragment;

    public AdminsSuggestionsAdapter(Context context, ArrayList<CategorySuggestion> suggestions, Fragment fragment) {
        this.suggestions = suggestions;
        this.context = context;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public AdminsSuggestionsAdapter.AdminsCategorySuggestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AdminsSuggestionsAdapter.AdminsCategorySuggestionViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_category_suggestion, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdminsSuggestionsAdapter.AdminsCategorySuggestionViewHolder holder, int position) {
        holder.categoryName.setText(suggestions.get(position).getName());
        holder.suggestionForProduct.setText(suggestions.get(position).getForProduct());
    }

    @Override
    public int getItemCount() {
        return suggestions.size();
    }

    public class AdminsCategorySuggestionViewHolder extends RecyclerView.ViewHolder {
        private final TextView categoryName;
        private final TextView suggestionForProduct;
        public MaterialButton approveButton;
        public MaterialButton editButton;

        public AdminsCategorySuggestionViewHolder(@NonNull View itemView) {
            super(itemView);
            this.categoryName = itemView.findViewById(R.id.admins_category_suggestion_card_title);
            this.suggestionForProduct = itemView.findViewById(R.id.admins_category_suggestion_for_product);
            this.approveButton = itemView.findViewById(R.id.admins_category_suggestion_card_approve_button);
            this.editButton = itemView.findViewById(R.id.admins_category_suggestions_card_edit_button);
        }
    }
}
