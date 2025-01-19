package com.ftn.eventhopper.fragments.profile;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ftn.eventhopper.R;
import com.ftn.eventhopper.activities.MainActivity;
import com.ftn.eventhopper.adapters.ImagePreviewAdapter;
import com.ftn.eventhopper.clients.ClientUtils;
import com.ftn.eventhopper.clients.services.auth.UserService;
import com.ftn.eventhopper.clients.services.users.ProfileService;
import com.ftn.eventhopper.fragments.login.viewmodels.LoginViewModel;
import com.ftn.eventhopper.fragments.profile.viewmodels.ProfileViewModel;
import com.ftn.eventhopper.shared.models.users.PersonType;

import java.io.IOException;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    private ProfileViewModel viewModel;

    private PersonType role = UserService.getUserRole();

    private ImageView profileImage;


    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);


        NavController navController = NavHostFragment.findNavController(this);

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
            Intent intent = new Intent(getContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);  // Optional: Clear the activity stack
            startActivity(intent);

        });


        this.profileImage = view.findViewById(R.id.profileImage);
        profileImage.setOnClickListener(v -> {
            showImageDialog(view);
        });

        TextView companyName = view.findViewById(R.id.CompanyName);
        TextView companyAddress = view.findViewById(R.id.companyAddress);
        TextView companyDescription = view.findViewById(R.id.companyDescription);
        TextView companyEmail = view.findViewById(R.id.companyEmail);

        TextView roleTitle = view.findViewById(R.id.role);
        TextView userName = view.findViewById(R.id.userName);
        TextView userAddress = view.findViewById(R.id.userAddress);
        TextView userEmail = view.findViewById(R.id.userEmail);

        View companyInfoSection = view.findViewById(R.id.company_information);
        companyInfoSection.setVisibility(UserService.getUserRole() == PersonType.SERVICE_PROVIDER ? View.VISIBLE : View.GONE);

        viewModel.getProfileData().observe(getViewLifecycleOwner(), profile -> {
            if (profile != null) {
                if(this.role == PersonType.SERVICE_PROVIDER){
                    companyName.setText(profile.getCompanyName());
                    companyAddress.setText(profile.getCompanyLocation() != null ? profile.getCompanyLocation().getAddress() + ", " + profile.getCompanyLocation().getCity() : "Not found");
                    companyDescription.setText(profile.getCompanyDescription());
                    companyEmail.setText(profile.getCompanyEmail());
                }

                if(this.role == PersonType.SERVICE_PROVIDER){
                    roleTitle.setText("Service Provider");
                }
                if(this.role == PersonType.EVENT_ORGANIZER){
                    roleTitle.setText("Organizer");
                }
                if(this.role == PersonType.ADMIN){
                    roleTitle.setText("Admin");
                }
                if(this.role == PersonType.AUTHENTICATED_USER){
                    roleTitle.setText("User");
                }



                // Populate User Info
                userName.setText(String.format("%s %s", profile.getName(), profile.getSurname()));
                userAddress.setText(profile.getLocation() != null ? profile.getLocation().getAddress() + "," +  profile.getLocation().getCity() : "N/A");
                userEmail.setText(profile.getEmail());


                String profilePicUrl = profile.getProfilePicture();
                String fullProfileImageUrl = (profilePicUrl == null || profilePicUrl.isEmpty())
                        ? String.format("%s/%s", ClientUtils.SERVICE_API_IMAGE_PATH, "profile.png")
                        : String.format("%s/%s", ClientUtils.SERVICE_API_IMAGE_PATH, profilePicUrl);

                Glide.with(requireContext())
                        .load(fullProfileImageUrl)
                        .circleCrop()
                        .into(profileImage);
            }
        });

        viewModel.fetchProfile();


        return view;
    }


    private void showImageDialog(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_profile_picture, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();
        TextView uploadOption = dialogView.findViewById(R.id.uploadOption);
        TextView removeOption = dialogView.findViewById(R.id.removeOption);

        uploadOption.setOnClickListener(v -> {
            changeProfilePicture(view);
            dialog.dismiss();
        });

        removeOption.setOnClickListener(v -> {
            removeProfilePicture(view);
            dialog.dismiss();
        });

        dialog.show();
    }


    private void removeProfilePicture(View view){
        ImageView profileImage = view.findViewById(R.id.profileImage);
        Glide.with(requireContext())
                .load(String.format("%s/%s", ClientUtils.SERVICE_API_IMAGE_PATH, "profile.png"))
                .circleCrop()
                .into(profileImage);
        viewModel.removeProfilePicture();
    }

    private void changeProfilePicture(View view) {
        // Open the gallery to allow the user to pick an image
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*"); // Restrict to image files
        startActivityForResult(intent, 1);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImageUri);

                viewModel.changeProfilePicture(bitmap);

                viewModel.getImageUrlLiveData().observe(getViewLifecycleOwner(), newImageUrl -> {
                    if (newImageUrl != null) {
                        // Update the UI with the new profile picture
                        Glide.with(requireContext())
                                .load(String.format("%s/%s", ClientUtils.SERVICE_API_IMAGE_PATH, newImageUrl))
                                .circleCrop()
                                .into(profileImage);
                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



}