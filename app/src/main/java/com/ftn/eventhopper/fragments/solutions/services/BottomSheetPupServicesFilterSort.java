package com.ftn.eventhopper.fragments.solutions.services;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.MultiAutoCompleteTextView;


import com.ftn.eventhopper.R;
import com.ftn.eventhopper.fragments.solutions.services.viewmodels.PupsServicesViewModel;
import com.ftn.eventhopper.shared.dtos.categories.CategoryDTO;
import com.ftn.eventhopper.shared.dtos.eventTypes.SimpleEventTypeDTO;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.slider.RangeSlider;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BottomSheetPupServicesFilterSort extends BottomSheetDialogFragment {

    private PupsServicesViewModel viewModel;
    private TextInputLayout availability;
    private TextInputLayout category;
    private ChipGroup eventTypes;
    private RangeSlider priceRange;


    public BottomSheetPupServicesFilterSort(PupsServicesViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pup_services_filter, container, false);

        Button applyButton = view.findViewById(R.id.apply_filters_button);
        Button clearButton = view.findViewById(R.id.clear_filters_button);
        availability = view.findViewById(R.id.menu_availability);
        priceRange = view.findViewById(R.id.price_range_slider);
        category = view.findViewById(R.id.menu_category);
        eventTypes = view.findViewById(R.id.selected_event_types);

        viewModel.fetchCategories();
        viewModel.getCategories().observe(getViewLifecycleOwner(), categories -> {
            if (categories == null) return;

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
        });

        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFilters();
                dismiss();
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearFilters();
                setFilters();
                dismiss();
            }
        });

        return view;
    }

    private void setFields() {
        Map<String, String> filters = viewModel.getFilters().getValue();

        if (filters != null) {
            if (filters.containsKey("isAvailable"))
                availability.getEditText().setText(filters.get("isAvailable").equals("true") ? "Available" : "Not Available");

            if (filters.containsKey("minPrice")) {
                priceRange.setValues(Float.parseFloat(filters.get("minPrice")), Float.parseFloat(filters.get("maxPrice")));
            }

            if (filters.containsKey("maxPrice")) {
                priceRange.setValues(Float.parseFloat(filters.get("minPrice")), Float.parseFloat(filters.get("maxPrice")));
            }

            if (filters.containsKey("categoryId")) {
                CategoryDTO selectedCategory = viewModel.getCategories().getValue().stream().filter(c -> c.getId().toString().equals(filters.get("categoryId"))).findFirst().orElse(null);
                category.getEditText().setText(selectedCategory.getName());
                if (selectedCategory != null) {
                    ((AutoCompleteTextView) category.getEditText()).getOnItemClickListener().onItemClick(
                            null,
                            null,
                            viewModel.getCategories().getValue().indexOf(selectedCategory),
                            0
                    );
                }
            }

            if (filters.containsKey("eventTypeIds")) {
                String[] selectedEventTypesIds = filters.get("eventTypeIds").split(",");
                CategoryDTO selectedCategory = viewModel.getCategories().getValue().stream().filter(category -> category.getId().toString().equals(filters.get("categoryId"))).findFirst().orElse(null);

                // Loop through the chips in the ChipGroup
                for (int i = 0; i < eventTypes.getChildCount(); i++) {
                    Chip chip = (Chip) eventTypes.getChildAt(i);
                    String chipText = chip.getText().toString();

                    // Check if the chip matches one of the selected event types by ID
                    for (String eventTypeId : selectedEventTypesIds) {
                        SimpleEventTypeDTO eventType = selectedCategory.getEventTypes().stream()
                                .filter(e -> e.getId().toString().equals(eventTypeId))
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

    private void setFilters() {
        Map<String, String> filters = new HashMap<>();

        if (!availability.getEditText().getText().toString().isEmpty()) {
            boolean available = availability.getEditText().getText().toString().equals("Available");
            filters.put("isAvailable", String.valueOf(available));
        }

        if (priceRange.getValues().get(0) != priceRange.getValueFrom()) {
            filters.put("minPrice", String.valueOf(priceRange.getValues().get(0)));
        }

        if (priceRange.getValues().get(1) != priceRange.getValueTo()) {
            filters.put("maxPrice", String.valueOf(priceRange.getValues().get(1)));
        }

        if (!category.getEditText().getText().toString().isEmpty()) {
            CategoryDTO selectedCategory = viewModel.getCategories().getValue().stream().filter(c -> c.getName().equals(category.getEditText().getText().toString())).findFirst().orElse(null);
            if (selectedCategory != null) {
                filters.put("categoryId", selectedCategory.getId().toString());

                // Initialize a list to hold the selected event type names
                List<String> selectedEventTypeIds = new ArrayList<>();

                // Loop through all chips in the ChipGroup
                for (int i = 0; i < eventTypes.getChildCount(); i++) {
                    Chip chip = (Chip) eventTypes.getChildAt(i);

                    if (chip.isChecked()) {
                        // Add the chip's text (event type name) to the list
                        selectedEventTypeIds.add(
                                selectedCategory.getEventTypes().stream().filter(e -> e.getName().equals(chip.getText().toString())).findFirst().get().getId().toString()
                        );
                    }
                }

                // Optionally, put the selected event type names into the filters map
                if (!selectedEventTypeIds.isEmpty()) {
                    filters.put("eventTypeIds", TextUtils.join(",", selectedEventTypeIds));
                }
            }

        }



        viewModel.setFilters(filters);
    }

    private void clearFilters() {
        viewModel.setFilters(null);
    }
}