package com.ftn.eventhopper.fragments.solutions.services;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.ftn.eventhopper.R;
import com.ftn.eventhopper.filters.MinMaxInputFilter;
import com.ftn.eventhopper.fragments.solutions.services.viewmodels.ServiceEditViewModel;
import com.ftn.eventhopper.shared.dtos.categories.CategoryDTO;
import com.ftn.eventhopper.shared.dtos.eventTypes.SimpleEventTypeDTO;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

public class ServiceEditData1Fragment extends Fragment {
    private TextInputLayout nameInput, descriptionInput, reservationWindowInput, durationInput, cancellationWindowInput;
    private CheckBox isVisibleCheckbox, isAvailableCheckbox, isAutoAcceptCheckbox;
    private ChipGroup eventTypes;
    private TextView eventTypesError;

    ServiceEditViewModel viewModel;


    private NavController navController;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                viewModel.reset();
                navController.popBackStack(R.id.pup_services, false);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_service_edit_data1, container, false);
        navController = NavHostFragment.findNavController(this);

        viewModel = new ViewModelProvider(requireActivity(),
                new ViewModelProvider.NewInstanceFactory()).get(ServiceEditViewModel.class);

        if (getArguments() != null) {
            viewModel.setData(getArguments());
        }

        // Find input fields and the next button
        nameInput = view.findViewById(R.id.service_name);
        descriptionInput = view.findViewById(R.id.service_description);
        durationInput = view.findViewById(R.id.duration);
        reservationWindowInput = view.findViewById(R.id.reservation_window);
        cancellationWindowInput = view.findViewById(R.id.cancellation_window);
        isVisibleCheckbox = view.findViewById(R.id.visibility_checkbox);
        isAvailableCheckbox = view.findViewById(R.id.availability_checkbox);
        isAutoAcceptCheckbox = view.findViewById(R.id.auto_accept_checkbox);
        eventTypes = view.findViewById(R.id.event_types);
        eventTypesError = view.findViewById(R.id.eventTypesError);

        Objects.requireNonNull(reservationWindowInput.getEditText())
                .setFilters(new InputFilter[]{new MinMaxInputFilter(0, 365)});
        Objects.requireNonNull(cancellationWindowInput.getEditText())
                .setFilters(new InputFilter[]{new MinMaxInputFilter(0, 365)});
        Objects.requireNonNull(durationInput.getEditText())
                .setFilters(new InputFilter[]{new MinMaxInputFilter(0, 1440)});

        viewModel.fetchCategories();
        viewModel.getCategory().observe(
                getViewLifecycleOwner(),
                category -> {
                    if (category != null) {
                            eventTypes.removeAllViews(); // Clear previous chips if any

                            List<String> eventTypesList = category.getEventTypes().stream()
                                .filter(
                                        eventType -> eventType.getName() != null
                                                && !eventType.getName().isEmpty()
                                                && !eventType.isDeactivated()
                                )
                                .map(SimpleEventTypeDTO::getName)
                                .collect(Collectors.toList());

                            for (String eventType : eventTypesList) {
                                Chip chip = new Chip(getContext());
                                chip.setText(eventType);
                                chip.setCheckable(true);
                                eventTypes.addView(chip);
                            }

                        setFields();
                    }
                }
        );

        // Set click listener for the button
        view.findViewById(R.id.next_button).setOnClickListener(v -> {
            if (validate()){
                patchService();
                navController.navigate(R.id.action_to_edit_service2);
            }
        });

        return view;
    }

    private void patchService() {
        viewModel.getServiceUpdateDTO().setName(nameInput.getEditText().getText().toString());
        viewModel.getServiceUpdateDTO().setDescription(descriptionInput.getEditText().getText().toString());
        viewModel.getServiceUpdateDTO().setDurationMinutes(Integer.parseInt(durationInput.getEditText().getText().toString()));
        viewModel.getServiceUpdateDTO().setReservationWindowDays(Integer.parseInt(reservationWindowInput.getEditText().getText().toString()));
        viewModel.getServiceUpdateDTO().setCancellationWindowDays(Integer.parseInt(cancellationWindowInput.getEditText().getText().toString()));
        viewModel.getServiceUpdateDTO().setVisible(isVisibleCheckbox.isChecked());
        viewModel.getServiceUpdateDTO().setAvailable(isAvailableCheckbox.isChecked());
        viewModel.getServiceUpdateDTO().setAutoAccept(isAutoAcceptCheckbox.isChecked());

        CategoryDTO selectedCategory = viewModel.getCategory().getValue();

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
            viewModel.getServiceUpdateDTO().setEventTypesIds(selectedEventTypeIds);
        }
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

        if (durationInput.getEditText().getText() == null || durationInput.getEditText().getText().toString().trim().isEmpty()) {
            durationInput.setError("Duration is required");
            return false;
        } else {
            durationInput.setError(null);
        }

        if (reservationWindowInput.getEditText().getText() == null || reservationWindowInput.getEditText().getText().toString().trim().isEmpty()) {
            reservationWindowInput.setError("Reservation window is required");
            return false;
        } else {
            reservationWindowInput.setError(null);
        }

        if (cancellationWindowInput.getEditText().getText() == null || cancellationWindowInput.getEditText().getText().toString().trim().isEmpty()) {
            cancellationWindowInput.setError("Cancellation window is required");
            return false;
        } else {
            cancellationWindowInput.setError(null);
        }

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

        return true;
    }

    private void setFields() {
        if (viewModel.getServiceUpdateDTO().getName() != null) {
            nameInput.getEditText().setText(viewModel.getServiceUpdateDTO().getName());
        }
        if (viewModel.getServiceUpdateDTO().getDescription() != null) {
            descriptionInput.getEditText().setText(viewModel.getServiceUpdateDTO().getDescription());
        }
        durationInput.getEditText().setText(String.valueOf(viewModel.getServiceUpdateDTO().getDurationMinutes()));
        reservationWindowInput.getEditText().setText(String.valueOf(viewModel.getServiceUpdateDTO().getReservationWindowDays()));
        cancellationWindowInput.getEditText().setText(String.valueOf(viewModel.getServiceUpdateDTO().getCancellationWindowDays()));
        isVisibleCheckbox.setChecked(viewModel.getServiceUpdateDTO().isVisible());
        isAvailableCheckbox.setChecked(viewModel.getServiceUpdateDTO().isAvailable());
        isAutoAcceptCheckbox.setChecked(viewModel.getServiceUpdateDTO().isAutoAccept());

        if (viewModel.getServiceCategoryId() != null) {
            CategoryDTO selectedCategory = viewModel.getCategory().getValue();
            List<String> eventTypesList = selectedCategory.getEventTypes().stream()
                    .filter(
                            eventType -> eventType.getName() != null
                                    && !eventType.getName().isEmpty()
                                    && !eventType.isDeactivated()
                    )
                    .map(SimpleEventTypeDTO::getName)
                    .collect(Collectors.toList());

            eventTypes.removeAllViews(); // Clear previous chips if any

            for (String eventTypeName : eventTypesList) {
                Chip et = new Chip(getContext());
                et.setText(eventTypeName);
                et.setCheckable(true);
                eventTypes.addView(et);
            }

            if (viewModel.getServiceUpdateDTO().getEventTypesIds() != null) {
                // Loop through the chips in the ChipGroup
                for (int i = 0; i < eventTypes.getChildCount(); i++) {
                    Chip chip = (Chip) eventTypes.getChildAt(i);
                    String chipText = chip.getText().toString();

                    // Check if the chip matches one of the selected event types by ID
                    for (UUID eventTypeId : viewModel.getServiceUpdateDTO().getEventTypesIds()) {
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