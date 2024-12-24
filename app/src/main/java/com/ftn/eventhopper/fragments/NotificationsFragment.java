package com.ftn.eventhopper.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ftn.eventhopper.R;
import com.ftn.eventhopper.adapters.NotificationsAdapter;
import com.ftn.eventhopper.shared.models.Notification;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class NotificationsFragment extends Fragment {


    public NotificationsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);

        // Set up the RecyclerView with an adapter
        RecyclerView recyclerView = view.findViewById(R.id.notification_recyclerview);

        ArrayList<Notification> notifications = new ArrayList<>();
        notifications.add(new Notification("Welcome", "Thank you for joining us!", LocalDateTime.now().minusDays(5)));
        notifications.add(new Notification("Update Available", "A new version of the app is ready for download.", LocalDateTime.now().minusDays(3)));
        notifications.add(new Notification("Reminder", "Don't forget to check out our new features.", LocalDateTime.now().minusDays(2)));
        notifications.add(new Notification("Event Today", "Join our live webinar at 3 PM.", LocalDateTime.now().minusHours(6)));
        notifications.add(new Notification("Survey", "We value your feedback! Take our survey.", LocalDateTime.now().minusMinutes(30)));
        notifications.add(new Notification("Maintenance", "Scheduled maintenance at midnight.", LocalDateTime.now().plusHours(5)));



        NotificationsAdapter adapter = new NotificationsAdapter(getContext(), notifications, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        return view;
    }
}