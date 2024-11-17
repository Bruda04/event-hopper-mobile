package com.ftn.eventhopper.activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.ftn.eventhopper.R;
import com.ftn.eventhopper.fragments.Fragment2;
import com.ftn.eventhopper.fragments.Fragment3;
import com.ftn.eventhopper.fragments.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HostActivity extends AppCompatActivity implements BottomNavigationView
        .OnNavigationItemSelectedListener{

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_host);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });

        bottomNavigationView
                = findViewById(R.id.bottomNavigationView);

        bottomNavigationView
                .setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.home);
    }

    HomeFragment homeFragment = new HomeFragment();
    Fragment2 secondFragment = new Fragment2();
    Fragment3 thirdFragment = new Fragment3();


    @Override
    public boolean
    onNavigationItemSelected(@NonNull MenuItem item)
    {

        if(item.getItemId() == R.id.home){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content, homeFragment)
                    .commit();
            return true;
        }else if(item.getItemId() == R.id.calendar){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content, secondFragment)
                    .commit();
            return true;
        }else if(item.getItemId() == R.id.profile){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content, thirdFragment)
                    .commit();
            return true;
        }


        return false;
    }


}