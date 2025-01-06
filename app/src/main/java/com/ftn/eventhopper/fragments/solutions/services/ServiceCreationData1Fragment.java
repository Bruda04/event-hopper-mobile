package com.ftn.eventhopper.fragments.solutions.services;

import android.os.Bundle;

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

import com.ftn.eventhopper.R;
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

public class ServiceCreationData1Fragment extends Fragment {

    private TextInputLayout nameInput, descriptionInput, category;
    private ChipGroup eventTypes;
    private MaterialSwitch suggestSwitch;
    private CheckBox isVisibleCheckbox, isAvailableCheckbox, isAutoAcceptCheckbox;

    private ServiceCreationViewModel viewModel;
    private NavController navController;

    private Button nextButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_service_creation_data1, container, false);

        navController = NavHostFragment.findNavController(this);
        viewModel = new ViewModelProvider(requireActivity(),
                new ViewModelProvider.NewInstanceFactory()).get(ServiceCreationViewModel.class);


        nextButton = view.findViewById(R.id.next_button);
        nameInput = view.findViewById(R.id.service_name);
        descriptionInput = view.findViewById(R.id.service_description);
        suggestSwitch = view.findViewById(R.id.suggest_new_category_switch);
        isVisibleCheckbox = view.findViewById(R.id.visibility_checkbox);
        isAvailableCheckbox = view.findViewById(R.id.availability_checkbox);
        isAutoAcceptCheckbox = view.findViewById(R.id.auto_accept_checkbox);
        category = view.findViewById(R.id.category_select);
        eventTypes = view.findViewById(R.id.event_types);

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
            patchService();
            navController.navigate(R.id.action_to_create_service2);
        });

        return view;
    }

    private void patchService() {
        viewModel.getService().setName(nameInput.getEditText().getText().toString().trim());
        viewModel.getService().setDescription(descriptionInput.getEditText().getText().toString().trim());
        viewModel.getService().setVisible(isVisibleCheckbox.isChecked());
        viewModel.getService().setAvailable(isAvailableCheckbox.isChecked());
        viewModel.getService().setAutoAccept(isAutoAcceptCheckbox.isChecked());
        CategoryDTO selectedCategory = viewModel.getCategories().getValue().stream().filter(c -> c.getName().equals(category.getEditText().getText().toString())).findFirst().orElse(null);
        if (selectedCategory != null) {
            viewModel.getService().setCategoryId(selectedCategory.getId());

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
                viewModel.getService().setEventTypesIds(selectedEventTypeIds);
            }
        }
    }

    private void setFields() {
        if (viewModel.getService().getName() != null) {
            nameInput.getEditText().setText(viewModel.getService().getName());
        }
        if (viewModel.getService().getDescription() != null) {
            descriptionInput.getEditText().setText(viewModel.getService().getDescription());
        }
        if (viewModel.getService().isVisible()) {
            isVisibleCheckbox.setChecked(true);
        }
        if (viewModel.getService().isAvailable()) {
            isAvailableCheckbox.setChecked(true);
        }
        if (viewModel.getService().isAutoAccept()) {
            isAutoAcceptCheckbox.setChecked(true);
        }
        if (viewModel.getService().getCategoryId() != null) {
            CategoryDTO selectedCategory = viewModel.getCategories().getValue().stream().filter(c -> c.getId().equals(viewModel.getService().getCategoryId())).findFirst().orElse(null);
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

            if (viewModel.getService().getEventTypesIds() != null) {
                // Loop through the chips in the ChipGroup
                for (int i = 0; i < eventTypes.getChildCount(); i++) {
                    Chip chip = (Chip) eventTypes.getChildAt(i);
                    String chipText = chip.getText().toString();

                    // Check if the chip matches one of the selected event types by ID
                    for (UUID eventTypeId : viewModel.getService().getEventTypesIds()) {
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

