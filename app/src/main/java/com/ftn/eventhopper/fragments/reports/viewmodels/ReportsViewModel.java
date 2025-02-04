package com.ftn.eventhopper.fragments.reports.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ftn.eventhopper.clients.ClientUtils;
import com.ftn.eventhopper.shared.dtos.comments.SimpleCommentDTO;
import com.ftn.eventhopper.shared.dtos.reports.CreateReportDTO;
import com.ftn.eventhopper.shared.dtos.reports.GetReportDTO;
import com.ftn.eventhopper.shared.dtos.users.account.SimpleAccountDTO;

import java.util.ArrayList;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportsViewModel extends ViewModel {

    private final MutableLiveData<ArrayList<GetReportDTO>> reports = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();


    public LiveData<ArrayList<GetReportDTO>> getReports() {
        return reports;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void fetchReports(){

        Call<ArrayList<GetReportDTO>> call = ClientUtils.reportService.getReports();
        call.enqueue(new Callback<ArrayList<GetReportDTO>>() {
             @Override
             public void onResponse(Call<ArrayList<GetReportDTO>> call, Response<ArrayList<GetReportDTO>> response) {
                 if(response.isSuccessful()){
                     reports.postValue(response.body());
                 }else{
                     errorMessage.postValue("Failed to fetch reports. Code: " + response.code());
                 }
             }

             @Override
             public void onFailure(Call<ArrayList<GetReportDTO>> call, Throwable t) {
                 errorMessage.postValue(t.getMessage());
             }
         }
        );
    }

    public void deleteReport(UUID id){
        Call<Void> call = ClientUtils.reportService.delete(id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    fetchReports();
                    errorMessage.postValue(null);
                }else{
                    errorMessage.postValue("Failed to delete report. Code: "+ response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                errorMessage.postValue(t.getMessage());
            }
        });

    }

    public void suspend(UUID id){
        Call<Void> call = ClientUtils.reportService.suspend(id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    fetchReports();
                    errorMessage.postValue(null);
                }else{
                    errorMessage.postValue("Failed to suspend user. Code: "+ response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                errorMessage.postValue(t.getMessage());
            }
        });
    }

    public void create(String reason, SimpleAccountDTO reporter, SimpleAccountDTO reported){

        CreateReportDTO createReportDTO = new CreateReportDTO();
        createReportDTO.setReason(reason);
        createReportDTO.setReporter(reporter);
        createReportDTO.setReported(reported);

        Call<Void> call = ClientUtils.reportService.create(createReportDTO);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    fetchReports();
                    errorMessage.postValue(null);
                }else{
                    errorMessage.postValue("Failed to create report. Code: "+ response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                errorMessage.postValue(t.getMessage());
            }
        });
    }



}
