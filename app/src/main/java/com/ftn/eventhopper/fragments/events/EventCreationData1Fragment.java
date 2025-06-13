package com.ftn.eventhopper.fragments.events;

import android.app.DatePickerDialog;
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
import android.widget.RadioButton;
import android.widget.TextView;

import com.ftn.eventhopper.R;
import com.ftn.eventhopper.fragments.events.viewmodels.EventCreationViewModel;
import com.ftn.eventhopper.fragments.solutions.services.viewmodels.ServiceCreationViewModel;
import com.ftn.eventhopper.shared.dtos.categories.CategoryDTO;
import com.ftn.eventhopper.shared.dtos.eventTypes.EventTypeManagementDTO;
import com.ftn.eventhopper.shared.dtos.eventTypes.SimpleEventTypeDTO;
import com.ftn.eventhopper.shared.models.events.EventPrivacyType;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.materialswitch.MaterialSwitch;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EventCreationData1Fragment extends Fragment {

    private TextInputLayout nameInput, descriptionInput, eventType;
    private EventCreationViewModel viewModel;
    private NavController navController;
    private Button nextButton;

    private TextInputLayout dateInputLayout;
    private TextView dateInput;
    private RadioButton privateRadio;
    private RadioButton publicRadio;

    public EventCreationData1Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                viewModel.reset();
                navController.popBackStack(R.id.event_creation_1, true);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_creation_data1, container, false);

        navController = NavHostFragment.findNavController(this);
        viewModel = new ViewModelProvider(requireActivity(),
                new ViewModelProvider.NewInstanceFactory()).get(EventCreationViewModel.class);


        nextButton = view.findViewById(R.id.next_button);
        nameInput = view.findViewById(R.id.event_name);
        descriptionInput = view.findViewById(R.id.event_description);
        eventType = view.findViewById(R.id.event_type_select);

        dateInputLayout = view.findViewById(R.id.event_date);
        dateInput = view.findViewById(R.id.date_input);
        publicRadio = view.findViewById(R.id.public_radio);
        privateRadio = view.findViewById(R.id.private_radio);

        if (!privateRadio.isChecked() && !publicRadio.isChecked()) {
            publicRadio.setChecked(true);
        }


        viewModel.fetchEventTypes();
        viewModel.getEventTypes().observe(
                getViewLifecycleOwner(),
                eventTypes -> {
                    if (eventTypes != null) {
                        List<String> eventTypeNames = eventTypes.stream().map(SimpleEventTypeDTO::getName).collect(Collectors.toList());
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, eventTypeNames);
                        ((AutoCompleteTextView) eventType.getEditText()).setAdapter(adapter);

                        setFields();
                    }
                }
        );


        dateInput.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(),
                    (view1, year1, month1, dayOfMonth) -> {
                        Calendar selectedDate = Calendar.getInstance();
                        selectedDate.set(year1, month1, dayOfMonth);
                        if (selectedDate.before(Calendar.getInstance())) {
                            dateInputLayout.setError("Date cannot be in the past");
                        } else {
                            dateInputLayout.setError(null);
                            String dateString = String.format(Locale.getDefault(), "%04d-%02d-%02d", year1, month1 + 1, dayOfMonth);
                            dateInput.setText(dateString);
                        }
                    },
                    year, month, day);

            // Disable past dates
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            datePickerDialog.show();
        });



        // Set click listener for the button
        nextButton.setOnClickListener(v -> {
            if (validate()) {
                patchService();
                navController.navigate(R.id.action_to_event_creation2);
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
        } else if (descriptionInput.getEditText().getText().toString().trim().length() > 255) {
            descriptionInput.setError("Description is too long");
            return false;

        } else {
            descriptionInput.setError(null);
        }

        if (dateInput.getText().toString().trim().isEmpty()) {
            dateInputLayout.setError("Date is required");
            return false;
        } else {
            dateInputLayout.setError(null);
        }

        if (eventType.getEditText().getText().toString().trim().isEmpty()) {
            eventType.setError("Event type is required");
            return false;
        } else {
            List<String> eventTypeNames = viewModel.getEventTypes().getValue()
                    .stream().map(SimpleEventTypeDTO::getName).collect(Collectors.toList());
            String selected = eventType.getEditText().getText().toString().trim();
            if (!eventTypeNames.contains(selected)) {
                eventType.setError("Invalid event type");
                return false;
            }
            eventType.setError(null);
        }

        return true;
    }

    private void patchService() {
        viewModel.getEvent().setName(nameInput.getEditText().getText().toString().trim());
        viewModel.getEvent().setDescription(descriptionInput.getEditText().getText().toString().trim());

        // Patch date (assuming format: yyyy-MM-dd)
        String dateString = dateInput.getText().toString().trim();
        if (!dateString.isEmpty()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate date = LocalDate.parse(dateString, formatter);
            LocalDateTime dateTime = date.atTime(12, 0); // Set to 12:00 PM arbitrarily
            viewModel.getEvent().setTime(dateTime);
        }

        if (privateRadio.isChecked()) {
            viewModel.getEvent().setEventPrivacyType(EventPrivacyType.PRIVATE);
        } else if (publicRadio.isChecked()) {
            viewModel.getEvent().setEventPrivacyType(EventPrivacyType.PUBLIC);
        }



        SimpleEventTypeDTO selectedEventType = viewModel.getEventTypes().getValue().stream().filter(c -> c.getName().equals(eventType.getEditText().getText().toString())).findFirst().orElse(null);
        if (selectedEventType != null) {
            if(Objects.equals(selectedEventType.getName(), "All")){
                viewModel.getEvent().setEventTypeId(null);
            }else{
                viewModel.getEvent().setEventTypeId(selectedEventType.getId());
            }


        }
    }

    private void setFields() {
        if (viewModel.getEvent().getName() != null) {
            nameInput.getEditText().setText(viewModel.getEvent().getName());
        }

        if (viewModel.getEvent().getDescription() != null) {
            descriptionInput.getEditText().setText(viewModel.getEvent().getDescription());
        }

        // Set date if already selected
        if (viewModel.getEvent().getTime() != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String formattedDate = viewModel.getEvent().getTime().toLocalDate().format(formatter);
            dateInput.setText(formattedDate);
        }

        // Set radio button based on privacy
        if (viewModel.getEvent().getEventPrivacyType() != null) {
            if (viewModel.getEvent().getEventPrivacyType() == EventPrivacyType.PRIVATE) {
                privateRadio.setChecked(true);
            } else {
                publicRadio.setChecked(true);
            }
        }

        // Set selected event type
        if (viewModel.getEventTypes().getValue() != null && viewModel.getEvent().getEventTypeId() != null) {
            for (SimpleEventTypeDTO type : viewModel.getEventTypes().getValue()) {
                if (type.getId().equals(viewModel.getEvent().getEventTypeId())) {
                    eventType.getEditText().setText(type.getName());
                    break;
                }
            }
        }
    }


}