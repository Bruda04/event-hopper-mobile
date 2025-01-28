package com.ftn.eventhopper.fragments.comments.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ftn.eventhopper.clients.ClientUtils;
import com.ftn.eventhopper.shared.dtos.categories.CategorySuggestionDTO;
import com.ftn.eventhopper.shared.dtos.comments.SimpleCommentDTO;

import java.util.ArrayList;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminsCommentsManagementViewModel extends ViewModel {

    private final MutableLiveData<ArrayList<SimpleCommentDTO>> pendingComments  = new MutableLiveData<>();

    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public LiveData<ArrayList<SimpleCommentDTO>> getPendingComments() {
        return pendingComments;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }


    public void fetchPendingComments() {
        Call<ArrayList<SimpleCommentDTO>> call = ClientUtils.commentsService.getPendingComments();
        call.enqueue(new Callback<ArrayList<SimpleCommentDTO>>() {
             @Override
             public void onResponse(Call<ArrayList<SimpleCommentDTO>> call, Response<ArrayList<SimpleCommentDTO>> response) {
                if(response.isSuccessful()){
                    pendingComments.postValue(response.body());
                }else{
                    errorMessage.postValue("Failed to fetch pending comments. Code: " + response.code());
                }
             }

             @Override
             public void onFailure(Call<ArrayList<SimpleCommentDTO>> call, Throwable t) {
                    errorMessage.postValue(t.getMessage());
             }
         }

        );
    }
    public void approveComment(UUID id){
        Call<Void> call = ClientUtils.commentsService.approveComment(id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    fetchPendingComments();
                }else{
                    errorMessage.postValue("Failed to approve comment. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                    errorMessage.postValue(t.getMessage());
            }
        });
    }

    public void deleteComment(UUID id){
        Call<Void> call = ClientUtils.commentsService.deleteComment(id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    fetchPendingComments();
                }else{
                    errorMessage.postValue("Failed to delete comment. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                errorMessage.postValue(t.getMessage());
            }
        });
    }
}
