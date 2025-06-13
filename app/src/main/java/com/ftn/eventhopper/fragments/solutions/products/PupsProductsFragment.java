package com.ftn.eventhopper.fragments.solutions.products;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ftn.eventhopper.R;
import com.ftn.eventhopper.adapters.PupsProductsAdapter;
import com.ftn.eventhopper.fragments.solutions.products.viewmodels.PupsProductsViewModel;
import com.ftn.eventhopper.fragments.solutions.services.BottomSheetPupServicesFilterSort;
import com.ftn.eventhopper.fragments.solutions.services.viewmodels.PupsServicesViewModel;
import com.ftn.eventhopper.shared.dtos.solutions.ProductForManagementDTO;
import com.ftn.eventhopper.shared.dtos.solutions.ServiceManagementDTO;
import com.ftn.eventhopper.shared.responses.PagedResponse;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.search.SearchBar;
import com.google.android.material.search.SearchView;

import java.util.ArrayList;
import java.util.UUID;


public class PupsProductsFragment extends Fragment {

    private PupsProductsViewModel viewModel;
    private RecyclerView recyclerView;
    private TextView statusMessage;
    private int currentPage = 0;
    private final int pageSize = 10;
    private Button filterButton;
    private Button searchButton;
    private SearchView searchView;
    private SearchBar searchBar;
    private Button nextPageButton;
    private Button previousPageButton;
    private TextView pager;
    private FloatingActionButton createServiceButton;
    private SwipeRefreshLayout swipeRefreshLayout;
    private NavController navController;


    public PupsProductsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                navController.popBackStack(R.id.pup_products, true);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pups_products, container, false);
        navController = NavHostFragment.findNavController(this);
        viewModel = new ViewModelProvider(this).get(PupsProductsViewModel.class);

        statusMessage = view.findViewById(R.id.status_message);
        statusMessage.setText(R.string.loading_categories);
        statusMessage.setVisibility(View.VISIBLE);

        recyclerView = view.findViewById(R.id.recycler_view_services);
        nextPageButton = view.findViewById(R.id.next_page_button);
        previousPageButton = view.findViewById(R.id.previous_page_button);
        pager = view.findViewById(R.id.pager);
        filterButton = view.findViewById(R.id.filterButton);
        createServiceButton = view.findViewById(R.id.floating_add_button);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);

        searchBar = view.findViewById(R.id.search_bar);
        searchView = view.findViewById(R.id.search_view);
        searchButton = view.findViewById(R.id.search_button);

        swipeRefreshLayout.setOnRefreshListener(() -> {
            recyclerView.setVisibility(View.GONE);
            statusMessage.setText(R.string.loading_services);
            statusMessage.setVisibility(View.VISIBLE);
            currentPage = 0;
            viewModel.fetchAllProductsPage(currentPage, pageSize);
            swipeRefreshLayout.setRefreshing(false);
        });

        currentPage = 0;
        viewModel.fetchAllProductsPage(currentPage, pageSize);

        viewModel.getProductsPage().observe(getViewLifecycleOwner(), productPage -> {
            if(productPage != null){
                statusMessage.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                pager.setVisibility(View.VISIBLE);
                this.setFields(productPage);
            }
        });

        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                Log.e("EventHopper", "Error fetching products: " + error);
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

        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                BottomSheetPupProductsFilterSort bottomSheet = new BottomSheetPupProductsFilterSort(viewModel);
                bottomSheet.show(fragmentManager, "FilterSortBottomSheet");
            }
        });

        viewModel.getFilters().observe(getViewLifecycleOwner(), filters -> {
            currentPage = 0;
            viewModel.fetchAllProductsPage(currentPage, pageSize);
        });

        createServiceButton.setOnClickListener(v -> {
            currentPage = 0;
            NavController navController = NavHostFragment.findNavController(this);
            navController.navigate(R.id.action_to_create_product1);
        });

        searchButton.setOnClickListener(v -> {
            viewModel.setSearchText(searchView.getText().toString().trim());
            searchBar.setText(viewModel.getSearchText());
            Log.d("PupsProductsFragment", "Search text set to: " + viewModel.getSearchText());
            searchView.hide();
            currentPage = 0;
            viewModel.fetchAllProductsPage(currentPage, pageSize);

        });

        return view;
    }

    private void setFields(PagedResponse<ProductForManagementDTO> productPage) {
        PupsProductsAdapter adapter = new PupsProductsAdapter(getContext(), new ArrayList<>(productPage.getContent()), NavHostFragment.findNavController(this), this, viewModel);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        if (productPage.getTotalPages() == 0) {
            pager.setVisibility(View.GONE);
        }

        pager.setText(String.format("%d/%d", currentPage + 1, productPage.getTotalPages()));

        if (currentPage + 1 < productPage.getTotalPages()) {
            nextPageButton.setBackgroundColor(getResources().getColor(R.color.darker_blue));
            nextPageButton.setOnClickListener(v -> {
                viewModel.fetchAllProductsPage(++currentPage , pageSize);

            });
            nextPageButton.setEnabled(true);
        } else {
            nextPageButton.setEnabled(false);
            nextPageButton.setBackgroundColor(getResources().getColor(R.color.grey));
        }

        if (currentPage > 0) {
            previousPageButton.setBackgroundColor(getResources().getColor(R.color.darker_blue));
            previousPageButton.setOnClickListener(v -> {
                viewModel.fetchAllProductsPage(--currentPage, pageSize);
            });
            previousPageButton.setEnabled(true);
        } else {
            previousPageButton.setEnabled(false);
            previousPageButton.setBackgroundColor(getResources().getColor(R.color.grey));
        }

    }

    public void deleteProduct(UUID id) {
        viewModel.deleteProduct(id);
        currentPage = 0;
        viewModel.fetchAllProductsPage(currentPage, pageSize);
    }
}
