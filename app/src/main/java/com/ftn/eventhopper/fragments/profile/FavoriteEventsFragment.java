package com.ftn.eventhopper.fragments.profile;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ftn.eventhopper.R;
import com.ftn.eventhopper.adapters.events.EventAdapter;
import com.ftn.eventhopper.fragments.profile.viewmodels.ProfileViewModel;
import com.ftn.eventhopper.shared.dtos.events.SimpleEventDTO;
import java.util.ArrayList;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FavoriteEventsFragment#} factory method to
 * create an instance of this fragment.
 */
public class FavoriteEventsFragment extends Fragment {

    private ProfileViewModel viewModel;
    private RecyclerView allEventsRecyclerView;

    private TextView emptyMessage;

    private SwipeRefreshLayout swipeRefreshLayout;

    public FavoriteEventsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite_events, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(ProfileViewModel.class);

        this.allEventsRecyclerView = view.findViewById(R.id.recyclerView_allevents);
        if (allEventsRecyclerView.getParent() instanceof ViewGroup) {
            ((ViewGroup) allEventsRecyclerView.getParent()).setLayoutTransition(null);
        }
        allEventsRecyclerView.setLayoutTransition(null);

        this.emptyMessage = view.findViewById(R.id.emptyMessage);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            fetchFavoriteEvents(true);
        });

        this.fetchFavoriteEvents(false);


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


    private void fetchFavoriteEvents(boolean refresh){
        //if you're told to refresh, or this is your first time and you have to
        if(refresh || viewModel.getProfileData().getValue() == null){
            viewModel.fetchProfile();
        }
        viewModel.getProfileData().observe(getViewLifecycleOwner(), profile -> {
            if (profile != null && profile.getFavoriteEvents() != null && !profile.getFavoriteEvents().isEmpty()) {
                allEventsRecyclerView.setVisibility(View.VISIBLE);

                emptyMessage.setVisibility(View.GONE);
                this.setAll(new ArrayList<>(profile.getFavoriteEvents()));
            } else {
                allEventsRecyclerView.setVisibility(View.GONE);
                emptyMessage.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setAll(ArrayList<SimpleEventDTO> events){
        EventAdapter adapter = new EventAdapter(getContext(),events,this);
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(getContext());
        this.allEventsRecyclerView.setLayoutManager(layoutManager);
        this.allEventsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        this.allEventsRecyclerView.setAdapter(adapter);
    }
}