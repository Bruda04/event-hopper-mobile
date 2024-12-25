package com.ftn.eventhopper.fragments.home;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;

import com.ftn.eventhopper.R;
import com.ftn.eventhopper.fragments.home.viewmodels.HomeViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.util.UUID;

public class EventsFilterSort extends BottomSheetDialogFragment {

    private HomeViewModel viewModel;
    private RadioGroup sortRadioGroup;
    private Button applyFiltersButton;
    private Button openDatePickerButton;
    private String selectedDate = "";
    private AutoCompleteTextView cityAutoComplete;
    private AutoCompleteTextView eventTypeAutoComplete;


    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.filter_events, container, false);
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        // Initialize views
        this.sortRadioGroup = view.findViewById(R.id.sort_group);
        this.applyFiltersButton = view.findViewById(R.id.apply_filters_button);
        openDatePickerButton = view.findViewById(R.id.open_date_picker_button);

        cityAutoComplete = view.findViewById(R.id.location_menu);
        eventTypeAutoComplete = view.findViewById(R.id.event_type_menu);


        this.setUpApplyFiltersButton();
        this.setUpDatePickerButton();

        return view;
    }

    private void setUpApplyFiltersButton() {
        applyFiltersButton.setOnClickListener(v -> {
            String sortOption = getSelectedSortOption();
            String city = cityAutoComplete.getText().toString().trim();
            String eventType = eventTypeAutoComplete.getText().toString().trim();

            //applyFilters(sortOption,selectedDate, city, eventType);
            dismiss();
        });
    }


    private String getSelectedSortOption() {
        int selectedSortId = sortRadioGroup.getCheckedRadioButtonId();
        if (selectedSortId == R.id.sort_events_by_date) {
            return "Date";
        } else if (selectedSortId == R.id.sort_events_by_name) {
            return "Name";
        } else {
            return ""; // No selection or invalid selection
        }
    }

    private void applyFilters(String sortField, String time, String city, UUID eventTypeId) {
        viewModel.applySort(city,
                eventTypeId,
                time,
                searchContent,
                sortField,
                sortDirection,
                page,
                size);
//        if (!sortOption.isEmpty()) {
//            // You can update your ViewModel or trigger the filter logic based on selected sort option.
//            viewModel.applySort(city,
//                    eventTypeId,
//                    time,
//                    searchContent,
//                    sortField,
//                    sortDirection,
//                    page,
//                    size);
//        } else {
//            Toast.makeText(getContext(), "No sort option selected.", Toast.LENGTH_SHORT).show();
//        }
    }


    private void setUpDatePickerButton() {
        openDatePickerButton.setOnClickListener(v -> {
            // Create and show the DatePicker
            showDatePicker();
        });
    }


    private void showDatePicker() {
        MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select a Date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .setCalendarConstraints(new CalendarConstraints.Builder().build()) // Optional constraints
                .build();

        datePicker.show(getChildFragmentManager(), "DATE_PICKER");

        datePicker.addOnPositiveButtonClickListener(selection -> {
            this.selectedDate = datePicker.getHeaderText();
            Toast.makeText(getContext(), "Selected Date: " + selectedDate, Toast.LENGTH_SHORT).show();
        });
    }







}
