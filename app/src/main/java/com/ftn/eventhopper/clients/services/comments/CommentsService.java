package com.ftn.eventhopper.clients.services.comments;

import com.ftn.eventhopper.shared.dtos.categories.UpdateCategoryDTO;
import com.ftn.eventhopper.shared.dtos.comments.SimpleCommentDTO;

import java.util.ArrayList;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface CommentsService {

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("comments/pending")
    Call<ArrayList<SimpleCommentDTO>> getPendingComments();

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @PUT("comments/pending/{id}/approve")
    Call<Void> approveComment(@Path("id")UUID id);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @PUT("comments/pending/{id}/delete")
    Call<Void> deleteComment(@Path("id")UUID id);


}
