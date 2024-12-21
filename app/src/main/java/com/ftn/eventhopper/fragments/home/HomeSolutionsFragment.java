package com.ftn.eventhopper.fragments.home;

import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ftn.eventhopper.R;
import com.ftn.eventhopper.adapters.ServiceAdapter;
import com.ftn.eventhopper.shared.models.Service;
import com.github.islamkhsh.CardSliderViewPager;

import java.util.ArrayList;

public class HomeSolutionsFragment extends Fragment {


    private Button filterButton_solutions;

    public HomeSolutionsFragment() {
        // Required empty public constructor
    }

    private ArrayList<Service> loadTop5Services() {
        ArrayList<Service> services = new ArrayList<>();
        Service service = new Service(
                ContextCompat.getDrawable(getContext(), R.drawable.slani_ketering_2022_postavka),
                getString(R.string.catering_title),
                getString(R.string.catering_secondary),
                getString(R.string.catering_description)
        );
        services.add(service);
        return services;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_solutions, container, false);

        CardSliderViewPager cardSliderViewPager = view.findViewById(R.id.viewPagerSolutions);
        cardSliderViewPager.setAdapter(new ServiceAdapter(loadTop5Services()));

        filterButton_solutions = view.findViewById(R.id.filterButtonSolution);
        filterButton_solutions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                SolutionsFilterSort bottomSheet = new SolutionsFilterSort();
                bottomSheet.show(fragmentManager, "FilterSortBottomSheet");
            }
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