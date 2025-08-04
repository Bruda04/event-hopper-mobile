package com.ftn.eventhopper.clients.services.images;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ImageService {
    @Multipart
    @POST("images")
    Call<String> uploadImage(@Part MultipartBody.Part image);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @DELETE("images/{imageName}")
    Call<Void> deleteImage(@Path("imageName") String imageName);
}
