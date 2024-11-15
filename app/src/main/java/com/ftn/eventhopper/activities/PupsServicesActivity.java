package com.ftn.eventhopper.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ftn.eventhopper.R;
import com.ftn.eventhopper.adapters.PupServicesAdapter;
import com.ftn.eventhopper.fragments.filters.BottomSheetPupServicesFilterSort;
import com.ftn.eventhopper.models.Service;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class PupsServicesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pups_services);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button filterButton_events = findViewById(R.id.filterButtonSolution);

        // Set up the click listener to show the bottom sheet
        filterButton_events.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                BottomSheetPupServicesFilterSort bottomSheet = new BottomSheetPupServicesFilterSort();
                bottomSheet.show(fragmentManager, "BottomSheetPupServicesFilterSort");
            }
        });

        FloatingActionButton AddButton = findViewById(R.id.floating_add_button);

        AddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PupsServicesActivity.this, ServiceCreationActivity.class);
                startActivity(intent);
            }
        });

        ArrayList<Service> services = new ArrayList<Service>();

        services.add(new Service(getDrawable(R.drawable.concert), "Service 1", "$10", "Description for Service 1"));
        services.add(new Service(getDrawable(R.drawable.wedding), "Service 2", "$20", "Description for Service 2"));
        services.add(new Service(getDrawable(R.drawable.concert), "Service 3", "$30", "Description for Service 3"));
        services.add(new Service(getDrawable(R.drawable.concert), "Service 4", "$40", "Description for Service 4"));
        services.add(new Service(getDrawable(R.drawable.wedding), "Service 5", "$50", "Description for Service 5"));

        RecyclerView recyclerView = findViewById(R.id.recycler_view_services);
        PupServicesAdapter adapter = new PupServicesAdapter(this, services);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);


    }
}