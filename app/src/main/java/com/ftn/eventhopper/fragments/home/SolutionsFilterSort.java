package com.ftn.eventhopper.fragments.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;

import com.ftn.eventhopper.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class SolutionsFilterSort extends BottomSheetDialogFragment {

    private RadioGroup sortRadioGroup;

    private Button applyFiltersButton;


    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.filter_solution, container, false);

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
                if (selectedSortId == R.id.sort_solutions_by_date) {
                    sortOption = "Date";
                } else if (selectedSortId == R.id.sort_solutions_by_date) {
                    sortOption = "Name";
                }

                dismiss();
            }
        });



        return view;
    }





}
