package com.ftn.eventhopper.fragments.messages;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ftn.eventhopper.R;
import com.ftn.eventhopper.adapters.ConversationsAdapter;
import com.ftn.eventhopper.fragments.messages.viewmodels.ConversationsListViewModel;
import com.ftn.eventhopper.shared.dtos.messages.ConversationPreviewDTO;

import java.util.ArrayList;

public class ConversationsListFragment extends Fragment {
    private ConversationsListViewModel viewModel;
    private TextView statusMessage;
    private RecyclerView conversationsListRecyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_conversations_list, container, false);
        viewModel = new ViewModelProvider(this).get(ConversationsListViewModel.class);

        statusMessage = view.findViewById(R.id.status_message);
        statusMessage.setText(R.string.loading_conversations);
        statusMessage.setVisibility(View.VISIBLE);
        conversationsListRecyclerView = view.findViewById(R.id.conversations_list);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);

        swipeRefreshLayout.setOnRefreshListener(() -> {
            viewModel.fetchConversations();
            swipeRefreshLayout.setRefreshing(false);
        });


        viewModel.getConversations().observe(getViewLifecycleOwner(), conversations -> {
            if (conversations != null) {
                setFields(conversations);
                statusMessage.setVisibility(View.GONE);
            } else {
                statusMessage.setText(R.string.oops_something_went_wrong_please_try_again_later);
                statusMessage.setVisibility(View.VISIBLE);
            }
        });

        viewModel.fetchConversations();

        return view;
    }

    private void setFields(ArrayList<ConversationPreviewDTO> conversations) {
        ConversationsAdapter conversationsAdapter = new ConversationsAdapter(conversations, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        conversationsListRecyclerView.setLayoutManager(layoutManager);
        conversationsListRecyclerView.setItemAnimator(new DefaultItemAnimator());
        conversationsListRecyclerView.setAdapter(conversationsAdapter);
    }

    public void openSingleChat(ConversationPreviewDTO conversation) {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);

        Bundle bundle = new Bundle();
        bundle.putString("username", conversation.getUsername());
        bundle.putString("name", conversation.getName());
        bundle.putString("surname", conversation.getSurname());

        navController.navigate(R.id.action_to_single_chat, bundle);
    }
}