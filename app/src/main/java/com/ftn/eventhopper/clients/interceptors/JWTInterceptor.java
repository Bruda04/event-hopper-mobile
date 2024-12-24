package com.ftn.eventhopper.clients.interceptors;

import com.ftn.eventhopper.clients.services.auth.UserService;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class JWTInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        Request.Builder requestBuilder = originalRequest.newBuilder();

        // Add Authorization header if JWT token is available
        String jwt = UserService.getJwtToken();
        if (jwt != null && !jwt.isEmpty()) {
            requestBuilder.addHeader("Authorization", "Bearer " + jwt);
        }

        Request modifiedRequest = requestBuilder.build();
        return chain.proceed(modifiedRequest);
    }

}
