package com.ftn.eventhopper.fragments.messages.viewmodels;

import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.NavInflater;
import androidx.navigation.fragment.NavHostFragment;

import com.ftn.eventhopper.R;
import com.ftn.eventhopper.clients.ClientUtils;
import com.ftn.eventhopper.clients.services.auth.UserService;
import com.ftn.eventhopper.clients.webSockets.channelHandlers.IChannelHandler;
import com.ftn.eventhopper.fragments.HostFragment;
import com.ftn.eventhopper.shared.dtos.blocks.CreateBlockDTO;
import com.ftn.eventhopper.shared.dtos.messages.ChatMessageDTO;
import com.ftn.eventhopper.shared.dtos.messages.ConversationPreviewDTO;
import com.ftn.eventhopper.shared.dtos.messages.NewChatMessageDTO;
import com.ftn.eventhopper.shared.dtos.reports.CreateReportDTO;
import com.ftn.eventhopper.shared.dtos.users.account.SimpleAccountDTO;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SingleChatViewModel extends ViewModel {
    private final MutableLiveData<ArrayList<ChatMessageDTO>> historyLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public LiveData<ArrayList<ChatMessageDTO>> getHistory() {
        return historyLiveData;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public MutableLiveData<SimpleAccountDTO> receiver = new MutableLiveData<>();

    @Getter
    @Setter
    private String receiverUsername;

    public LiveData<SimpleAccountDTO> getReceiverLiveData() {
        return receiver;
    }
    public void fetchHistory() {
        Call<ArrayList<ChatMessageDTO>> call = ClientUtils.messageService.getChatHistory(receiverUsername);
        call.enqueue(new Callback<ArrayList<ChatMessageDTO>>() {
            @Override
            public void onResponse(Call<ArrayList<ChatMessageDTO>> call, Response<ArrayList<ChatMessageDTO>> response) {
                if (response.isSuccessful()) {
                    historyLiveData.postValue(response.body());

                } else {
                    errorMessage.postValue("Failed to fetch history. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ChatMessageDTO>> call, Throwable t) {
                errorMessage.postValue(t.getMessage());
            }
        });

    }

    public void sendMessage(String content) {
        NewChatMessageDTO message = new NewChatMessageDTO();
        message.setContent(content);
        message.setRecipient(receiverUsername);
        message.setSender(UserService.getJwtClaim(UserService.getJwtToken(), "sub"));

        ClientUtils.sendMessage("/app/chat", ClientUtils.setupGson().toJson(message, NewChatMessageDTO.class));
    }

    public void onMessageReceived(ChatMessageDTO message) {
        if (!message.getSender().equals(receiverUsername) && !message.getRecipient().equals(receiverUsername)) {
            return;
        }

        ArrayList<ChatMessageDTO> history = historyLiveData.getValue();
        if (history == null) {
            history = new ArrayList<>();
        }
        history.add(message);
        historyLiveData.postValue(history);
    }

    public void connectToChat() {
        ClientUtils.setMessageHandler(new IChannelHandler() {
            @Override
            public void onMessage(String message) {
                Log.d("SingleChatViewModel", "Message received: " + message);
                ChatMessageDTO chatMessageDTO = ClientUtils.setupGson().fromJson(message, ChatMessageDTO.class);
                onMessageReceived(chatMessageDTO);
            }
        });
    }

    public void disconnectChat() {
        ClientUtils.setMessageHandler(null);
    }


    public void createReport(String reason, UUID reported){

        CreateReportDTO createReportDTO = new CreateReportDTO();
        createReportDTO.setReason(reason);
        createReportDTO.setReported(reported);

        Call<Void> call = ClientUtils.reportService.create(createReportDTO);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
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

    public void createBlock(UUID blocked){

        CreateBlockDTO createBlockDTO = new CreateBlockDTO();
        createBlockDTO.setBlocked(blocked);

        Call<Void> call = ClientUtils.blockService.create(createBlockDTO);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    errorMessage.postValue(null);
                }else{
                    errorMessage.postValue("Failed to create block. Code: "+ response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                errorMessage.postValue(t.getMessage());
            }
        });

    }


    public void fetchReceiverByEmail(String email) {

        Call<SimpleAccountDTO> call = ClientUtils.profileService.getActiveByEmail(email);
        call.enqueue(new Callback<SimpleAccountDTO>() {

            @Override
            public void onResponse(Call<SimpleAccountDTO> call, Response<SimpleAccountDTO> response) {
                if(response.isSuccessful()){
                    receiver.setValue(response.body());
                    errorMessage.postValue(null);
                }else{
                    errorMessage.postValue("Failed to fetch user. Code: "+ response.code());

                }
            }

            @Override
            public void onFailure(Call<SimpleAccountDTO> call, Throwable t) {
                errorMessage.postValue(t.getMessage());
            }
        });
    }

}
