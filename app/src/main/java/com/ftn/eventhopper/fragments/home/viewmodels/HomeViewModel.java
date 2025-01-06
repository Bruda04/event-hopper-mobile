package com.ftn.eventhopper.fragments.home.viewmodels;


import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ftn.eventhopper.clients.ClientUtils;
import com.ftn.eventhopper.shared.dtos.categories.CategoryDTO;
import com.ftn.eventhopper.shared.dtos.eventTypes.EventTypeManagementDTO;
import com.ftn.eventhopper.shared.dtos.eventTypes.SimpleEventTypeDTO;
import com.ftn.eventhopper.shared.dtos.events.SimpleEventDTO;
import com.ftn.eventhopper.shared.dtos.location.LocationDTO;
import com.ftn.eventhopper.shared.dtos.solutions.SimpleProductDTO;
import com.ftn.eventhopper.shared.responses.PagedResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeViewModel extends ViewModel {

    //events
    private final MutableLiveData<ArrayList<SimpleEventDTO>> allEvents = new MutableLiveData<>();
    private final MutableLiveData<PagedResponse<SimpleEventDTO>> allEventsPage = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<SimpleEventDTO>> top5Events = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<LocationDTO>> locations = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<String>> cities = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<SimpleEventTypeDTO>> eventTypes = new MutableLiveData<ArrayList<SimpleEventTypeDTO>>();

    //products

    private final MutableLiveData<ArrayList<SimpleProductDTO>> allProducts = new MutableLiveData<>();
    private final MutableLiveData<PagedResponse<SimpleProductDTO>> allProductsPage = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<SimpleProductDTO>> top5Products = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<CategoryDTO>> categories = new MutableLiveData<ArrayList<CategoryDTO>>();

    //Event filters
    private MutableLiveData<String> searchTextEvents = new MutableLiveData<>("");
    private MutableLiveData<String> selectedCity = new MutableLiveData<>("");
    private MutableLiveData<UUID> selectedEventTypeEvents = new MutableLiveData<>();
    private MutableLiveData<String> selectedDate = new MutableLiveData<>("");
    private MutableLiveData<String> sortFieldEvents = new MutableLiveData<>("");

    //product filters

    private MutableLiveData<Boolean> product=new MutableLiveData<>(true);
    private MutableLiveData<Boolean> service=new MutableLiveData<>(true);
    private MutableLiveData<String> searchTextProducts = new MutableLiveData<>("");
    private MutableLiveData<ArrayList<UUID>> selectedEventTypesProducts = new MutableLiveData<>();
    private MutableLiveData<UUID> selectedCategoryProducts = new MutableLiveData<>();
    private MutableLiveData<Double> minPrice = new MutableLiveData<>();
    private MutableLiveData<Double> maxPrice = new MutableLiveData<>();
    private MutableLiveData<String> sortFieldProducts = new MutableLiveData<>("");
    private MutableLiveData<Boolean> availability = new MutableLiveData<>();
    //event page properties:


    private MutableLiveData<Integer> totalCountEvents = new MutableLiveData<>(0);

    //solution page properties:
    private MutableLiveData<Integer> totalCountProducts = new MutableLiveData<>(0);
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();


    public LiveData<ArrayList<SimpleEventDTO>> getTop5Events() {
        return top5Events;
    }

    public LiveData<ArrayList<SimpleEventDTO>> getEvents() {
        return allEvents;
    }
    public LiveData<PagedResponse<SimpleEventDTO>> getEventsPage() {
        return allEventsPage;
    }

     public LiveData<ArrayList<SimpleProductDTO>> getTop5Products() {
        return top5Products;
    }

    public LiveData<PagedResponse<SimpleProductDTO>> getProductsPage() {
        return allProductsPage;
    }
    public LiveData<ArrayList<String>> getCities() {
        return cities;
    }

    public LiveData<ArrayList<SimpleEventTypeDTO>> getEventTypes() {
        return eventTypes;
    }

    public LiveData<ArrayList<CategoryDTO>> getCategories(){
        return categories;
    }
    public LiveData<String> getSearchTextEvents() {
        return searchTextEvents;
    }
    public LiveData<String> getSelectedCity() {
        return selectedCity;
    }
    public LiveData<UUID> getSelectedEventTypeEvents() {
        return selectedEventTypeEvents;
    }
    public LiveData<String> getSelectedDate() {
        return selectedDate;
    }
    public LiveData<String> getSortFieldEvents() {
        return sortFieldEvents;
    }

    public LiveData<String> getSearchTextProducts() {
        return searchTextProducts;
    }
    public LiveData<ArrayList<UUID>> getSelectedEventTypesProducts() {
        return selectedEventTypesProducts;
    }
    public LiveData<String> getSortFieldProducts() {
        return sortFieldProducts;
    }
    public LiveData<Double> getMinPrice(){return  minPrice;}
    public LiveData<Double> getMaxPrice(){return  maxPrice;}
    public LiveData<UUID> getSelectedCategory(){return selectedCategoryProducts;}
    public LiveData<Boolean> getIsProduct(){return product;}
    public LiveData<Boolean> getIsService(){return service;}
    public LiveData<Boolean> getAvailability(){return availability;}

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void setSearchTextEvents(String text) {
        searchTextEvents.setValue(text);
    }

    public void setSearchTextProducts(String text) {
        searchTextProducts.setValue(text);
    }
    public void setSortFieldEvents(String sortOption) {
        sortFieldEvents.setValue(sortOption);
    }

    public void setSelectedCity(String city){
        selectedCity.setValue(city);
    }

    public void setSelectedEventTypeEvents(UUID id){
        selectedEventTypeEvents.setValue(id);
    }

    public void setSelectedDate(String date){
        selectedDate.setValue(date);
    }

    public void setSortFieldProducts(String sortOption) {
        sortFieldProducts.setValue(sortOption);
    }

    public void setAvailability(Boolean availability) {
        this.availability.setValue(availability);
    }

    public void setMaxPrice(Double maxPrice) {
        this.maxPrice.setValue(maxPrice);
    }

    public void setMinPrice(Double minPrice) {
        this.minPrice.setValue(minPrice);
    }

    public void setIsProduct(Boolean isProductSelected) {
        this.product.setValue(isProductSelected);
    }
    public void setIsService(Boolean isServiceSelected) {
        this.service.setValue(isServiceSelected);
    }

    public void setSelectedCategory(UUID id){
        this.selectedCategoryProducts.setValue(id);
    }
    public void fetchAllEventsPage(
            String city,
            UUID eventTypeId,
            String time,
            String searchContent,
            String sortField,
            int page,
            int size
    ){

        Map<String,String> queryParams = new HashMap<>();

        queryParams.put("page", String.valueOf(page));
        queryParams.put("size", String.valueOf(size));
        if (city != null && !city.isEmpty()) {
            queryParams.put("city", city);
        }
        if (eventTypeId != null) {
            queryParams.put("eventTypeId", eventTypeId.toString());
        }
        if (time != null && !time.isEmpty()) {
            queryParams.put("time", time);
        }
        if (searchContent != null && !searchContent.isEmpty()) {
            queryParams.put("searchContent", searchContent);
        }
        if (sortField != null && !sortField.isEmpty()) {
            queryParams.put("sortField", sortField);
        }

        Call<PagedResponse<SimpleEventDTO>> call = ClientUtils.eventService.getEventsPage(
            queryParams
        );
        call.enqueue(new Callback<PagedResponse<SimpleEventDTO>>() {
            @Override
            public void onResponse(Call<PagedResponse<SimpleEventDTO>> call, Response<PagedResponse<SimpleEventDTO>> response) {
                if(response.isSuccessful()){
                    PagedResponse<SimpleEventDTO> pagedResponse = response.body();
                    allEventsPage.postValue(pagedResponse);
                    totalCountEvents.postValue((int) pagedResponse.getTotalElements());
                    errorMessage.postValue(null);
                }else{
                    errorMessage.postValue("Failed to fetch events. Code: "+ response.code());
                }
            }

            @Override
            public void onFailure(Call<PagedResponse<SimpleEventDTO>> call, Throwable t) {
                errorMessage.postValue(t.getMessage());
            }
        });
    }

    public void fetchAllSolutionsPage(
            Boolean isProduct,
            Boolean isService,
            UUID categoryId,
            ArrayList<UUID> eventTypeIds,
            Double minPrice,
            Double maxPrice,
            Boolean isAvailable,
            String searchContent,
            String sortField,
            int page,
            int size
    ){

        Map<String,String> queryParams = new HashMap<>();

        queryParams.put("page", String.valueOf(page));
        queryParams.put("size", String.valueOf(size));
        if (isProduct != null ) {
            queryParams.put("isProduct", isProduct.toString());
        }
        if (isService != null ) {
            queryParams.put("isService", isService.toString());
        }
        if (isAvailable != null ) {
            queryParams.put("isAvailable", isAvailable.toString());
        }
        if (categoryId != null) {
            queryParams.put("categoryId", categoryId.toString());
        }
        if (eventTypeIds != null ) {
            if(!eventTypeIds.isEmpty()){
                queryParams.put("eventTypeId", String.valueOf(eventTypeIds));
            }
        }
        if (minPrice != null) {
            queryParams.put("minPrice", minPrice.toString());
        }
        if (maxPrice != null) {
            queryParams.put("maxPrice", maxPrice.toString());
        }
        if (searchContent != null && !searchContent.isEmpty()) {
            queryParams.put("searchContent", searchContent);
        }
        if (sortField != null && !sortField.isEmpty()) {
            queryParams.put("sortField", sortField);
        }

        Call<PagedResponse<SimpleProductDTO>> call = ClientUtils.productService.getSolutionsPage(
            queryParams
        );
        call.enqueue(new Callback<PagedResponse<SimpleProductDTO>>() {
            @Override
            public void onResponse(Call<PagedResponse<SimpleProductDTO>> call, Response<PagedResponse<SimpleProductDTO>> response) {
                if(response.isSuccessful()){
                    PagedResponse<SimpleProductDTO> pagedResponse = response.body();
                    allProductsPage.postValue(pagedResponse);
                    totalCountProducts.postValue((int) pagedResponse.getTotalElements());
                    errorMessage.postValue(null);
                }else{
                    errorMessage.postValue("Failed to fetch solutions. Code: "+ response.code());
                }
            }

            @Override
            public void onFailure(Call<PagedResponse<SimpleProductDTO>> call, Throwable t) {
                errorMessage.postValue(t.getMessage());
            }
        });
    }
    public void fetchTopEvents(UUID id) {
        Call<ArrayList<SimpleEventDTO>> call = ClientUtils.eventService.getTop5Events(id);
        call.enqueue(new Callback<ArrayList<SimpleEventDTO>>() {

            @Override
            public void onResponse(Call<ArrayList<SimpleEventDTO>> call, Response<ArrayList<SimpleEventDTO>> response) {
                if(response.isSuccessful()){
                    top5Events.postValue(response.body());
                    errorMessage.postValue(null);
                }else{
                    errorMessage.postValue("Failed to fetch top events. Code: "+ response.code());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<SimpleEventDTO>> call, Throwable t) {
                errorMessage.postValue(t.getMessage());
            }
        });
    }
    public void fetchTopSolutions(UUID id) {
        Call<ArrayList<SimpleProductDTO>> call = ClientUtils.productService.getTop5Solutions(id);
        call.enqueue(new Callback<ArrayList<SimpleProductDTO>>() {

            @Override
            public void onResponse(Call<ArrayList<SimpleProductDTO>> call, Response<ArrayList<SimpleProductDTO>> response) {
                if(response.isSuccessful()){
                    top5Products.postValue(response.body());
                    errorMessage.postValue(null);
                }else{
                    errorMessage.postValue("Failed to fetch top solutions. Code: "+ response.code());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<SimpleProductDTO>> call, Throwable t) {
                errorMessage.postValue(t.getMessage());
            }
        });
    }

    public void fetchCities(){
        Call<ArrayList<String>> call = ClientUtils.locationService.getCities();
        call.enqueue(new Callback<ArrayList<String>>() {
            @Override
            public void onResponse(Call<ArrayList<String>> call, Response<ArrayList<String>> response) {
                if(response.isSuccessful()){
                    cities.postValue(response.body());
                    errorMessage.postValue(null);
                }else{
                    errorMessage.postValue("Failed to fetch cities. Code: "+ response.code());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<String>> call, Throwable t) {
                errorMessage.postValue(t.getMessage());
            }
        });
    }
    public void fetchCategories(){
        Call<ArrayList<CategoryDTO>> call = ClientUtils.categoriesService.getApproved();
        call.enqueue(new Callback<ArrayList<CategoryDTO>>() {
            @Override
            public void onResponse(Call<ArrayList<CategoryDTO>> call, Response<ArrayList<CategoryDTO>> response) {
                if(response.isSuccessful()){
                    categories.postValue(response.body());
                    errorMessage.postValue(null);
                }else{
                    errorMessage.postValue("Failed to fetch categories. Code: "+ response.code());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<CategoryDTO>> call, Throwable t) {
                errorMessage.postValue(t.getMessage());
            }
        });
    }

    public void fetchEventTypes(){
        Call<EventTypeManagementDTO> call = ClientUtils.eventTypeService.getEventTypes();
        call.enqueue(new Callback<EventTypeManagementDTO>() {
            @Override
            public void onResponse(Call<EventTypeManagementDTO> call, Response<EventTypeManagementDTO> response) {
                if(response.isSuccessful()){
                    EventTypeManagementDTO res = response.body();
                    eventTypes.postValue((ArrayList<SimpleEventTypeDTO>) res.getEventTypes());
                    errorMessage.postValue(null);
                }else{
                    errorMessage.postValue("Failed to fetch event types. Code: "+ response.code());
                }
            }

            @Override
            public void onFailure(Call<EventTypeManagementDTO> call, Throwable t) {
                errorMessage.postValue(t.getMessage());
            }
        });
    }

}
