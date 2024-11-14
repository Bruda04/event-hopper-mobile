package com.ftn.eventhopper.fragments;// BottomSheetFilterSort.java
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioGroup;

import com.ftn.eventhopper.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheetFilterSort extends BottomSheetDialogFragment {

    private RadioGroup sortRadioGroup;
    private CheckBox filterOption1;
    private CheckBox filterOption2;
    private Button applyFiltersButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.filter_events, container, false);

        // Initialize views
//        sortRadioGroup = view.findViewById(R.id.sort_group);
//        filterOption1 = view.findViewById(R.id.filter_option_1);
//        filterOption2 = view.findViewById(R.id.filter_option_2);
        applyFiltersButton = view.findViewById(R.id.apply_filters_button);

        // Handle the apply button click
        applyFiltersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get selected sorting option
                int selectedSortId = sortRadioGroup.getCheckedRadioButtonId();
                String sortOption = "";
                if (selectedSortId == R.id.sort_by_date) {
                    sortOption = "Date";
                } else if (selectedSortId == R.id.sort_by_name) {
                    sortOption = "Name";
                }

                // Get selected filters
                boolean isOption1Checked = filterOption1.isChecked();
                boolean isOption2Checked = filterOption2.isChecked();

                // Here you would pass the selected options back to the main activity or fragment
                // You could use an interface callback or a shared ViewModel

                // Dismiss the bottom sheet
                dismiss();
            }
        });

        return view;
    }
}
