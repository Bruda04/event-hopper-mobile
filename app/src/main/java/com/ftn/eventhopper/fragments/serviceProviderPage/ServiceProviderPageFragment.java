package com.ftn.eventhopper.fragments.serviceProviderPage;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ftn.eventhopper.R;
import com.ftn.eventhopper.adapters.CommentAdapter;
import com.ftn.eventhopper.adapters.ImageSliderAdapter;
import com.ftn.eventhopper.clients.ClientUtils;
import com.ftn.eventhopper.clients.services.auth.UserService;
import com.ftn.eventhopper.fragments.serviceProviderPage.viewmodels.ServiceProviderPageViewModel;
import com.ftn.eventhopper.shared.dtos.users.serviceProvider.ServiceProviderDetailsDTO;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ServiceProviderPageFragment extends Fragment {
    private static final String ARG_ID = "id";
    private ServiceProviderPageViewModel viewModel;
    private UUID id;
    private ImageView profilePicture;
    private TextView companyName;
    private MaterialButton blockButton;
    private MaterialButton reportButton;
    private ViewPager2 companyImagesSlider;
    private TextView companyDescription;
    private TextView companyOwner;
    private TextView companyWorkingHours;
    private TextView companyLocation;
    private TextView companyEmail;
    private TextView companyPhone;
    private TextView companyRating;
    private RecyclerView companyComments;
    private TextView statusMessage;


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_service_provider_page, container, false);

        viewModel = new ViewModelProvider(this).get(ServiceProviderPageViewModel.class);
        statusMessage = view.findViewById(R.id.status_message);
        statusMessage.setText(R.string.loading_provider_details);
        statusMessage.setVisibility(View.VISIBLE);

        if (getArguments() != null) {
            id = UUID.fromString(getArguments().getString(ARG_ID));
            viewModel.fetchProviderDetailsById(id);

            profilePicture = view.findViewById(R.id.provider_profile_picture);
            companyName = view.findViewById(R.id.company_name);
            blockButton = view.findViewById(R.id.block_user_button);
            reportButton = view.findViewById(R.id.report_user_button);
            companyImagesSlider = view.findViewById(R.id.provider_company_image_slider);
            companyDescription = view.findViewById(R.id.provider_company_description);
            companyOwner = view.findViewById(R.id.provider_owner);
            companyWorkingHours = view.findViewById(R.id.provider_company_working_hours);
            companyLocation = view.findViewById(R.id.provider_company_location);
            companyEmail = view.findViewById(R.id.provider_company_email);
            companyPhone = view.findViewById(R.id.provider_company_phone);
            companyRating = view.findViewById(R.id.provider_rating);
            companyComments = view.findViewById(R.id.provider_comments_recyclerview);

        }

        viewModel.getProviderDetails().observe(getViewLifecycleOwner(), provider -> {
            if (provider != null) {
                statusMessage.setVisibility(View.GONE);
                this.setFieldValues(provider);
            }
        });

        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                Log.e("EventHopper", "Error fetching provider details: " + error);
                statusMessage.setText(R.string.oops_something_went_wrong_please_try_again_later);
                statusMessage.setVisibility(View.VISIBLE);
            }
        });

        return view;
    }

    private void setFieldValues(ServiceProviderDetailsDTO provider) {
        Glide.with(this)
                .load(String.format("%s/%s", ClientUtils.SERVICE_API_IMAGE_PATH, provider.getProfilePicture()))
                .circleCrop()
                .placeholder(R.drawable.baseline_image_placeholder_24)
                .error(R.drawable.baseline_image_not_supported_24)
                .into(profilePicture);

        String[] imageUrls = provider.getCompanyPhotos().toArray(String[]::new);
        ImageSliderAdapter imageSliderAdapter = new ImageSliderAdapter(List.of(imageUrls));
        companyImagesSlider.setAdapter(imageSliderAdapter);

        companyName.setText(provider.getCompanyName());

        companyDescription.setText(provider.getCompanyDescription());

        companyOwner.setText(String.format("Owner: %s %s", provider.getName(), provider.getSurname()));

        companyWorkingHours.setText(String.format("Working hours: %02d:%02d - %02d:%02d",
                provider.getWorkStart().getHour(), provider.getWorkStart().getMinute(),
                provider.getWorkEnd().getHour(), provider.getWorkEnd().getMinute()));

        companyLocation.setText(String.format("Location: %s, %s",
                provider.getCompanyLocation().getAddress(), provider.getCompanyLocation().getCity()));

        companyEmail.setText(String.format("Company email: %s", provider.getCompanyEmail()));

        companyPhone.setText(String.format("Company phone: %s", provider.getCompanyPhoneNumber()));

        int filledStars = (int) Math.floor(provider.getRating());
        int emptyStars = 5 - filledStars;
        StringBuilder ratingText = new StringBuilder();
        for (int i = 0; i < filledStars; i++) {
            ratingText.append("★");
        }
        for (int i = 0; i < emptyStars; i++) {
            ratingText.append("☆");
        }
        companyRating.setText(ratingText.toString());

        CommentAdapter commentsAdapter = new CommentAdapter(new ArrayList<>(provider.getComments()));
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        companyComments.setLayoutManager(layoutManager);
        companyComments.setItemAnimator(new DefaultItemAnimator());
        companyComments.setAdapter(commentsAdapter);

        if (!UserService.getJwtToken().isEmpty()) {
            blockButton.setVisibility(View.VISIBLE);
            reportButton.setVisibility(View.VISIBLE);

            blockButton.setOnClickListener(v -> {
                viewModel.blockUser();
            });

            reportButton.setOnClickListener(v -> {
                viewModel.reportUser();
            });
        }
    }
}