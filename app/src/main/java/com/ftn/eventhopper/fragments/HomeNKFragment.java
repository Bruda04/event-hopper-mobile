package com.ftn.eventhopper.fragments;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ftn.eventhopper.R;
import com.ftn.eventhopper.adapters.EventAdapter;
import com.ftn.eventhopper.adapters.ServiceAdapter;
import com.github.islamkhsh.CardSliderViewPager;


public class HomeNKFragment extends Fragment {


    private Button filterButton_events;
    private Button filterButton_solutions;


    public HomeNKFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_n_k, container, false);
        EdgeToEdge.enable(requireActivity());

        // Set up filter button for events
        filterButton_events = view.findViewById(R.id.filterButton_NK);
        filterButton_events.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                EventsFilterSort bottomSheet = new EventsFilterSort();
                bottomSheet.show(fragmentManager, "FilterSortBottomSheet");
            }
        });

        // Set up filter button for solutions
        filterButton_solutions = view.findViewById(R.id.filterButtonSolution_NK);
        filterButton_solutions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                SolutionsFilterSort bottomSheet = new SolutionsFilterSort();
                bottomSheet.show(fragmentManager, "FilterSortBottomSheet");
            }
        });

        // Apply window insets for padding adjustment
        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}