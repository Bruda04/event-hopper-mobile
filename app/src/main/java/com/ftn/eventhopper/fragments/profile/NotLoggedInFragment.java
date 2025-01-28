package com.ftn.eventhopper.fragments.profile;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ftn.eventhopper.R;
import com.ftn.eventhopper.activities.MainActivity;
import com.ftn.eventhopper.clients.services.auth.UserService;

public class NotLoggedInFragment extends Fragment {


    public NotLoggedInFragment() {
        // Required empty public constructor
    }

    public static NotLoggedInFragment newInstance(String param1, String param2) {
        NotLoggedInFragment fragment = new NotLoggedInFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_not_logged_in, container, false);

        view.findViewById(R.id.ListItemLogOut).setOnClickListener(v -> {
            ((MainActivity) requireActivity()).navigateToAuthGraph();
        });

        return view;
    }
}