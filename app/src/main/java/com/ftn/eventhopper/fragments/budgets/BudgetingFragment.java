package com.ftn.eventhopper.fragments.budgets;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.ftn.eventhopper.R;
import com.ftn.eventhopper.adapters.AdminsSuggestionsAdapter;
import com.ftn.eventhopper.adapters.BudgetItemsAdapter;
import com.ftn.eventhopper.fragments.budgets.viewmodels.BudgetingViewModel;
import com.ftn.eventhopper.shared.dtos.budgets.BudgetManagementDTO;
import com.ftn.eventhopper.shared.dtos.categories.CategoryDTO;
import com.ftn.eventhopper.shared.dtos.categories.SimpleCategoryDTO;
import com.ftn.eventhopper.shared.dtos.eventTypes.SimpleEventTypeDTO;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class BudgetingFragment extends Fragment {


    private BudgetingViewModel viewModel;
    private NavController navController;
    private final String ARG_EVENT_ID = "eventId";
    private UUID eventId;

    private RecyclerView recyclerView;
    private TextView header;
    private MaterialButton addBudgetItemButton;
    private MaterialButton saveButton;
    private TextInputLayout categoryInput;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            eventId = UUID.fromString(getArguments().getString(ARG_EVENT_ID));
        }

        requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                navController.popBackStack(R.id.budgeting, true);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_budgeting, container, false);

        viewModel = new ViewModelProvider(this).get(BudgetingViewModel.class);
        navController = NavHostFragment.findNavController(this);

        recyclerView = view.findViewById(R.id.recycler_view_budget_items);
        header = view.findViewById(R.id.header_text);
        addBudgetItemButton = view.findViewById(R.id.add_budget_item_button);
        saveButton = view.findViewById(R.id.save_button);
        categoryInput = view.findViewById(R.id.category_select_budgeting);



        viewModel.fetchBudget(eventId);
        viewModel.getBudget().observe(getViewLifecycleOwner(), budget -> {
            if (budget != null) {
                this.setFieldValues(budget);
            }
        });

        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                Log.e("Budgeting", error);
                new MaterialAlertDialogBuilder(requireContext())
                        .setTitle("Error")
                        .setMessage(error)
                        .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                        .show();
            }
        });

        return view;
    }

    private void setFieldValues(BudgetManagementDTO budget) {
        header.setText(String.format("Budget for %s", budget.getEvent().getName()));

        BudgetItemsAdapter recyclerViewAdapter = new BudgetItemsAdapter(getContext(), budget, viewModel, navController);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(recyclerViewAdapter);


        List<SimpleCategoryDTO> availableCategories = getAvailableCategories(budget);
        List<String> categoryNames = availableCategories.stream().map(SimpleCategoryDTO::getName).collect(Collectors.toList());
        ArrayAdapter<String> categoryOptionAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, categoryNames);
        ((AutoCompleteTextView) categoryInput.getEditText()).setAdapter(categoryOptionAdapter);

        addBudgetItemButton.setOnClickListener(v -> {
            String categoryName = categoryInput.getEditText().getText().toString();
            SimpleCategoryDTO selectedCategory = availableCategories.stream()
                    .filter(category -> category.getName().equals(categoryName))
                    .findFirst()
                    .orElse(null);

            if (selectedCategory == null) {
                categoryInput.setError("Invalid category");
                return;
            }

            viewModel.addBudgetItem(selectedCategory);
            categoryInput.getEditText().setText("");
        });

        saveButton.setOnClickListener(v -> {
            viewModel.saveBudget(recyclerViewAdapter.getItems());
        });
    }

    private List<SimpleCategoryDTO> getAvailableCategories(BudgetManagementDTO budget) {
        Set<UUID> selectedCategoriesUUID = new HashSet<>();
        budget.getBudgetItems().forEach(budgetItem -> {
            selectedCategoriesUUID.add(budgetItem.getCategory().getId());
        });

        return budget.getEvent().getEventType().getSuggestedCategories().stream()
                .filter(category -> !selectedCategoriesUUID.contains(category.getId()))
                .collect(Collectors.toList());
    }

}