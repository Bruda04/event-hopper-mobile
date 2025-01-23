package com.ftn.eventhopper.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ftn.eventhopper.R;
import com.ftn.eventhopper.clients.ClientUtils;
import com.ftn.eventhopper.shared.dtos.comments.SimpleCommentDTO;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.SimpleCommentDTOViewHolder> {
    private List<SimpleCommentDTO> comments;
    private Context context;

    public CommentAdapter(List<SimpleCommentDTO> comments, Context context) {
        this.comments = comments;
        this.context = context;
    }

    @NonNull
    @Override
    public SimpleCommentDTOViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, parent, false);
        return new SimpleCommentDTOViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SimpleCommentDTOViewHolder holder, int position) {
        SimpleCommentDTO comment = comments.get(position);

        if (comment.getAuthor().getProfilePicture() == null || comment.getAuthor().getProfilePicture().isEmpty()) {
            holder.profilePicture.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.profile_pic_temp));
        } else {
            String profilePictureUrl = String.format("%s/%s", ClientUtils.SERVICE_API_IMAGE_PATH, comment.getAuthor().getProfilePicture());
            Glide.with(holder.itemView.getContext())
                    .load(profilePictureUrl)
                    .circleCrop()
                    .placeholder(R.drawable.baseline_image_placeholder_24)  // Optional: Placeholder
                    .error(R.drawable.baseline_image_not_supported_24)        // Optional: Error image
                    .into(holder.profilePicture);
        }
        // Set name and content
        holder.commenterName.setText(String.format("%s %s",comment.getAuthor().getName(), comment.getAuthor().getSurname()));
        holder.commentContent.setText(comment.getContent());
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public static class SimpleCommentDTOViewHolder extends RecyclerView.ViewHolder {
        ImageView profilePicture;
        TextView commenterName;
        TextView commentContent;

        public SimpleCommentDTOViewHolder(View itemView) {
            super(itemView);
            profilePicture = itemView.findViewById(R.id.profile_picture);
            commenterName = itemView.findViewById(R.id.commenter_name);
            commentContent = itemView.findViewById(R.id.comment_content);
        }
    }
}
