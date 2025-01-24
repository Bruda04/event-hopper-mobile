package com.ftn.eventhopper.fragments.comments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ftn.eventhopper.R;
import com.ftn.eventhopper.fragments.comments.viewmodels.AdminsCommentsManagementViewModel;
import com.ftn.eventhopper.shared.dtos.comments.SimpleCommentDTO;

import java.util.ArrayList;

public class AdminsCommentsManagementFragment extends Fragment {

    private AdminsCommentsManagementViewModel viewModel;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ArrayList<SimpleCommentDTO> pendingComments;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_admins_comments_management, container, false);
        viewModel = new ViewModelProvider(this).get(AdminsCommentsManagementViewModel.class);


        return view;
    }
}