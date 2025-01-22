package com.ftn.eventhopper.fragments.profile;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ftn.eventhopper.R;
import com.ftn.eventhopper.adapters.ImageSliderAdapter;
import com.ftn.eventhopper.clients.services.auth.UserService;
import com.ftn.eventhopper.fragments.profile.viewmodels.ProfileViewModel;
import com.ftn.eventhopper.shared.models.users.PersonType;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CompanyFragment} factory method to
 * create an instance of this fragment.
 */
public class CompanyFragment extends Fragment {

    private ProfileViewModel viewModel;

    private ViewPager2 companyImagesSlider;

    private TextView companyName, companyAddress, companyDescription, companyEmail, companyPhoneNumber;


    public CompanyFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_company, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(ProfileViewModel.class);

        NavController navController = NavHostFragment.findNavController(this);

        this.companyName = view.findViewById(R.id.CompanyName);
        this.companyAddress = view.findViewById(R.id.companyAddress);
        this.companyDescription = view.findViewById(R.id.companyDescription);
        this.companyEmail = view.findViewById(R.id.companyEmail);
        this.companyPhoneNumber = view.findViewById(R.id.companyPhoneNumber);
        this.companyImagesSlider = view.findViewById(R.id.provider_company_image_slider);

        this.fillCompanyData();
        viewModel.fetchProfile();

        return view;
    }

    private void fillCompanyData(){
        viewModel.getProfileData().observe(getViewLifecycleOwner(), profile -> {
            if (profile != null) {
                companyName.setText(profile.getCompanyName());
                companyAddress.setText(profile.getCompanyLocation() != null ? profile.getCompanyLocation().getAddress() + ", " + profile.getCompanyLocation().getCity() : "Not found");
                companyDescription.setText(profile.getCompanyDescription());
                companyEmail.setText(profile.getCompanyEmail());
                companyPhoneNumber.setText(String.format("+%s", profile.getCompanyPhoneNumber()));

                String[] imageUrls = profile.getCompanyPhotos().toArray(String[]::new);
                ImageSliderAdapter imageSliderAdapter = new ImageSliderAdapter(List.of(imageUrls));
                companyImagesSlider.setAdapter(imageSliderAdapter);
            }
        });

    }
}