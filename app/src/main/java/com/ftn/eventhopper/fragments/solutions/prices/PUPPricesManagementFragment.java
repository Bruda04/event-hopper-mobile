package com.ftn.eventhopper.fragments.solutions.prices;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
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
import com.ftn.eventhopper.adapters.AdminsCategoriesAdapter;
import com.ftn.eventhopper.adapters.PupPricesAdapter;
import com.ftn.eventhopper.fragments.solutions.prices.viewmodels.PUPPricesManagementViewModel;
import com.ftn.eventhopper.shared.dtos.prices.PriceManagementDTO;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class PUPPricesManagementFragment extends Fragment {
    private PUPPricesManagementViewModel viewModel;
    private RecyclerView recyclerView;
    private TextView statusMessage;
    private FloatingActionButton exportButton;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_p_u_p_prices_management, container, false);

        viewModel = new ViewModelProvider(this).get(PUPPricesManagementViewModel.class);

        statusMessage = view.findViewById(R.id.status_message);
        statusMessage.setText(R.string.loading_prices);
        statusMessage.setVisibility(View.VISIBLE);

        recyclerView = view.findViewById(R.id.recycler_view_prices);
        statusMessage = view.findViewById(R.id.status_message);
        exportButton = view.findViewById(R.id.floating_export_button);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);

        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            recyclerView.setVisibility(View.GONE);
            statusMessage.setText(R.string.loading_prices);
            statusMessage.setVisibility(View.VISIBLE);
            viewModel.fetchPrices();
            swipeRefreshLayout.setRefreshing(false);
        });

        viewModel.fetchPrices();

        viewModel.getPrices().observe(getViewLifecycleOwner(), prices -> {
            if (prices != null) {
                statusMessage.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                this.setComponents(prices);
            }
        });

        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                Log.e("EventHopper", "Error fetching prices: " + error);
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

    private void setComponents(ArrayList<PriceManagementDTO> prices) {
        PupPricesAdapter adapter = new PupPricesAdapter(getContext(), prices, this, viewModel);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        exportButton.setOnClickListener(v -> {
            viewModel.exportPricesToPDF(getContext());
        });
    }
}