package com.ftn.eventhopper.activities;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ftn.eventhopper.R;
import com.ftn.eventhopper.adapters.PupServicesAdapter;
import com.ftn.eventhopper.models.Service;

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

        ArrayList<Service> services = new ArrayList<Service>();

        services.add(new Service(getDrawable(R.drawable.concert), "Service 1", "$10", "Description for Service 1"));
        services.add(new Service(getDrawable(R.drawable.wedding), "Service 2", "$20", "Description for Service 2"));
        services.add(new Service(getDrawable(R.drawable.concert), "Service 3", "$30", "Description for Service 3"));
        services.add(new Service(getDrawable(R.drawable.concert), "Service 4", "$40", "Description for Service 4"));
        services.add(new Service(getDrawable(R.drawable.wedding), "Service 5", "$50", "Description for Service 5"));

        RecyclerView recyclerView = findViewById(R.id.recycler_view_services);
        PupServicesAdapter adapter = new PupServicesAdapter(services);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);


    }
}