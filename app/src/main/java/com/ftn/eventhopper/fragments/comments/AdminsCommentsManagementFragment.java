package com.ftn.eventhopper.fragments.comments;

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
import com.ftn.eventhopper.adapters.AdminsCommentsAdapter;
import com.ftn.eventhopper.fragments.comments.viewmodels.AdminsCommentsManagementViewModel;
import com.ftn.eventhopper.shared.dtos.comments.SimpleCommentDTO;

import java.util.ArrayList;

public class AdminsCommentsManagementFragment extends Fragment {

    private AdminsCommentsManagementViewModel viewModel;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ArrayList<SimpleCommentDTO> pendingComments;
    private TextView statusMessage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_admins_comments_management, container, false);
        viewModel = new ViewModelProvider(this).get(AdminsCommentsManagementViewModel.class);

        recyclerView = view.findViewById(R.id.recycler_view_comments);

        statusMessage = view.findViewById(R.id.status_message);
        statusMessage.setText(R.string.loading_comments);
        statusMessage.setVisibility(View.VISIBLE);

        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            recyclerView.setVisibility(View.GONE);
            statusMessage.setText(R.string.loading_suggestions);
            statusMessage.setVisibility(View.VISIBLE);
            viewModel.fetchPendingComments();
            swipeRefreshLayout.setRefreshing(false);
        });

        viewModel.fetchPendingComments();
        viewModel.getPendingComments().observe(getViewLifecycleOwner(), pendingComments -> {
            if( pendingComments != null){
                statusMessage.setVisibility(View.GONE);
                this.pendingComments = pendingComments;
                this.setFieldValues(pendingComments);
            }
        });

        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                Log.e("EventHopper", "Error fetching comments: " + error);
                statusMessage.setText(R.string.oops_something_went_wrong_please_try_again_later);
                recyclerView.setVisibility(View.GONE);
                statusMessage.setVisibility(View.VISIBLE);
            } else {
                statusMessage.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }
        });

        return view;
    }

    private void setFieldValues(ArrayList<SimpleCommentDTO> pendingComments){
        AdminsCommentsAdapter adapater = new AdminsCommentsAdapter(getContext(), pendingComments, this, viewModel);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapater);

    }
}