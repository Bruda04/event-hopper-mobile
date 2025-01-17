package com.ftn.eventhopper.clients;


import android.util.Log;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

import com.ftn.eventhopper.BuildConfig;
import com.ftn.eventhopper.clients.deserializers.LocalDateAdapter;
import com.ftn.eventhopper.clients.deserializers.LocalDateTimeAdapter;
import com.ftn.eventhopper.clients.deserializers.LocalTimeAdapter;
import com.ftn.eventhopper.clients.services.auth.UserService;
import com.ftn.eventhopper.clients.services.categories.CategoriesService;
import com.ftn.eventhopper.clients.services.eventTypes.EventTypeService;
import com.ftn.eventhopper.clients.services.invitations.InvitationService;
import com.ftn.eventhopper.clients.services.images.ImageService;
import com.ftn.eventhopper.clients.services.locations.LocationService;
import com.ftn.eventhopper.clients.services.messages.MessageService;
import com.ftn.eventhopper.clients.services.solutions.ProductService;
import com.ftn.eventhopper.clients.services.solutions.ServiceService;
import com.ftn.eventhopper.clients.services.users.LoginService;
import com.ftn.eventhopper.clients.services.users.ProfileService;
import com.ftn.eventhopper.clients.interceptors.JWTInterceptor;
import com.ftn.eventhopper.clients.services.events.EventService;
import com.ftn.eventhopper.clients.services.users.RegistrationService;
import com.ftn.eventhopper.clients.webSockets.channelHandlers.IChannelHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import lombok.Getter;
import lombok.Setter;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;
import ua.naiksoftware.stomp.dto.StompHeader;

public class ClientUtils {

    public static final String SERVICE_API_PATH = "http://"+ BuildConfig.IP_ADDR +":8080/api/";
    public static final String SERVICE_API_IMAGE_PATH = "http://" + BuildConfig.IP_ADDR + ":8080/api/images";
    public static final String WEBSOCKET_PATH = "http://" + BuildConfig.IP_ADDR + ":8080/api/socket"; // WebSocket path

    private static final StompClient stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, WEBSOCKET_PATH);

    public static void connectWebSocket() {
        ArrayList<StompHeader> headers = new ArrayList<>();
        headers.add(new StompHeader("Authorization", String.format("Bearer %s", UserService.getJwtToken())));
        stompClient.connect(headers);
        Disposable d = stompClient.lifecycle().subscribe(
                lifecycleEvent -> {
                    switch (lifecycleEvent.getType()) {
                        case OPENED:
                            Log.d("WebSocket", "Stomp connection opened");
                            subscribeToDefaultTopics();
                            break;
                        case ERROR:
                            Log.e("WebSocket", "Error", lifecycleEvent.getException());
                            break;
                        case CLOSED:
                            Log.d("WebSocket", "Stomp connection closed");
                            break;
                    }
                }
        );

    }

    public static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(SERVICE_API_PATH)
            .addConverterFactory(GsonConverterFactory.create(setupGson()))
            .client(setupClient())
            .build();

    public static Gson setupGson() {
        return new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .registerTypeAdapter(LocalTime.class, new LocalTimeAdapter())
                .setLenient()
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

    @Getter
    @Setter
    private static IChannelHandler messageHandler;
    @Getter
    @Setter
    private static IChannelHandler notificationHandler;

    private static void subscribeToDefaultTopics() {
        subscribeToWebSocketTopic("/user/topic/notifications", new IChannelHandler() {
            @Override
            public void onMessage(String message) {
                Log.d("WebSocket","Received notification: " + message);
                if (notificationHandler != null) {
                    notificationHandler.onMessage(message);
                }
            }
        });
        subscribeToWebSocketTopic("/user/topic/chat", new IChannelHandler() {
            @Override
            public void onMessage(String message) {
                Log.d("WebSocket","Received message: " + message);
                if (messageHandler != null) {
                    messageHandler.onMessage(message);
                }
            }
        });
    }

    public static void subscribeToWebSocketTopic(String topic, IChannelHandler messageHandler) {
        Disposable d = stompClient.topic(topic)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(message -> {
                    messageHandler.onMessage(message.getPayload());
                }, throwable -> {
                    Log.e("WebSocket","Error on topic handler: " + topic + " " + throwable.getMessage());
                });
    }

    public static void sendMessage(String destination, String message) {
        Disposable d = stompClient.send(destination, message)
                .subscribe(() -> {
                    Log.d("WebSocket","Message sent successfully.");
                }, throwable -> {
                    Log.e("WebSocket","Error sending message: " + throwable.getMessage());
                });
    }

    public static void disconnectStompClient() {
        if (stompClient == null) {
            return;
        }

        if (stompClient.isConnected()) {
            stompClient.disconnect();
        }
    }

    public static EventService eventService = retrofit.create(EventService.class);
    public static ProductService productService = retrofit.create(ProductService.class);
    public static ProfileService profileService = retrofit.create(ProfileService.class);
    public static CategoriesService categoriesService = retrofit.create(CategoriesService.class);
    public static LocationService locationService = retrofit.create(LocationService.class);
    public static EventTypeService eventTypeService = retrofit.create(EventTypeService.class);
    public static LoginService loginService = retrofit.create(LoginService.class);
    public static RegistrationService registrationService = retrofit.create(RegistrationService.class);
    public static ServiceService serviceService = retrofit.create(ServiceService.class);
    public static InvitationService invitationService = retrofit.create(InvitationService.class);
    public static ImageService ImageService = retrofit.create(ImageService.class);
    public static MessageService messageService = retrofit.create(MessageService.class);

}
