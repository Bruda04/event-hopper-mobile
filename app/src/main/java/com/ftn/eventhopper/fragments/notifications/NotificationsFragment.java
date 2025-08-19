package com.ftn.eventhopper.fragments.notifications;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ftn.eventhopper.R;
import com.ftn.eventhopper.adapters.AdminsSuggestionsAdapter;
import com.ftn.eventhopper.adapters.NotificationsAdapter;
import com.ftn.eventhopper.fragments.profile.viewmodels.ProfileViewModel;
import com.ftn.eventhopper.shared.dtos.notifications.NotificationDTO;
import com.ftn.eventhopper.shared.dtos.profile.ProfileForPersonDTO;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;


public class NotificationsFragment extends Fragment {


    private ProfileViewModel viewModel;
    private RecyclerView recyclerView;
    private TextView emptyMessage;
    private ArrayList<NotificationDTO> notification;
    private SwipeRefreshLayout swipeRefreshLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_notifications2, container, false);
        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        recyclerView = view.findViewById(R.id.recycler_view_notifications);


        fetchNotifications(false);

        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnChildScrollUpCallback((parent, child) -> {
            // Prevent refresh if RecyclerView is not at the top
            return recyclerView.canScrollVertically(-1);
        });
        swipeRefreshLayout.setOnRefreshListener(() -> {
            recyclerView.setVisibility(View.GONE);
            fetchNotifications(true);
            swipeRefreshLayout.setRefreshing(false);
        });


        return view;
    }

    private void fetchNotifications(boolean refresh){

        if(refresh || viewModel.getProfile() == null){
            viewModel.fetchProfile();
        }
        viewModel.getProfileChanged().observe(getViewLifecycleOwner(), changed -> {
            ProfileForPersonDTO profile = viewModel.getProfile();
            if (changed != null && changed && profile.getFavoriteProducts() != null && !profile.getFavoriteProducts().isEmpty()) {
                recyclerView.setVisibility(View.VISIBLE);
                t.setVisibility(View.GONE);
                this.setAll(new ArrayList<>(profile.getNotifications()));
            } else {
                recyclerView.setVisibility(View.GONE);
                emptyMessage.setVisibility(View.VISIBLE);
            }
            swipeRefreshLayout.setRefreshing(false); // Stop the refresh animation
        });

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }



    private void setAll(ArrayList<NotificationDTO> notifications) {
        NotificationsAdapter adapter = new NotificationsAdapter(getContext(), notifications, this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }
}