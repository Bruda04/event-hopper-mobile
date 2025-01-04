package com.ftn.eventhopper.fragments.solutions.services;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


import com.ftn.eventhopper.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheetPupServicesFilterSort extends BottomSheetDialogFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pup_services_filter, container, false);

        Button applyButton = view.findViewById(R.id.apply_filters_button);

        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dismiss();
            }
        });

        return view;
    }
}