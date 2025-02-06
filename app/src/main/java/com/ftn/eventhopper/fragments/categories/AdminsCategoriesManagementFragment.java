package com.ftn.eventhopper.fragments.categories;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
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
import com.ftn.eventhopper.adapters.AdminsCategoriesAdapter;
import com.ftn.eventhopper.fragments.categories.viewmodels.AdminsCategoriesManagementViewModel;
import com.ftn.eventhopper.shared.dtos.categories.CategoryDTO;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

public class AdminsCategoriesManagementFragment extends Fragment {
    private AdminsCategoriesManagementViewModel viewModel;
    private RecyclerView recyclerView;
    private FloatingActionButton btnAddCategory;
    private TextView statusMessage;
    private SwipeRefreshLayout swipeRefreshLayout;


    public AdminsCategoriesManagementFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_categories_management, container, false);
        viewModel = new ViewModelProvider(this).get(AdminsCategoriesManagementViewModel.class);

        statusMessage = view.findViewById(R.id.status_message);
        statusMessage.setText(R.string.loading_categories);
        statusMessage.setVisibility(View.VISIBLE);

        // Set up the RecyclerView with an adapter
        recyclerView = view.findViewById(R.id.recycler_view_categories);

        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            recyclerView.setVisibility(View.GONE);
            statusMessage.setText(R.string.loading_categories);
            statusMessage.setVisibility(View.VISIBLE);
            viewModel.fetchApprovedCategories();
            swipeRefreshLayout.setRefreshing(false);
        });

        viewModel.fetchApprovedCategories();
        btnAddCategory = view.findViewById(R.id.floating_add_button);

        viewModel.getApprovedCategories().observe(getViewLifecycleOwner(), categories -> {
            if (categories != null) {
                statusMessage.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                this.setComponents(categories);
            }
        });

        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                Log.e("Manage Categories", error);
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

        return view;
    }

    private void setComponents(ArrayList<CategoryDTO> categories) {

        AdminsCategoriesAdapter adapter = new AdminsCategoriesAdapter(getContext(), categories, this, viewModel);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        btnAddCategory.setOnClickListener(v -> {
            View dialogView = getLayoutInflater().inflate(R.layout.dialog_category_creation, null);
            MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(getContext());
            dialog.setTitle("Create category");
            dialog.setView(dialogView);
            dialog.setPositiveButton("Create", (dialogInterface, i) -> {
            });
            dialog.setNegativeButton("Cancel", (dialogInterface, i) -> {
                dialogInterface.dismiss();
            });
            AlertDialog alertDialog = dialog.create();
            alertDialog.show();
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v1 -> {
                if (createCategory(dialogView)) {
                    alertDialog.dismiss();
                }
            });
        });
    }



    private boolean createCategory(View dialogView) {
        boolean isValid = true;

        // Get references to the input fields
        TextInputLayout categoryNameLayout = dialogView.findViewById(R.id.category_name_layout);
        String categoryName = categoryNameLayout != null ? categoryNameLayout.getEditText().getText().toString().trim() : "";

        // Check category name validation
        if (categoryNameLayout != null && categoryName.isEmpty()) {
            categoryNameLayout.setError("Category name is required");
            isValid = false;
        } else {
            categoryNameLayout.setError(null); // Clear previous error if any
        }

        TextInputLayout categoryDescriptionLayout = dialogView.findViewById(R.id.category_description_layout);
        String categoryDescription = categoryDescriptionLayout != null ? categoryDescriptionLayout.getEditText().getText().toString().trim() : "";

        // Check category description validation
        if (categoryDescriptionLayout != null && categoryDescription.isEmpty()) {
            categoryDescriptionLayout.setError("Category description is required");
            isValid = false;
        } else if (categoryDescription.length() > 1000) {
            categoryDescriptionLayout.setError("Category description is too long");
            isValid = false;
        } else {
            categoryDescriptionLayout.setError(null); // Clear previous error if any
        }

        // If validation fails, do not close the dialog
        if (!isValid) {
            return false; // Do not dismiss the dialog, keep it open
        }

        // If validation passes, proceed to create the category
        viewModel.createCategory(categoryName, categoryDescription);
        return true;
    }

}