package com.ftn.eventhopper.fragments.messages.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ftn.eventhopper.clients.ClientUtils;
import com.ftn.eventhopper.shared.dtos.messages.ConversationPreviewDTO;
import com.ftn.eventhopper.shared.dtos.solutions.SolutionDetailsDTO;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConversationsListViewModel extends ViewModel {
    private final MutableLiveData<ArrayList<ConversationPreviewDTO>> conversationsLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public LiveData<ArrayList<ConversationPreviewDTO>> getConversations() {
        return conversationsLiveData;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void fetchConversations() {
        Call<ArrayList<ConversationPreviewDTO>> call = ClientUtils.messageService.getConversationsPreview();
        call.enqueue(new Callback<ArrayList<ConversationPreviewDTO>>() {
            @Override
            public void onResponse(Call<ArrayList<ConversationPreviewDTO>> call, Response<ArrayList<ConversationPreviewDTO>> response) {
                if (response.isSuccessful()) {
                    conversationsLiveData.postValue(response.body());

                } else {
                    errorMessage.postValue("Failed to fetch conversations. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ConversationPreviewDTO>> call, Throwable t) {
                errorMessage.postValue(t.getMessage());
            }
        });

    }

}
