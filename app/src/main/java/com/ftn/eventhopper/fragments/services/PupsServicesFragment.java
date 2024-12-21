package com.ftn.eventhopper.fragments.services;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ftn.eventhopper.R;
import com.ftn.eventhopper.adapters.PupServicesAdapter;
import com.ftn.eventhopper.shared.models.Service;
import com.ftn.eventhopper.fragments.filters.BottomSheetPupServicesFilterSort;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class PupsServicesFragment extends Fragment {

    public PupsServicesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the fragment layout
        View view = inflater.inflate(R.layout.fragment_pups_services, container, false);

        Button filterButtonEvents = view.findViewById(R.id.filterButtonSolution);
        filterButtonEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getChildFragmentManager();
                BottomSheetPupServicesFilterSort bottomSheet = new BottomSheetPupServicesFilterSort();
                bottomSheet.show(fragmentManager, "BottomSheetPupServicesFilterSort");
            }
        });

        NavController navController = NavHostFragment.findNavController(this);

        FloatingActionButton addButton = view.findViewById(R.id.floating_add_button);
        addButton.setOnClickListener(v -> {
            navController.navigate(R.id.action_to_create_service1);
        });

        ArrayList<Service> services = new ArrayList<>();
        services.add(new Service(getResources().getDrawable(R.drawable.concert), "Service 1", "$10", "Description for Service 1"));
        services.add(new Service(getResources().getDrawable(R.drawable.wedding), "Service 2", "$20", "Description for Service 2"));
        services.add(new Service(getResources().getDrawable(R.drawable.concert), "Service 3", "$30", "Description for Service 3"));
        services.add(new Service(getResources().getDrawable(R.drawable.concert), "Service 4", "$40", "Description for Service 4"));
        services.add(new Service(getResources().getDrawable(R.drawable.wedding), "Service 5", "$50", "Description for Service 5"));

        // Set up the RecyclerView with an adapter
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_services);

        PupServicesAdapter adapter = new PupServicesAdapter(getContext(), services, navController, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        return view;
    }
}
