package com.ftn.eventhopper.activities;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.ftn.eventhopper.R;
import com.ftn.eventhopper.adapters.EventAdapter;
import com.ftn.eventhopper.adapters.ServiceAdapter;
import com.ftn.eventhopper.models.Event;
import com.ftn.eventhopper.models.Service;
import com.github.islamkhsh.CardSliderViewPager;

import java.util.ArrayList;

public class HomeScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_screen);

        ArrayList<Event> events = new ArrayList<Event>();

        Event event = new Event(
                ContextCompat.getDrawable(this, R.drawable.wedding), // Drawable resurs
                this.getString(R.string.jennet_and_john_wedding),    // String resurs za naslov
                this.getString(R.string.wedding_secondary),             // String resurs za sekundarni tekst
                this.getString(R.string.wedding_supporting)             // String resurs za opis
        );
        Event event2 = new Event(
                ContextCompat.getDrawable(this, R.drawable.concert), // Drawable resurs
                this.getString(R.string.concert_title),    // String resurs za naslov
                this.getString(R.string.concert_secondary),             // String resurs za sekundarni tekst
                this.getString(R.string.concert_supporting)             // String resurs za opis
        );
        events.add(event);
        events.add(event2);
        CardSliderViewPager cardSliderViewPager = (CardSliderViewPager) findViewById(R.id.viewPager);
        cardSliderViewPager.setAdapter(new EventAdapter(events));

        ArrayList<Service> services= new ArrayList<Service>();
        Service service = new Service(
                ContextCompat.getDrawable(this, R.drawable.slani_ketering_2022_postavka),
                this.getString(R.string.catering_title),
                this.getString(R.string.catering_secondary),
                this.getString(R.string.catering_description)
        );
        services.add(service);
        CardSliderViewPager cardSliderViewPager2 = (CardSliderViewPager) findViewById(R.id.viewPager2);
        cardSliderViewPager2.setAdapter(new ServiceAdapter(services));

        ArrayList<Event> allEvents = new ArrayList<Event>();
        allEvents.add(event);
        allEvents.add(event2);
        allEvents.add(event);
        allEvents.add(event2);
//        allEvents.add(event);
//        allEvents.add(event2);
//        allEvents.add(event);
//        allEvents.add(event2);
//        allEvents.add(event);
//        allEvents.add(event2);
//        RecyclerView recyclerView = findViewById(R.id.recyclerView_allevents);
//        recyclerView.setAdapter(new EventAdapter(allEvents));


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


    }

}