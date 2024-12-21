package com.ftn.eventhopper.fragments.solutions;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ftn.eventhopper.R;
import com.ftn.eventhopper.fragments.solutions.viewmodel.SolutionPageViewModel;

import java.util.UUID;


public class SolutionPageFragment extends Fragment {
    private static final String ARG_ID = "id";

    private SolutionPageViewModel viewModel;


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.fragment_solution_page, container, false);


        viewModel = new ViewModelProvider(this).get(SolutionPageViewModel.class);

        if (getArguments() != null) {
            UUID productId = UUID.fromString(getArguments().getString(ARG_ID));
            viewModel.fetchSolutionDetailsById(productId);
        }

        viewModel.getSolutionDetails().observe(getViewLifecycleOwner(), product -> {
            if (product != null) {
                Log.d("EventHopper", "Solution details fetched: " + product);
            }
        });

        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                Log.e("EventHopper", "Error fetching solution details: " + error);
                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}