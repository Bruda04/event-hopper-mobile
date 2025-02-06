package com.ftn.eventhopper.fragments.categories;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ftn.eventhopper.R;
import com.ftn.eventhopper.adapters.AdminsSuggestionsAdapter;
import com.ftn.eventhopper.fragments.categories.viewmodels.AdminsSuggestionsManagementViewModel;
import com.ftn.eventhopper.shared.dtos.categories.CategoryDTO;
import com.ftn.eventhopper.shared.dtos.categories.CategorySuggestionDTO;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;

public class AdminsSuggestionsManagementFragment extends Fragment {

    private AdminsSuggestionsManagementViewModel viewModel;
    private RecyclerView recyclerView;
    private TextView statusMessage;
    private ArrayList<CategorySuggestionDTO> suggestions;
    private ArrayList<CategoryDTO> approvedCategories;
    private SwipeRefreshLayout swipeRefreshLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_suggestions_management, container, false);
        viewModel = new ViewModelProvider(this).get(AdminsSuggestionsManagementViewModel.class);

        // Set up the RecyclerView with an adapter
        recyclerView = view.findViewById(R.id.recycler_view_suggestions);

        statusMessage = view.findViewById(R.id.status_message);
        statusMessage.setText(R.string.loading_suggestions);
        statusMessage.setVisibility(View.VISIBLE);


        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            recyclerView.setVisibility(View.GONE);
            statusMessage.setText(R.string.loading_suggestions);
            statusMessage.setVisibility(View.VISIBLE);
            viewModel.fetchSuggestions();
            viewModel.fetchApprovedCategories();
            swipeRefreshLayout.setRefreshing(false);
        });



        viewModel.fetchSuggestions();
        viewModel.getCategorySuggestions().observe(getViewLifecycleOwner(), suggestions -> {
            if (suggestions != null) {
                statusMessage.setVisibility(View.GONE);
                this.suggestions = suggestions;
                if (approvedCategories != null) {
                    recyclerView.setVisibility(View.VISIBLE);
                    this.setFieldValues(suggestions, approvedCategories);
                }
            }
        });

        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                Log.e("Category Suggestions", error);
                new MaterialAlertDialogBuilder(requireContext())
                        .setTitle("Error")
                        .setMessage(error)
                        .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                        .show();
            } else {
                statusMessage.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }
        });

        viewModel.fetchApprovedCategories();
        viewModel.getApprovedCategories().observe(getViewLifecycleOwner(), approved -> {
            if (approved != null) {
                statusMessage.setVisibility(View.GONE);
                this.approvedCategories = approved;
                if (suggestions != null) {
                    recyclerView.setVisibility(View.VISIBLE);
                    this.setFieldValues(suggestions, approved);
                }
            }
        });


        return view;
    }

    private void setFieldValues(ArrayList<CategorySuggestionDTO> suggestions, ArrayList<CategoryDTO> approvedCategories) {
        AdminsSuggestionsAdapter adapter = new AdminsSuggestionsAdapter(getContext(), suggestions, this, viewModel, approvedCategories);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }
}