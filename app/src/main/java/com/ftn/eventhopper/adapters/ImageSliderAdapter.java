package com.ftn.eventhopper.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ftn.eventhopper.R;
import com.ftn.eventhopper.clients.ClientUtils;

import java.util.List;
import java.util.stream.Collectors;

public class ImageSliderAdapter extends RecyclerView.Adapter<ImageSliderAdapter.ImageViewHolder> {

    private final List<String> imageUrls;

    public ImageSliderAdapter(List<String> imageUrls) {

        this.imageUrls = imageUrls.stream()
                .map(image -> String.format("%s/%s", ClientUtils.SERVICE_API_IMAGE_PATH, image))
                .collect(Collectors.toList());
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.slider_image, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        // Load the image into the ImageView using Glide
        String imageUrl = imageUrls.get(position);
        Glide.with(holder.imageView.getContext())
                .load(imageUrl)
                .placeholder(R.drawable.baseline_image_placeholder_24)  // Optional: Placeholder
                .error(R.drawable.baseline_image_not_supported_24)        // Optional: Error image
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return imageUrls.size();
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ImageViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_slider_element);
        }
    }
}
