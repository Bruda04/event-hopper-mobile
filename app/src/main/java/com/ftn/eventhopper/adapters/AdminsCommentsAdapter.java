package com.ftn.eventhopper.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.ftn.eventhopper.R;
import com.ftn.eventhopper.fragments.comments.viewmodels.AdminsCommentsManagementViewModel;
import com.ftn.eventhopper.shared.dtos.comments.SimpleCommentDTO;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class AdminsCommentsAdapter extends RecyclerView.Adapter<AdminsCommentsAdapter.AdminsSimpleCommentsDTOViewHolder> {

    ArrayList<SimpleCommentDTO> comments;
    Context context;
    private final Fragment fragment;
    private AdminsCommentsManagementViewModel viewModel;

    public AdminsCommentsAdapter(Context context,
                                 ArrayList<SimpleCommentDTO> comments,
                                 Fragment fragment,
                                 AdminsCommentsManagementViewModel viewModel){

        this.comments = comments;
        this.context = context;
        this.fragment = fragment;
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public AdminsCommentsAdapter.AdminsSimpleCommentsDTOViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        return new AdminsCommentsAdapter.AdminsSimpleCommentsDTOViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_pups_service, parent, false));
    }

    @Override
    public void onBindViewHolder(@NotNull AdminsCommentsAdapter.AdminsSimpleCommentsDTOViewHolder holder, int position){
        holder.author.setText(comments.get(position).getAuthor().getName() + " " + comments.get(position).getAuthor().getSurname());
        holder.commentContent.setText(comments.get(position).getContent());


        holder.approveButton.setOnClickListener(v -> {
            setupApproveDialog(position);
        });

        holder.deleteButton.setOnClickListener(v -> {
            setupDeleteDialog(position);
        });

    }


    private void setupApproveDialog(int position) {
        MaterialAlertDialogBuilder approveDialog = new MaterialAlertDialogBuilder(context);
        approveDialog.setTitle("Approve comment");
        approveDialog.setMessage("Are you sure you want to approve this comment?");
        approveDialog.setPositiveButton("Approve", (dialog, which) -> {
            viewModel.approveComment(comments.get(position).getId());
        });
        approveDialog.setNegativeButton("Cancel", (dialog, which) -> {
        });
        approveDialog.show();
    }

    private void setupDeleteDialog(int position) {
        MaterialAlertDialogBuilder approveDialog = new MaterialAlertDialogBuilder(context);
        approveDialog.setTitle("Delete comment");
        approveDialog.setMessage("Are you sure you want to Delete this comment?");
        approveDialog.setPositiveButton("Delete", (dialog, which) -> {
            viewModel.approveComment(comments.get(position).getId());
        });
        approveDialog.setNegativeButton("Cancel", (dialog, which) -> {
        });
        approveDialog.show();
    }

    @Override
    public int getItemCount() { return comments.size(); }

    public class AdminsSimpleCommentsDTOViewHolder extends RecyclerView.ViewHolder {

        private final TextView author;
        private final TextView commentContent;
        public MaterialButton approveButton;
        public MaterialButton deleteButton;

        public AdminsSimpleCommentsDTOViewHolder(@NonNull View itemView){
            super(itemView);
            this.author = itemView.findViewById(R.id.admins_pending_comment_card_author);
            this.commentContent = itemView.findViewById(R.id.admins_pending_comment_content);
            this.approveButton = itemView.findViewById(R.id.admins_pending_comment_card_approve_button);
            this.deleteButton = itemView.findViewById(R.id.admins_pending_comment_card_delete_button);
        }
    }


}
