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
import com.ftn.eventhopper.fragments.reports.viewmodels.ReportsViewModel;
import com.ftn.eventhopper.shared.dtos.reports.GetReportDTO;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class AdminsReportsAdapter extends RecyclerView.Adapter<AdminsReportsAdapter.AdminsGetReportDTOViewHolder> {

    ArrayList<GetReportDTO> reports;
    Context context;
    private final Fragment fragment;
    private ReportsViewModel viewModel;

    public AdminsReportsAdapter(Context context,
                                 ArrayList<GetReportDTO> reports,
                                 Fragment fragment,
                                 ReportsViewModel viewModel){

        this.reports = reports;
        this.context = context;
        this.fragment = fragment;
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public AdminsReportsAdapter.AdminsGetReportDTOViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        return new AdminsReportsAdapter.AdminsGetReportDTOViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_report, parent, false));
    }

    @Override
    public void onBindViewHolder(@NotNull AdminsReportsAdapter.AdminsGetReportDTOViewHolder holder, int position){

        holder.reporter.setText(reports.get(position).getReporter().getEmail());
        holder.reported.setText(reports.get(position).getReported().getEmail());
        holder.reason.setText((reports.get(position).getReason()));

        holder.suspendButton.setOnClickListener(v -> {
            setupSuspensionDialog(position);
        });

        holder.deleteButton.setOnClickListener(v -> {
            setupDeleteDialog(position);
        });

    }


    private void setupSuspensionDialog(int position) {
        MaterialAlertDialogBuilder suspensionDialog = new MaterialAlertDialogBuilder(context);
        suspensionDialog.setTitle("Suspend user");
        suspensionDialog.setMessage("Are you sure you want to suspend user \n"
                + reports.get(position).getReported().getEmail() + " ?");
        suspensionDialog.setPositiveButton("Suspend", (dialog, which) -> {
            viewModel.suspend(reports.get(position).getId());
        });
        suspensionDialog.setNegativeButton("Cancel", (dialog, which) -> {
        });
        suspensionDialog.show();
    }

    private void setupDeleteDialog(int position) {
        MaterialAlertDialogBuilder approveDialog = new MaterialAlertDialogBuilder(context);
        approveDialog.setTitle("Delete report");
        approveDialog.setMessage("Are you sure you want to Delete this report and not to suspend user\n"
                        + reports.get(position).getReported().getEmail() + " ?");
        approveDialog.setPositiveButton("Delete", (dialog, which) -> {
            viewModel.deleteReport(reports.get(position).getId());
        });
        approveDialog.setNegativeButton("Cancel", (dialog, which) -> {
        });
        approveDialog.show();
    }

    @Override
    public int getItemCount() { return reports.size(); }

    public class AdminsGetReportDTOViewHolder extends RecyclerView.ViewHolder {

        private final TextView reporter;
        private final TextView reported;
        private final TextView reason;
        public MaterialButton suspendButton;
        public MaterialButton deleteButton;

        public AdminsGetReportDTOViewHolder(@NonNull View itemView){
            super(itemView);
            this.reporter = itemView.findViewById(R.id.admins_report_card_reporter);
            this.reported = itemView.findViewById(R.id.admins_report_card_reported);
            this.reason = itemView.findViewById(R.id.admins_report_card_reason);
            this.suspendButton = itemView.findViewById(R.id.admins_report_card_suspend_button);
            this.deleteButton = itemView.findViewById(R.id.admins_report_card_delete_button);
        }
    }

}
