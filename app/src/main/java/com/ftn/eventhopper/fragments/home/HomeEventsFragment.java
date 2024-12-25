package com.ftn.eventhopper.fragments.home;

import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SimpleAdapter;

import com.ftn.eventhopper.R;
import com.ftn.eventhopper.adapters.EventAdapter;
import com.ftn.eventhopper.adapters.TopEventAdapter;
import com.ftn.eventhopper.clients.services.auth.UserService;
import com.ftn.eventhopper.fragments.home.viewmodels.HomeViewModel;
import com.ftn.eventhopper.shared.dtos.events.CardEventDTO;
import com.ftn.eventhopper.shared.dtos.events.SimpleEventDTO;
import com.github.islamkhsh.CardSliderViewPager;
import com.google.android.material.search.SearchBar;

import java.util.ArrayList;
import java.util.UUID;


public class HomeEventsFragment extends Fragment {

    private HomeViewModel viewModel;
    private CardSliderViewPager topEventsRecyclerView;
    private RecyclerView allEventsRecyclerView;
    private SearchBar searchBar;
    private Button filterButton_events;

    private String searchText = "";


    public HomeEventsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_events, container, false);
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);


        // Set up the Event and Service sliders
        this.topEventsRecyclerView = view.findViewById(R.id.viewPagerEvents);
        this.allEventsRecyclerView = view.findViewById(R.id.recyclerView_allevents);

        viewModel.fetchAllEvents();
        UUID usersId = UUID.fromString(UserService.getJwtClaim(
                UserService.getJwtToken(),"id"
        ));
        viewModel.fetchTopEvents(usersId);

        viewModel.getTop5Events().observe(getViewLifecycleOwner(), topEvents ->
        {
            if (topEvents!=null){
                topEventsRecyclerView.setVisibility(View.VISIBLE);
                this.setTop5(topEvents);
            }
        });


        viewModel.getEvents().observe(getViewLifecycleOwner(), events ->
        {
           if(events!=null){
               allEventsRecyclerView.setVisibility(View.VISIBLE);
               this.setAll(events);

           }
        });


        // Set up filter button for events
        filterButton_events = view.findViewById(R.id.filterButton);
        filterButton_events.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                EventsFilterSort bottomSheet = new EventsFilterSort();
                bottomSheet.show(fragmentManager, "FilterSortBottomSheet");
            }
        });

        return view;
        //return inflater.inflate(R.layout.fragment_home_events, container, false);
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

    private void setAll(ArrayList<SimpleEventDTO> events){

        EventAdapter adapter = new EventAdapter(getContext(),events,this);
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(getContext());
        this.allEventsRecyclerView.setLayoutManager(layoutManager);
        this.allEventsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        this.allEventsRecyclerView.setAdapter(adapter);
    }

    private void setTop5(ArrayList<SimpleEventDTO> top5Events){
        this.topEventsRecyclerView.setAdapter(new TopEventAdapter(top5Events));
    }
}