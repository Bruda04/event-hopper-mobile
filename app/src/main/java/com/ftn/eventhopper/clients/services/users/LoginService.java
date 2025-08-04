package com.ftn.eventhopper.clients.services.users;

import com.ftn.eventhopper.shared.dtos.login.LoginDTO;
import com.ftn.eventhopper.shared.dtos.login.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface LoginService {
    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @POST("login")
    Call<LoginResponse> loginUser(@Body LoginDTO loginDTO);
}
