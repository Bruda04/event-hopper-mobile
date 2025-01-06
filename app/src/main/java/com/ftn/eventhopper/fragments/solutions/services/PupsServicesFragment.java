package com.ftn.eventhopper.fragments.solutions.services;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ftn.eventhopper.R;
import com.ftn.eventhopper.adapters.PupServicesAdapter;
import com.ftn.eventhopper.fragments.solutions.services.viewmodels.PupsServicesViewModel;
import com.ftn.eventhopper.shared.dtos.solutions.ServiceManagementDTO;
import com.ftn.eventhopper.shared.responses.PagedResponse;
import com.google.android.material.search.SearchBar;
import com.google.android.material.search.SearchView;

import java.util.ArrayList;
import java.util.UUID;

public class PupsServicesFragment extends Fragment {

    private PupsServicesViewModel viewModel;
    private RecyclerView recyclerView;
    private TextView statusMessage;
    private int currentPage = 0;
    private final int pageSize = 10;
    private Button filterButton;
    private Button searchButton;
    private SearchView searchView;
    private SearchBar searchBar;
    private String searchText = "";
    private Button nextPageButton;
    private Button previousPageButton;
    private TextView pager;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pups_services, container, false);
        viewModel = new ViewModelProvider(this).get(PupsServicesViewModel.class);

        statusMessage = view.findViewById(R.id.status_message);
        statusMessage.setText(R.string.loading_categories);
        statusMessage.setVisibility(View.VISIBLE);

        recyclerView = view.findViewById(R.id.recycler_view_services);
        nextPageButton = view.findViewById(R.id.next_page_button);
        previousPageButton = view.findViewById(R.id.previous_page_button);
        pager = view.findViewById(R.id.pager);
        filterButton = view.findViewById(R.id.filterButton);

        viewModel.fetchAllServicesPage(currentPage, pageSize);

        viewModel.getServicesPage().observe(getViewLifecycleOwner(), servicePage -> {
            if(servicePage != null){
                statusMessage.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                pager.setVisibility(View.VISIBLE);
                this.setFields(servicePage);
            }
        });

        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                Log.e("EventHopper", "Error fetching services: " + error);
                statusMessage.setText(R.string.oops_something_went_wrong_please_try_again_later);
                recyclerView.setVisibility(View.GONE);
                pager.setVisibility(View.GONE);
                statusMessage.setVisibility(View.VISIBLE);
            } else {
                statusMessage.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                pager.setVisibility(View.VISIBLE);
            }
        });

        viewModel.getFilters().observe(getViewLifecycleOwner(), filters -> {
            if (filters != null) {
                viewModel.fetchAllServicesPage(currentPage, pageSize);
            }
        });

        return view;
    }

    private void setFields(PagedResponse<ServiceManagementDTO> servicePage) {
        PupServicesAdapter adapter = new PupServicesAdapter(getContext(), new ArrayList<>(servicePage.getContent()), NavHostFragment.findNavController(this), this, viewModel);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        if (servicePage.getTotalPages() == 0) {
            pager.setVisibility(View.GONE);
        }
        currentPage = (int) (servicePage.getTotalElements() / pageSize);

        pager.setText(String.format("%d/%d", currentPage + 1, servicePage.getTotalPages()));

        if (currentPage + 1 < servicePage.getTotalPages()) {
            nextPageButton.setOnClickListener(v -> {
                    viewModel.fetchAllServicesPage(++currentPage, pageSize);
            });
        } else {
            nextPageButton.setBackgroundColor(getResources().getColor(R.color.grey));
        }

        if (currentPage > 0) {
            previousPageButton.setOnClickListener(v -> {
                    viewModel.fetchAllServicesPage(--currentPage, pageSize);
            });
        } else {
            previousPageButton.setBackgroundColor(getResources().getColor(R.color.grey));
        }

        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                BottomSheetPupServicesFilterSort bottomSheet = new BottomSheetPupServicesFilterSort(viewModel);
                bottomSheet.show(fragmentManager, "FilterSortBottomSheet");
            }
        });

    }

    public void deleteService(UUID id) {
        viewModel.deleteService(id);
        currentPage = 0;
    }
}
