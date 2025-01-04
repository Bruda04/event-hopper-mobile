package com.ftn.eventhopper.clients;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.ftn.eventhopper.BuildConfig;
import com.ftn.eventhopper.clients.deserializers.LocalDateAdapter;
import com.ftn.eventhopper.clients.deserializers.LocalDateTimeAdapter;
import com.ftn.eventhopper.clients.deserializers.LocalTimeAdapter;
import com.ftn.eventhopper.clients.services.categories.CategoriesService;
import com.ftn.eventhopper.clients.services.eventTypes.EventTypeService;
import com.ftn.eventhopper.clients.services.locations.LocationService;
import com.ftn.eventhopper.clients.services.solutions.ProductService;
import com.ftn.eventhopper.clients.services.solutions.ServiceService;
import com.ftn.eventhopper.clients.services.users.LoginService;
import com.ftn.eventhopper.clients.services.users.ProfileService;
import com.ftn.eventhopper.clients.interceptors.JWTInterceptor;
import com.ftn.eventhopper.clients.services.users.RegistrationService;
import com.ftn.eventhopper.clients.services.events.EventService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ClientUtils {

    public static final String SERVICE_API_PATH = "http://"+ BuildConfig.IP_ADDR +":8080/api/";
    public static final String SERVICE_API_IMAGE_PATH = "http://" + BuildConfig.IP_ADDR + ":8080/api/images";


    public static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(SERVICE_API_PATH)
            .addConverterFactory(GsonConverterFactory.create(setupGson()))
            .client(setupClient())
            .build();

    private static Gson setupGson() {
        return new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .registerTypeAdapter(LocalTime.class, new LocalTimeAdapter())
                .create();
    }

    public static OkHttpClient setupClient(){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        return new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .addInterceptor(new JWTInterceptor())
                .build();
    }

    public static EventService eventService = retrofit.create(EventService.class);
    public static ProductService productService = retrofit.create(ProductService.class);
    public static ProfileService profileService = retrofit.create(ProfileService.class);
    public static CategoriesService categoriesService = retrofit.create(CategoriesService.class);
    public static LoginService loginService = retrofit.create(LoginService.class);
    public static RegistrationService registrationService = retrofit.create(RegistrationService.class);
    public static LocationService locationService = retrofit.create(LocationService.class);
    public static EventTypeService eventTypeService = retrofit.create(EventTypeService.class);
    public static ServiceService serviceService = retrofit.create(ServiceService.class);

}
