package com.ftn.eventhopper.fragments.profile;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ftn.eventhopper.R;
import com.ftn.eventhopper.clients.services.auth.UserService;
import com.ftn.eventhopper.fragments.profile.viewmodels.ProfileViewModel;
import com.ftn.eventhopper.shared.models.users.PersonType;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CompanyFragment} factory method to
 * create an instance of this fragment.
 */
public class CompanyFragment extends Fragment {

    private ProfileViewModel viewModel;


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

        TextView companyName = view.findViewById(R.id.CompanyName);
        TextView companyAddress = view.findViewById(R.id.companyAddress);
        TextView companyDescription = view.findViewById(R.id.companyDescription);
        TextView companyEmail = view.findViewById(R.id.companyEmail);




        viewModel.getProfileData().observe(getViewLifecycleOwner(), profile -> {
            if (profile != null) {
                companyName.setText(profile.getCompanyName());
                companyAddress.setText(profile.getCompanyLocation() != null ? profile.getCompanyLocation().getAddress() + ", " + profile.getCompanyLocation().getCity() : "Not found");
                companyDescription.setText(profile.getCompanyDescription());
                companyEmail.setText(profile.getCompanyEmail());
            }
        });
        viewModel.fetchProfile();





        return view;
    }
}