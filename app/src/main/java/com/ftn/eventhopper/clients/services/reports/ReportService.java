package com.ftn.eventhopper.clients.services.reports;

import com.ftn.eventhopper.shared.dtos.reports.CreateReportDTO;
import com.ftn.eventhopper.shared.dtos.reports.GetReportDTO;

import java.util.ArrayList;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ReportService {

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("reports")
    Call<ArrayList<GetReportDTO>> getReports();

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @DELETE("reports/delete/{id}")
    Call<Void> delete(@Path("id")UUID id);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @DELETE("reports/suspend/{id}")
    Call<Void> suspend(@Path("id") UUID id);


    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @POST("reports")
    Call<Void> create(@Body CreateReportDTO report);

}
