package com.ftn.eventhopper.adapters;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ftn.eventhopper.R;
import com.ftn.eventhopper.clients.ClientUtils;

import java.util.List;

public class ImagePreviewAdapter extends RecyclerView.Adapter<ImagePreviewAdapter.ViewHolder> {

    private final List<ImagePreviewItem> items;
    private final OnImageRemoveListener listener;

    public interface OnImageRemoveListener {
        void onImageRemove(int position);
    }

    public ImagePreviewAdapter(List<ImagePreviewItem> items, OnImageRemoveListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_preview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ImagePreviewItem item = items.get(position);

        if (item.isBitmap()) {
            // Load Bitmap
            holder.imageView.setImageBitmap(item.getBitmap());
        } else {
            // Load URL with Glide
            String url = String.format("%s/%s", ClientUtils.SERVICE_API_IMAGE_PATH, item.getImageUrl());
            Glide.with(holder.imageView.getContext())
                    .load(url)
                    .placeholder(R.drawable.baseline_image_placeholder_24) // Optional: Placeholder
                    .error(R.drawable.baseline_image_not_supported_24)     // Optional: Error image
                    .into(holder.imageView);
        }

        holder.removeButton.setOnClickListener(v -> listener.onImageRemove(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        ImageButton removeButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.preview_image);
            removeButton = itemView.findViewById(R.id.remove_image_button);
        }
    }

    public static class ImagePreviewItem {
        private Bitmap bitmap;
        private String imageUrl;

        public ImagePreviewItem(Bitmap bitmap) {
            this.bitmap = bitmap;
        }

        public ImagePreviewItem(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public Bitmap getBitmap() {
            return bitmap;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public boolean isBitmap() {
            return bitmap != null;
        }
    }
}
