package com.ftn.eventhopper.fragments.profile;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ftn.eventhopper.R;
import com.ftn.eventhopper.activities.MainActivity;
import com.ftn.eventhopper.clients.ClientUtils;
import com.ftn.eventhopper.clients.services.auth.UserService;
import com.ftn.eventhopper.fragments.login.viewmodels.LoginViewModel;
import com.ftn.eventhopper.fragments.profile.viewmodels.ProfileViewModel;
import com.ftn.eventhopper.shared.models.users.PersonType;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    private ProfileViewModel viewModel;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);


        NavController navController = NavHostFragment.findNavController(this);

        // Set up the click listener for the "View my services" button
        view.findViewById(R.id.ListItemMyServices).setOnClickListener(v -> {
            navController.navigate(R.id.action_to_pup_services);
        });

        view.findViewById(R.id.ListItemCategories).setOnClickListener(v -> {
            navController.navigate(R.id.action_to_manage_categories);
        });

        view.findViewById(R.id.ListItemMyPrices).setOnClickListener(v -> {
            navController.navigate(R.id.action_to_manage_prices);
        });

        view.findViewById(R.id.ListItemLogOut).setOnClickListener(v -> {
            viewModel.logout();
            Intent intent = new Intent(requireContext(), MainActivity.class);

            // Clear the back stack and start MainActivity as a new task
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            // Start MainActivity
            startActivity(intent);



            // Optionally, finish the current activity if needed
            requireActivity().finish();
        });



        ImageView profileImage = view.findViewById(R.id.profileImage);
        TextView companyName = view.findViewById(R.id.CompanyName);
        TextView companyAddress = view.findViewById(R.id.companyAddress);
        TextView companyDescription = view.findViewById(R.id.companyDescription);
        TextView companyEmail = view.findViewById(R.id.companyEmail);

        TextView userName = view.findViewById(R.id.userName);
        TextView userAddress = view.findViewById(R.id.userAddress);
        TextView userCity = view.findViewById(R.id.userCity);
        TextView userEmail = view.findViewById(R.id.userEmail);

        View companyInfoSection = view.findViewById(R.id.company_information);
        companyInfoSection.setVisibility(UserService.getUserRole() == PersonType.SERVICE_PROVIDER ? View.VISIBLE : View.GONE);
        // Observe LiveData
        viewModel.getProfileData().observe(getViewLifecycleOwner(), profile -> {
            if (profile != null) {
                // Populate Company Info
                if(UserService.getUserRole() == PersonType.SERVICE_PROVIDER){
                    companyName.setText(profile.getCompanyName());
                    companyAddress.setText(profile.getCompanyLocation() != null ? profile.getCompanyLocation().getAddress() + ", " + profile.getCompanyLocation().getCity() : "Not found");
                    companyDescription.setText(profile.getCompanyDescription());
                    companyEmail.setText(profile.getCompanyEmail());
                }

                // Populate User Info
                userName.setText(String.format("%s %s", profile.getName(), profile.getSurname()));
                userAddress.setText(profile.getLocation() != null ? profile.getLocation().getAddress() : "N/A");
                userCity.setText(profile.getLocation() != null ? profile.getLocation().getCity() : "N/A");
                userEmail.setText(profile.getEmail());


                String profilePicUrl = profile.getProfilePicture();
                String fullProfileImageUrl = (profilePicUrl == null || profilePicUrl.isEmpty())
                        ? String.format("%s/profile.png", ClientUtils.SERVICE_API_IMAGE_PATH)
                        : String.format("%s/%s", ClientUtils.SERVICE_API_IMAGE_PATH, profilePicUrl);

                Glide.with(requireContext())
                        .load(fullProfileImageUrl)
                        .circleCrop()
                        .into(profileImage);
            }
        });

        // Fetch profile data
        viewModel.fetchProfile();


        return view;
    }
}