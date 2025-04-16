package com.ftn.eventhopper.fragments.eventTypes;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
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
import com.ftn.eventhopper.adapters.AdminsEventTypesAdapter;
import com.ftn.eventhopper.fragments.eventTypes.viewmodels.AdminsEventTypeManagementViewModel;
import com.ftn.eventhopper.shared.dtos.eventTypes.SimpleEventTypeDTO;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdminsEventTypeManagement#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminsEventTypeManagement extends Fragment {
    private AdminsEventTypeManagementViewModel viewModel;
    private RecyclerView recyclerView;
    private FloatingActionButton btnAddEventType;
    private TextView statusMessage;
    private SwipeRefreshLayout swipeRefreshLayout;
    private NavController navController;

    public AdminsEventTypeManagement() {
        // Required empty public constructor
    }

    public static AdminsEventTypeManagement newInstance() {
        return new AdminsEventTypeManagement();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                navController.popBackStack(R.id.manage_event_types, true);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        navController= NavHostFragment.findNavController(this);
        View view = inflater.inflate(R.layout.fragment_admins_event_type_management, container, false);
        viewModel = new ViewModelProvider(this).get(AdminsEventTypeManagementViewModel.class);

        statusMessage = view.findViewById(R.id.status_message);
        statusMessage.setText(R.string.loading_event_types);
        statusMessage.setVisibility(View.VISIBLE);

        // Set up the RecyclerView with an adapter
        recyclerView = view.findViewById(R.id.recycler_view_event_types);

        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            recyclerView.setVisibility(View.GONE);
            statusMessage.setText(R.string.loading_event_types);
            statusMessage.setVisibility(View.VISIBLE);
            viewModel.fetchEventTypes();
            swipeRefreshLayout.setRefreshing(false);
        });

        viewModel.fetchEventTypes();
        btnAddEventType = view.findViewById(R.id.floating_add_button);

        viewModel.getEventTypes().observe(getViewLifecycleOwner(), eventTypes -> {
            if (eventTypes != null) {
                statusMessage.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                this.setComponents(eventTypes);
            }
        });

        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                Log.e("EventHopper", "Error fetching event types: " + error);
                statusMessage.setText(R.string.oops_something_went_wrong_please_try_again_later);
                recyclerView.setVisibility(View.GONE);
                statusMessage.setVisibility(View.VISIBLE);
            } else {
                statusMessage.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }
        });

        return view;
    }



    private void setComponents(ArrayList<SimpleEventTypeDTO> eventTypes) {

        AdminsEventTypesAdapter adapter = new AdminsEventTypesAdapter(getContext(), eventTypes, this, viewModel);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        btnAddEventType.setOnClickListener(v -> {
            View dialogView = getLayoutInflater().inflate(R.layout.dialog_category_creation, null);
            MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(getContext());
            dialog.setTitle("Create Event Type");
            dialog.setView(dialogView);
            dialog.setPositiveButton("Create", (dialogInterface, i) -> {
            });
            dialog.setNegativeButton("Cancel", (dialogInterface, i) -> {
                dialogInterface.dismiss();
            });
            AlertDialog alertDialog = dialog.create();
            alertDialog.show();
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v1 -> {
                if (createEventType(dialogView)) {
                    alertDialog.dismiss();
                }
            });
        });
    }



    private boolean createEventType(View dialogView) {
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
        } else {
            categoryDescriptionLayout.setError(null); // Clear previous error if any
        }

        // If validation fails, do not close the dialog
        if (!isValid) {
            return false; // Do not dismiss the dialog, keep it open
        }

        // If validation passes, proceed to create the category
        viewModel.createEventType(categoryName, categoryDescription);
        return true;
    }
}