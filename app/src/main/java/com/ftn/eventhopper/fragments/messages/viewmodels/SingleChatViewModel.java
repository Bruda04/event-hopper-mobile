package com.ftn.eventhopper.fragments.messages.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ftn.eventhopper.clients.ClientUtils;
import com.ftn.eventhopper.clients.services.auth.UserService;
import com.ftn.eventhopper.clients.webSockets.channelHandlers.IChannelHandler;
import com.ftn.eventhopper.shared.dtos.messages.ChatMessageDTO;
import com.ftn.eventhopper.shared.dtos.messages.ConversationPreviewDTO;
import com.ftn.eventhopper.shared.dtos.messages.NewChatMessageDTO;
import com.google.gson.Gson;

import java.util.ArrayList;

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

    @Getter
    @Setter
    private String receiverUsername;

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
}
