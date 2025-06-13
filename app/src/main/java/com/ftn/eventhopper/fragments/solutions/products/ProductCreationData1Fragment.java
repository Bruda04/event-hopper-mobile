package com.ftn.eventhopper.fragments.solutions.products;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.ftn.eventhopper.R;
import com.ftn.eventhopper.fragments.solutions.products.viewmodels.ProductCreationViewModel;
import com.ftn.eventhopper.fragments.solutions.services.viewmodels.ServiceCreationViewModel;
import com.ftn.eventhopper.shared.dtos.categories.CategoryDTO;
import com.ftn.eventhopper.shared.dtos.eventTypes.SimpleEventTypeDTO;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.materialswitch.MaterialSwitch;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


public class ProductCreationData1Fragment extends Fragment {

    private TextInputLayout nameInput, descriptionInput, category, suggestedCategoryName;
    private ChipGroup eventTypes;
    private TextView eventTypesError;
    private MaterialSwitch suggestSwitch;
    private CheckBox isVisibleCheckbox, isAvailableCheckbox;
    private ProductCreationViewModel viewModel;
    private NavController navController;
    private Button nextButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                viewModel.reset();
                navController.popBackStack(R.id.product_creation1, true);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_creation_data1, container, false);

        navController = NavHostFragment.findNavController(this);
        viewModel = new ViewModelProvider(requireActivity(),
                new ViewModelProvider.NewInstanceFactory()).get(ProductCreationViewModel.class);




        nextButton = view.findViewById(R.id.next_button);
        nameInput = view.findViewById(R.id.service_name);
        descriptionInput = view.findViewById(R.id.service_description);
        suggestSwitch = view.findViewById(R.id.suggest_new_category_switch);
        isVisibleCheckbox = view.findViewById(R.id.visibility_checkbox);
        isAvailableCheckbox = view.findViewById(R.id.availability_checkbox);
        category = view.findViewById(R.id.category_select);
        eventTypes = view.findViewById(R.id.event_types);
        eventTypesError = view.findViewById(R.id.eventTypesError);
        suggestedCategoryName = view.findViewById(R.id.suggested_category_name);


        suggestSwitch.setOnClickListener(v -> {
            if (!suggestSwitch.isChecked()) {
                suggestedCategoryName.setVisibility(View.GONE);
                category.setVisibility(View.VISIBLE);
                eventTypes.setVisibility(View.VISIBLE);
                viewModel.setCategorySuggested(false);
            } else {
                category.setVisibility(View.GONE);
                eventTypes.setVisibility(View.GONE);
                suggestedCategoryName.setVisibility(View.VISIBLE);
                viewModel.setCategorySuggested(true);

            }
        });

        viewModel.fetchCategories();
        viewModel.getCategories().observe(
                getViewLifecycleOwner(),
                categories -> {
                    if (categories != null) {
                        List<String> categoryNames = categories.stream().map(CategoryDTO::getName).collect(Collectors.toList());
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, categoryNames);
                        ((AutoCompleteTextView) category.getEditText()).setAdapter(adapter);

                        ((AutoCompleteTextView) category.getEditText()).setOnItemClickListener((parent, view1, position, id) -> {
                            CategoryDTO selectedCategory = categories.get(position);
                            List<String> eventTypesList = selectedCategory.getEventTypes().stream()
                                    .map(SimpleEventTypeDTO::getName)
                                    .collect(Collectors.toList());

                            eventTypes.removeAllViews(); // Clear previous chips if any

                            for (String eventType : eventTypesList) {
                                Chip chip = new Chip(getContext());
                                chip.setText(eventType);
                                chip.setCheckable(true);
                                eventTypes.addView(chip);
                            }
                        });

                        setFields();
                    }
                }
        );




        // Set click listener for the button
        nextButton.setOnClickListener(v -> {
            if (validate()) {
                patchService();
                navController.navigate(R.id.action_to_create_product2);
            }
        });

        return view;
    }

    private boolean validate() {
        if (nameInput.getEditText().getText().toString().trim().isEmpty()) {
            nameInput.setError("Name is required");
            return false;
        } else {
            nameInput.setError(null);
        }

        if (descriptionInput.getEditText().getText().toString().trim().isEmpty()) {
            descriptionInput.setError("Description is required");
            return false;
        } else if (descriptionInput.getEditText().getText().toString().trim().length() > 1000) {
            descriptionInput.setError("Description is too long");
            return false;

        } else {
            descriptionInput.setError(null);
        }

        if (!suggestSwitch.isChecked()) {
            if (category.getEditText().getText().toString().trim().isEmpty()) {
                category.setError("Category is required");
                return false;
            } else {
                category.setError(null);

                boolean atLeastOneEventTypeSelected = false;
                for (int i = 0; i < eventTypes.getChildCount(); i++) {
                    Chip chip = (Chip) eventTypes.getChildAt(i);
                    if (chip.isChecked()) {
                        atLeastOneEventTypeSelected = true;
                        break;
                    }
                }
                if (!atLeastOneEventTypeSelected) {
                    eventTypesError.setText("At least one event type must be selected");
                    eventTypesError.setVisibility(View.VISIBLE);
                    return false;
                } else {
                    eventTypesError.setVisibility(View.GONE);
                }
            }

        } else {
            if (suggestedCategoryName.getEditText().getText().toString().trim().isEmpty()) {
                suggestedCategoryName.setError("Category name is required");
                return false;
            } else {
                suggestedCategoryName.setError(null);
            }
        }



        return true;
    }

    private void patchService() {
        viewModel.getProduct().setName(nameInput.getEditText().getText().toString().trim());
        viewModel.getProduct().setDescription(descriptionInput.getEditText().getText().toString().trim());
        viewModel.getProduct().setVisible(isVisibleCheckbox.isChecked());
        viewModel.getProduct().setAvailable(isAvailableCheckbox.isChecked());
        if (suggestSwitch.isChecked()) {
            viewModel.setSuggestedCategoryName(suggestedCategoryName.getEditText().getText().toString().trim());
        } else {
            CategoryDTO selectedCategory = viewModel.getCategories().getValue().stream().filter(c -> c.getName().equals(category.getEditText().getText().toString())).findFirst().orElse(null);
            if (selectedCategory != null) {
                viewModel.getProduct().setCategoryId(selectedCategory.getId());

                // Initialize a list to hold the selected event type names
                List<UUID> selectedEventTypeIds = new ArrayList<>();

                // Loop through all chips in the ChipGroup
                for (int i = 0; i < eventTypes.getChildCount(); i++) {
                    Chip chip = (Chip) eventTypes.getChildAt(i);

                    if (chip.isChecked()) {
                        // Add the chip's text (event type name) to the list
                        selectedEventTypeIds.add(
                                selectedCategory.getEventTypes().stream().filter(e -> e.getName().equals(chip.getText().toString())).findFirst().get().getId()
                        );
                    }
                }

                // Optionally, put the selected event type names into the filters map
                if (!selectedEventTypeIds.isEmpty()) {
                    viewModel.getProduct().setEventTypesIds(selectedEventTypeIds);
                }
            }
        }
    }

    private void setFields() {
        if (viewModel.getProduct().getName() != null) {
            nameInput.getEditText().setText(viewModel.getProduct().getName());
        }
        if (viewModel.getProduct().getDescription() != null) {
            descriptionInput.getEditText().setText(viewModel.getProduct().getDescription());
        }
        if (viewModel.getProduct().isVisible()) {
            isVisibleCheckbox.setChecked(true);
        }
        if (viewModel.getProduct().isAvailable()) {
            isAvailableCheckbox.setChecked(true);
        }
        if (viewModel.isCategorySuggested()) {
            suggestSwitch.setChecked(true);
            suggestedCategoryName.setVisibility(View.VISIBLE);
            category.setVisibility(View.GONE);
            eventTypes.setVisibility(View.GONE);
            suggestedCategoryName.getEditText().setText(viewModel.getSuggestedCategoryName());
        } else {
            suggestSwitch.setChecked(false);
            suggestedCategoryName.setVisibility(View.GONE);
            category.setVisibility(View.VISIBLE);
            eventTypes.setVisibility(View.VISIBLE);

            if (viewModel.getProduct().getCategoryId() != null) {
                CategoryDTO selectedCategory = viewModel.getCategories().getValue().stream().filter(c -> c.getId().equals(viewModel.getProduct().getCategoryId())).findFirst().orElse(null);
                ((AutoCompleteTextView) category.getEditText()).setText(selectedCategory.getName(), false);

                List<String> eventTypesList = selectedCategory.getEventTypes().stream()
                        .map(SimpleEventTypeDTO::getName)
                        .collect(Collectors.toList());

                eventTypes.removeAllViews(); // Clear previous chips if any

                for (String eventTypeName : eventTypesList) {
                    Chip et = new Chip(getContext());
                    et.setText(eventTypeName);
                    et.setCheckable(true);
                    eventTypes.addView(et);
                }

                if (viewModel.getProduct().getEventTypesIds() != null) {
                    // Loop through the chips in the ChipGroup
                    for (int i = 0; i < eventTypes.getChildCount(); i++) {
                        Chip chip = (Chip) eventTypes.getChildAt(i);
                        String chipText = chip.getText().toString();

                        // Check if the chip matches one of the selected event types by ID
                        for (UUID eventTypeId : viewModel.getProduct().getEventTypesIds()) {
                            SimpleEventTypeDTO eventType = selectedCategory.getEventTypes().stream()
                                    .filter(e -> e.getId().toString().equals(eventTypeId.toString()))
                                    .findFirst().orElse(null);

                            if (eventType != null && chipText.equals(eventType.getName())) {
                                chip.setChecked(true);
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

}
