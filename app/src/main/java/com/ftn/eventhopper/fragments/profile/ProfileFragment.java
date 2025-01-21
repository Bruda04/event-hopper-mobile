package com.ftn.eventhopper.fragments.profile;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

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
        viewModel = new ViewModelProvider(requireActivity()).get(ProfileViewModel.class);

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

        view.findViewById(R.id.ListItemDeactivateProfile).setOnClickListener(v -> {
            this.openDeactivateAccountDialog();
        });


        view.findViewById(R.id.ListItemLogOut).setOnClickListener(v -> {
            viewModel.logout();
            ((MainActivity) requireActivity()).navigateToAuthGraph();
        });


        this.profileImage = view.findViewById(R.id.profileImage);
        profileImage.setOnClickListener(v -> {
            showImageDialog(view);
        });


        TextView roleTitle = view.findViewById(R.id.role);
        TextView userName = view.findViewById(R.id.userName);
        TextView userAddress = view.findViewById(R.id.userAddress);
        TextView userEmail = view.findViewById(R.id.userEmail);

        viewModel.getProfileData().observe(getViewLifecycleOwner(), profile -> {
            if (profile != null) {
                if(this.role == PersonType.SERVICE_PROVIDER){
                    roleTitle.setText(R.string.provider);
                }
                if(this.role == PersonType.EVENT_ORGANIZER){
                    roleTitle.setText(R.string.organizer);
                }
                if(this.role == PersonType.ADMIN){
                    roleTitle.setText(R.string.admin);
                }
                if(this.role == PersonType.AUTHENTICATED_USER){
                    roleTitle.setText(R.string.user);
                }


                // Populate User Info
                userName.setText(String.format("%s %s", profile.getName(), profile.getSurname()));
                userAddress.setText(profile.getLocation() != null ? profile.getLocation().getAddress() + ", " +  profile.getLocation().getCity() : "N/A");
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



        CardView changePasswordCard = view.findViewById(R.id.ListItemChangePassword);
        CardView myProductsCard = view.findViewById(R.id.ListItemMyProducts);
        CardView myServicesCard = view.findViewById(R.id.ListItemMyServices);
        CardView myPricesCard = view.findViewById(R.id.ListItemMyPrices);
        CardView categoriesCard = view.findViewById(R.id.ListItemCategories);
        CardView eventTypesCard = view.findViewById(R.id.ListItemEventTypes);
        CardView myEventsCard = view.findViewById(R.id.ListItemMyEvents);
        CardView logOutCard = view.findViewById(R.id.ListItemLogOut);
        CardView deactivateProfileCard = view.findViewById(R.id.ListItemDeactivateProfile);
        CardView upgradeProfileCard = view.findViewById(R.id.ListItemUpgradeProfile);
        CardView commentsCard = view.findViewById(R.id.ListItemComments);

        myProductsCard.setVisibility(View.GONE);
        myServicesCard.setVisibility(View.GONE);
        myPricesCard.setVisibility(View.GONE);
        categoriesCard.setVisibility(View.GONE);
        eventTypesCard.setVisibility(View.GONE);
        myEventsCard.setVisibility(View.GONE);
        upgradeProfileCard.setVisibility(View.GONE);
        commentsCard.setVisibility(View.GONE);

        switch (role.toString()) {
            case "SERVICE_PROVIDER":
                myProductsCard.setVisibility(View.VISIBLE);
                myServicesCard.setVisibility(View.VISIBLE);
                myPricesCard.setVisibility(View.VISIBLE);
                break;

            case "ADMIN":
                categoriesCard.setVisibility(View.VISIBLE);
                eventTypesCard.setVisibility(View.VISIBLE);
                commentsCard.setVisibility(View.VISIBLE);
                break;

            case "EVENT_ORGANIZER":
                myEventsCard.setVisibility(View.VISIBLE);
                break;

            default:    //authenticated user
                upgradeProfileCard.setVisibility(View.VISIBLE);
                break;
        }

        changePasswordCard.setVisibility(View.VISIBLE);
        logOutCard.setVisibility(View.VISIBLE);
        deactivateProfileCard.setVisibility(View.VISIBLE);

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


    private void openDeactivateAccountDialog() {
        MaterialAlertDialogBuilder confirmDialog = new MaterialAlertDialogBuilder(requireContext());
        confirmDialog.setTitle("Are you sure you want to deactivate your account?");
        confirmDialog.setMessage("This action can't be undone.");
        confirmDialog.setPositiveButton("Yes", (dialog, which) -> {
            viewModel.deactivateAccount();
            viewModel.getDeactivateAccountSuccess().observe(getViewLifecycleOwner(), success -> {
                if (success) {
                    ((MainActivity) requireActivity()).navigateToAuthGraph();
                }else{
                    String failureMessage;
                    if(this.role == PersonType.SERVICE_PROVIDER){
                        failureMessage = "Failed: You have upcoming bookings.";
                    }else if(this.role == PersonType.EVENT_ORGANIZER){
                        failureMessage = "Failed: You have upcoming events.";
                    }else{
                        failureMessage = "Account deactivation failed, try again later.";
                    }
                    Toast.makeText(requireContext(), failureMessage, Toast.LENGTH_LONG).show();
                }
            });
        });
        confirmDialog.setNegativeButton("No", (dialog, which) -> {
        });
        confirmDialog.show();
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