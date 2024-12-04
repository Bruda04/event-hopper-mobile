package com.ftn.eventhopper.fragments.home;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.ftn.eventhopper.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;

public class EventsFilterSort extends BottomSheetDialogFragment {

    private RadioGroup sortRadioGroup;

    private Button applyFiltersButton;
    private Button openDatePickerButton;

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.filter_events, container, false);

        // Initialize views
        sortRadioGroup = view.findViewById(R.id.sort_group);
        applyFiltersButton = view.findViewById(R.id.apply_filters_button);

        // Handle the apply button click
        applyFiltersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get selected sorting option
                int selectedSortId = sortRadioGroup.getCheckedRadioButtonId();
                String sortOption = "";
                if (selectedSortId == R.id.sort_events_by_date) {
                    sortOption = "Date";
                } else if (selectedSortId == R.id.sort_events_by_name) {
                    sortOption = "Name";
                }



                // Here you would pass the selected options back to the main activity or fragment
                // You could use an interface callback or a shared ViewModel

                // Dismiss the bottom sheet
                dismiss();
            }
        });

        openDatePickerButton = view.findViewById(R.id.open_date_picker_button);

        openDatePickerButton.setOnClickListener(v -> {
            // Create the DatePicker
            MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Select a Date")
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .setCalendarConstraints(new CalendarConstraints.Builder().build()) // Optional constraints
                    .build();

            // Show the DatePicker
            datePicker.show(getChildFragmentManager(), "DATE_PICKER");

            // Handle selection
            datePicker.addOnPositiveButtonClickListener(selection -> {
                // Selection is in UTC milliseconds
                String selectedDate = datePicker.getHeaderText();
                Toast.makeText(getContext(), "Selected Date: " + selectedDate, Toast.LENGTH_SHORT).show();
            });
        });

        return view;
    }





}
