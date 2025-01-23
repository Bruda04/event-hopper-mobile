package com.ftn.eventhopper.clients.services.messages;

import com.ftn.eventhopper.shared.dtos.messages.ChatMessageDTO;
import com.ftn.eventhopper.shared.dtos.messages.ConversationPreviewDTO;
import com.ftn.eventhopper.shared.dtos.solutions.ServiceManagementDTO;
import com.ftn.eventhopper.shared.responses.PagedResponse;

import java.util.ArrayList;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface MessageService {

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("chat/conversations-preview")
    Call<ArrayList<ConversationPreviewDTO>> getConversationsPreview();

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("chat/history/{username}")
    Call<ArrayList<ChatMessageDTO>> getChatHistory(@Path("username") String username);


}
