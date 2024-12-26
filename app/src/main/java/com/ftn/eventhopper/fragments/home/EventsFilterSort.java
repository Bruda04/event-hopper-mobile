package com.ftn.eventhopper.fragments.home;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;

import com.ftn.eventhopper.R;
import com.ftn.eventhopper.fragments.home.viewmodels.HomeViewModel;
import com.ftn.eventhopper.shared.dtos.eventTypes.SimpleEventTypeDTO;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class EventsFilterSort extends BottomSheetDialogFragment {

    private HomeViewModel viewModel;
    private RadioGroup sortRadioGroup;
    private Button applyFiltersButton;
    private Button openDatePickerButton;
    private AutoCompleteTextView cityAutoComplete;
    private AutoCompleteTextView eventTypeAutoComplete;
    private String selectedCity = "";
    private SimpleEventTypeDTO eventType = null;
    private String selectedSortField = "";
    private String searchText = "";
    private String selectedDate = "";

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.filter_events, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);

        // Initialize views
        this.sortRadioGroup = view.findViewById(R.id.sort_group);
        this.applyFiltersButton = view.findViewById(R.id.apply_filters_button);

        openDatePickerButton = view.findViewById(R.id.open_date_picker_button);

        cityAutoComplete = (AutoCompleteTextView) ((TextInputLayout) view.findViewById(R.id.location_menu)).getEditText();
        eventTypeAutoComplete = (AutoCompleteTextView) ((TextInputLayout) view.findViewById(R.id.event_type_menu)).getEditText();

        viewModel.fetchCities();
        viewModel.getCities().observe(getViewLifecycleOwner(), cities -> {
            if (cities != null && !cities.isEmpty()) {
                // Kreiraj adapter sa listom gradova
                ArrayAdapter<String> cityAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, cities);

                // Postavi adapter na AutoCompleteTextView
                cityAutoComplete.setAdapter(cityAdapter);
            }
        });

        applyFiltersButton = view.findViewById(R.id.apply_filters_button);
        applyFiltersButton.setOnClickListener(v -> {
            applyFilters();
            setOnDefault();
            dismiss();
        });
        this.setUpDatePickerButton();

        return view;
    }
    private void applyFilters() {

        this.setSelectedSortOption();
        this.setSelectedCity();
        this.setSelectedEventType();
        UUID eventTypeId = null;
        if (this.eventType != null){
            eventTypeId = eventType.getId();
        }
        viewModel.fetchAllEventsPage(selectedCity,eventTypeId, selectedDate, searchText, selectedSortField, 0, 10);
    }


    private void setSelectedSortOption() {
        int selectedSortId = sortRadioGroup.getCheckedRadioButtonId();
        if (selectedSortId == R.id.sort_events_by_date) {
            selectedSortField= "Date";
        } else if (selectedSortId == R.id.sort_events_by_name) {
            selectedSortField = "Name";
        }
    }

    private void setSelectedCity(){
        selectedCity = cityAutoComplete.getText().toString().trim();
    }

    private void setSelectedEventType(){
        String selectedEventTypeText = eventTypeAutoComplete.getText().toString().trim();
        viewModel.getEventTypes().observe(getViewLifecycleOwner(), eventTypes -> {
            if (eventTypes != null) {
                for (SimpleEventTypeDTO eventTypeDTO : eventTypes) {
                    if (eventTypeDTO.getName().equalsIgnoreCase(selectedEventTypeText)) {
                        eventType = eventTypeDTO;
                        break;
                    }
                }
            }
        });

    }


    private void setUpDatePickerButton() {
        openDatePickerButton.setOnClickListener(v -> {
            showDatePicker();
        });
    }

    private void showDatePicker() {
        MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select a Date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .setCalendarConstraints(new CalendarConstraints.Builder().build())
                .build();

        datePicker.show(getChildFragmentManager(), "DATE_PICKER");

        datePicker.addOnPositiveButtonClickListener(selection -> {

            Date date = new Date(selection);
            String formattedDate = formatDate(date);

            this.selectedDate = formattedDate;
            Toast.makeText(getContext(), "Selected Date: " + formattedDate, Toast.LENGTH_SHORT).show();
        });
    }


    public String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
        return sdf.format(date);
    }

    private void setOnDefault(){
        this.selectedSortField = "";
        this.selectedCity = "";
        this.eventType = null;
        this.selectedDate = "";
    }

}
