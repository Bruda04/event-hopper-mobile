package com.ftn.eventhopper.clients.services.blocks;

import com.ftn.eventhopper.shared.dtos.blocks.CreateBlockDTO;
import com.ftn.eventhopper.shared.dtos.blocks.GetBlockDTO;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface BlockService {

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("blocking")
    Call<ArrayList<GetBlockDTO>> getBlocks();

    @Headers({
        "User-Agent: Mobile-Android",
                "Content-Type:application/json"
    })
    @POST("blocking")
    Call<Void> create(@Body CreateBlockDTO block);

}
