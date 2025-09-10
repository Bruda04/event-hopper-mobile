package com.ftn.eventhopper.fragments;

import android.content.Intent;
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
import com.ftn.eventhopper.adapters.NotificationsAdapter;
import com.ftn.eventhopper.clients.ClientUtils;
import com.ftn.eventhopper.clients.services.notifications.NotificationForegroundService;
import com.ftn.eventhopper.fragments.profile.viewmodels.ProfileViewModel;
import com.ftn.eventhopper.shared.dtos.notifications.NotificationDTO;
import com.ftn.eventhopper.shared.dtos.profile.ProfileForPersonDTO;
import com.ftn.eventhopper.shared.models.Notification;
import com.google.android.material.materialswitch.MaterialSwitch;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class NotificationsFragment extends Fragment {

    private ProfileViewModel viewModel;
    private RecyclerView recyclerView;
    private TextView emptyMessage;
    private NotificationsAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private MaterialSwitch muteSwitch;
    private Gson wsGson;

    public NotificationsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);
        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        // Set up the RecyclerView with an adapter
        recyclerView = view.findViewById(R.id.notification_recyclerview);
        emptyMessage = view.findViewById(R.id.empty_message);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        muteSwitch = view.findViewById(R.id.mute_button);

        muteSwitch.setChecked(!androidx.core.app.NotificationManagerCompat.from(requireContext()).areNotificationsEnabled());


        adapter = new NotificationsAdapter(getContext(), new ArrayList<>(), this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        // Swipe to refresh
        swipeRefreshLayout.setOnRefreshListener(() -> {
            fetchNotifications();
            swipeRefreshLayout.setRefreshing(false);
        });

        // LiveData observer
        viewModel.getNotificationsLiveData().observe(getViewLifecycleOwner(), notifications -> {
            adapter.notifications.clear();
            adapter.notifications.addAll(notifications);
            adapter.notifyDataSetChanged();
            recyclerView.setVisibility(notifications.isEmpty() ? View.GONE : View.VISIBLE);
            emptyMessage.setVisibility(notifications.isEmpty() ? View.VISIBLE : View.GONE);
            if (!notifications.isEmpty()) recyclerView.scrollToPosition(0);
        });

        fetchNotifications();

        wsGson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, (JsonDeserializer<LocalDateTime>) (json, type, context) -> LocalDateTime.parse(json.getAsString()))
                .create();


        muteSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {

            Intent intent = new Intent();
            intent.setAction(android.provider.Settings.ACTION_APP_NOTIFICATION_SETTINGS);
            intent.putExtra(android.provider.Settings.EXTRA_APP_PACKAGE, requireContext().getPackageName());

            startActivity(intent);

        });



        return view;
    }

    private void fetchNotifications() {
        viewModel.fetchProfile();
        viewModel.getProfileChanged().observe(getViewLifecycleOwner(), changed -> {
            if (changed != null && changed) {
                ProfileForPersonDTO profile = viewModel.getProfile();
                viewModel.loadProfileNotifications(profile);
            }
        });
    }


    @Override
    public void onResume(){
        super.onResume();
        updateSwitchState();
    }

    private void updateSwitchState() {
        boolean enabled = androidx.core.app.NotificationManagerCompat.from(requireContext()).areNotificationsEnabled();
        muteSwitch.setChecked(!enabled);
    }

}