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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.ftn.eventhopper.R;
import com.ftn.eventhopper.adapters.AdminsEventTypesAdapter;
import com.ftn.eventhopper.fragments.eventTypes.viewmodels.AdminsEventTypeManagementViewModel;
import com.ftn.eventhopper.shared.dtos.categories.SimpleCategoryDTO;
import com.ftn.eventhopper.shared.dtos.eventTypes.SimpleEventTypeDTO;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AdminsEventTypeManagement extends Fragment {
    private AdminsEventTypeManagementViewModel viewModel;
    private RecyclerView recyclerView;
    private FloatingActionButton btnAddEventType;
    private TextView statusMessage;
    private SwipeRefreshLayout swipeRefreshLayout;
    private NavController navController;

    public AdminsEventTypeManagement() {}

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        navController = NavHostFragment.findNavController(this);
        View view = inflater.inflate(R.layout.fragment_admins_event_type_management, container, false);
        viewModel = new ViewModelProvider(this).get(AdminsEventTypeManagementViewModel.class);

        statusMessage = view.findViewById(R.id.status_message);
        statusMessage.setText(R.string.loading_event_types);
        statusMessage.setVisibility(View.VISIBLE);

        recyclerView = view.findViewById(R.id.recycler_view_event_types);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        btnAddEventType = view.findViewById(R.id.floating_add_button);

        swipeRefreshLayout.setOnRefreshListener(() -> {
            recyclerView.setVisibility(View.GONE);
            statusMessage.setText(R.string.loading_event_types);
            statusMessage.setVisibility(View.VISIBLE);
            viewModel.fetchEventTypes();
            swipeRefreshLayout.setRefreshing(false);
        });

        viewModel.fetchEventTypes();

        viewModel.isLoaded().observe(getViewLifecycleOwner(), isLoaded -> {
            if (isLoaded == Boolean.TRUE) {
                statusMessage.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                setComponents(viewModel.getEventTypes().getValue(), viewModel.getCategories().getValue());
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

    private void setComponents(ArrayList<SimpleEventTypeDTO> eventTypes, ArrayList<SimpleCategoryDTO> categories) {
        AdminsEventTypesAdapter adapter = new AdminsEventTypesAdapter(getContext(), eventTypes, categories, this, viewModel);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        btnAddEventType.setOnClickListener(v -> {
            View dialogView = getLayoutInflater().inflate(R.layout.dialog_event_type_creation, null);
            MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(getContext());
            dialog.setTitle("Create Event Type");
            dialog.setView(dialogView);
            dialog.setPositiveButton("Create", (dialogInterface, i) -> {});
            dialog.setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss());
            AlertDialog alertDialog = dialog.create();
            alertDialog.show();

            AutoCompleteTextView categoryDropdown = dialogView.findViewById(R.id.category_dropdown);
            LinearLayout selectedContainer = dialogView.findViewById(R.id.selected_categories_container);
            List<SimpleCategoryDTO> allCategories = new ArrayList<>(categories);
            List<SimpleCategoryDTO> selectedCategories = new ArrayList<>();

            ArrayAdapter<String> dropdownAdapter = new ArrayAdapter<>(getContext(), R.layout.item_dropdown, new ArrayList<>());
            categoryDropdown.setAdapter(dropdownAdapter);
            updateDropdownAdapter(dropdownAdapter, allCategories, selectedCategories);

            categoryDropdown.setOnItemClickListener((parent, view1, pos, id) -> {
                String selectedName = (String) parent.getItemAtPosition(pos);
                SimpleCategoryDTO selected = allCategories.stream()
                        .filter(cat -> cat.getName().equals(selectedName))
                        .findFirst()
                        .orElse(null);

                if (selected != null && selectedCategories.stream().noneMatch(sc -> sc.getId().equals(selected.getId()))) {
                    selectedCategories.add(selected);
                    refreshSelectedCategoriesUI(selectedContainer, selectedCategories, categoryDropdown, allCategories, dropdownAdapter);
                }

                categoryDropdown.setText("");
            });

            refreshSelectedCategoriesUI(selectedContainer, selectedCategories, categoryDropdown, allCategories, dropdownAdapter);

            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v1 -> {
                if (createEventType(dialogView, selectedCategories)) {
                    alertDialog.dismiss();
                }
            });
        });
    }

    private boolean createEventType(View dialogView, List<SimpleCategoryDTO> selectedCategories) {
        TextInputLayout nameLayout = dialogView.findViewById(R.id.event_type_name_layout);
        String name = nameLayout != null ? nameLayout.getEditText().getText().toString().trim() : "";
        TextInputLayout descriptionLayout = dialogView.findViewById(R.id.event_type_description_layout);
        String description = descriptionLayout != null ? descriptionLayout.getEditText().getText().toString().trim() : "";

        boolean isValid = true;
        if (nameLayout != null && name.isEmpty()) {
            nameLayout.setError("Name is required");
            isValid = false;
        } else {
            nameLayout.setError(null);
        }

        if (descriptionLayout != null && description.isEmpty()) {
            descriptionLayout.setError("Description is required");
            isValid = false;
        } else {
            descriptionLayout.setError(null);
        }

        if (!isValid) {
            return false;
        }

        viewModel.createEventType(name, description, selectedCategories);
        return true;
    }

    private void updateDropdownAdapter(ArrayAdapter<String> adapter, List<SimpleCategoryDTO> allCategories, List<SimpleCategoryDTO> selectedCategories) {
        List<String> availableNames = allCategories.stream()
                .filter(cat -> selectedCategories.stream()
                        .noneMatch(selected -> selected.getId().equals(cat.getId())))
                .map(SimpleCategoryDTO::getName)
                .collect(Collectors.toList());

        adapter.clear();
        adapter.addAll(availableNames);
        adapter.notifyDataSetChanged();
    }

    private void refreshSelectedCategoriesUI(LinearLayout container, List<SimpleCategoryDTO> selectedCategories, AutoCompleteTextView dropdown, List<SimpleCategoryDTO> allCategories, ArrayAdapter<String> adapter) {
        container.removeAllViews();
        for (SimpleCategoryDTO cat : selectedCategories) {
            View catView = LayoutInflater.from(getContext()).inflate(R.layout.item_selected_category, container, false);
            TextView name = catView.findViewById(R.id.category_name);
            TextView remove = catView.findViewById(R.id.remove_button);

            name.setText(cat.getName());
            remove.setOnClickListener(v -> {
                selectedCategories.remove(cat);
                refreshSelectedCategoriesUI(container, selectedCategories, dropdown, allCategories, adapter);
            });

            container.addView(catView);
        }

        updateDropdownAdapter(adapter, allCategories, selectedCategories);
        dropdown.setText("");
        dropdown.clearFocus();
    }
}
