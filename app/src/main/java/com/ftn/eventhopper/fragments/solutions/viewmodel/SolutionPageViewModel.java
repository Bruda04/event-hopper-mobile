package com.ftn.eventhopper.fragments.solutions.viewmodel;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.navigation.NavController;

import com.ftn.eventhopper.R;
import com.ftn.eventhopper.clients.ClientUtils;
import com.ftn.eventhopper.shared.dtos.comments.CreateCommentDTO;
import com.ftn.eventhopper.shared.dtos.ratings.CreateProductRatingDTO;
import com.ftn.eventhopper.shared.dtos.reservations.CreateReservationProductDTO;
import com.ftn.eventhopper.shared.dtos.reservations.CreateReservationServiceDTO;
import com.ftn.eventhopper.shared.dtos.reservations.CreatedReservationProductDTO;
import com.ftn.eventhopper.shared.dtos.solutions.SolutionDetailsDTO;

import org.json.JSONObject;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SolutionPageViewModel extends ViewModel {
    private final MutableLiveData<SolutionDetailsDTO> solutionDetailsLiveData = new MutableLiveData<>();
    private final MutableLiveData<Collection<LocalDateTime>> freeTerms = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public LiveData<SolutionDetailsDTO> getSolutionDetails() {
        return solutionDetailsLiveData;
    }

    public LiveData<Collection<LocalDateTime>> getFreeTerms() {return freeTerms; }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void fetchSolutionDetailsById(UUID solutionId) {
        Call<SolutionDetailsDTO> call = ClientUtils.productService.getSolutionDetailsById(solutionId);
        call.enqueue(new Callback<SolutionDetailsDTO>() {
            @Override
            public void onResponse(Call<SolutionDetailsDTO> call, Response<SolutionDetailsDTO> response) {
                if (response.isSuccessful()) {
                    solutionDetailsLiveData.postValue(response.body());

                } else {
                    errorMessage.postValue("Failed to fetch products details.");
                }
            }

            @Override
            public void onFailure(Call<SolutionDetailsDTO> call, Throwable t) {
                errorMessage.postValue(t.getMessage());
            }
        });
    }

    public void toggleFavorite(Context context) {
        SolutionDetailsDTO solutionDetails = getSolutionDetails().getValue();
        if (solutionDetails != null) {
            if (!solutionDetails.isFavorite())  {
                Call<Void> call = ClientUtils.profileService.addSolutionToFavorites(solutionDetails.getId());
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            solutionDetails.setFavorite(true);
                            Toast.makeText(context, "Added to favorites!", Toast.LENGTH_SHORT).show();
                            solutionDetailsLiveData.postValue(solutionDetails);
                        } else {
                            errorMessage.postValue("Failed to add solution to favorites. Code: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        errorMessage.postValue(t.getMessage());
                    }
                });
            } else {
                Call<Void> call = ClientUtils.profileService.removeSolutionFromFavorites(solutionDetails.getId());
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            solutionDetails.setFavorite(false);
                            Toast.makeText(context, "Removed from favorites.", Toast.LENGTH_SHORT).show();
                            solutionDetailsLiveData.postValue(solutionDetails);
                        } else {
                            errorMessage.postValue("Failed to remove solution from favorites. Code: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        errorMessage.postValue(t.getMessage());
                    }
                });
            }
        }
    }

    public void goToProviderPage(NavController navController) {
        SolutionDetailsDTO solutionDetails = getSolutionDetails().getValue();
        if (solutionDetails != null && navController != null && solutionDetails.getProvider() != null) {
            Bundle bundle = new Bundle();
            bundle.putString("id", solutionDetails.getProvider().getId().toString());
            navController.navigate(R.id.action_to_provider_page, bundle);
        }

    }

    public void makeReview(Integer rating, String comment) {
        if (rating != null) {
            rateSolution(rating);
        }
        if (comment != null) {
            leaveComment(comment);
        }
    }

    private void leaveComment(String comment) {
        CreateCommentDTO createCommentDTO = new CreateCommentDTO();
        createCommentDTO.setProductId(getSolutionDetails().getValue().getId());
        createCommentDTO.setContent(comment);

        Call<ResponseBody> call = ClientUtils.productService.commentProduct(createCommentDTO);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    fetchSolutionDetailsById(getSolutionDetails().getValue().getId());
                } else {
                    try {
                        String errorBody = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(errorBody);
                        String errorMessageString = jsonObject.getString("message");
                        errorMessage.postValue("Error commenting solution: " + errorMessageString);

                    } catch (Exception e) {
                        errorMessage.postValue("An unexpected error occurred.");
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                errorMessage.postValue(t.getMessage());
            }
        });
    }

    private void rateSolution(Integer rating) {
        CreateProductRatingDTO createProductRatingDTO = new CreateProductRatingDTO();
        createProductRatingDTO.setProductId(getSolutionDetails().getValue().getId());
        createProductRatingDTO.setValue(rating);

        Call<ResponseBody> call = ClientUtils.productService.rateProduct(createProductRatingDTO);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    fetchSolutionDetailsById(getSolutionDetails().getValue().getId());
                } else {
                    try {
                        String errorBody = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(errorBody);
                        String errorMessageString = jsonObject.getString("message");
                        errorMessage.postValue("Error rating solution: " + errorMessageString);

                    } catch (Exception e) {
                        errorMessage.postValue("An unexpected error occurred.");
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                errorMessage.postValue(t.getMessage());
            }
        });
    }

    public void buyProduct(UUID eventId) {
        CreateReservationProductDTO reservationRequest = new CreateReservationProductDTO();
        SolutionDetailsDTO solutionDetails = getSolutionDetails().getValue();
        if (solutionDetails == null) {
            errorMessage.postValue("Failed to buy product.");
            return;
        }
        reservationRequest.setProductId(solutionDetails.getId());
        reservationRequest.setEventId(eventId);

        Call<ResponseBody> call = ClientUtils.reservationService.buyProduct(reservationRequest);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    fetchSolutionDetailsById(getSolutionDetails().getValue().getId());
                } else {
                    try {
                        String errorBody = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(errorBody);
                        String errorMessageString = jsonObject.getString("message");
                        errorMessage.postValue("Error buying product: " + errorMessageString);

                    } catch (Exception e) {
                        errorMessage.postValue("An unexpected error occurred.");
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                errorMessage.postValue(t.getMessage());
            }
        });
    }

    public void bookService(UUID eventId, LocalDateTime startTime, LocalDateTime endTime){
        CreateReservationServiceDTO reservationRequest = new CreateReservationServiceDTO();
        SolutionDetailsDTO solutionDetailsDTO = getSolutionDetails().getValue();
        if (solutionDetailsDTO == null){
            errorMessage.postValue("Failed to book a service.");
            return;
        }
        reservationRequest.setProductId(solutionDetailsDTO.getId());
        reservationRequest.setEventId(eventId);
        reservationRequest.setFrom(startTime);
        reservationRequest.setTo(endTime);

        Call<ResponseBody> call = ClientUtils.reservationService.bookService(reservationRequest);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    fetchSolutionDetailsById(getSolutionDetails().getValue().getId());
                }else{
                    try {
                        String errorBody = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(errorBody);
                        String errorMessageString = jsonObject.getString("message");
                        errorMessage.postValue("Error booking a service: " + errorMessageString);

                    } catch (Exception e) {
                        errorMessage.postValue("An unexpected error occurred.");
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                errorMessage.postValue(t.getMessage());
            }
        });

    }

    public void fetchFreeTerms(String date){

        Log.i("vm",date);
        SolutionDetailsDTO solutionDetailsDTO = getSolutionDetails().getValue();
        if (solutionDetailsDTO == null){
            errorMessage.postValue("Failed to book a service.");
            return;
        }

        Call<List<LocalDateTime>> call = ClientUtils.reservationService.getAvailableTerms(solutionDetailsDTO.getId(), date);
        call.enqueue(new Callback<List<LocalDateTime>>() {
            @Override
            public void onResponse(Call<List<LocalDateTime>> call, Response<List<LocalDateTime>> response) {
                if(response.isSuccessful()){
                    freeTerms.postValue(response.body());
                    errorMessage.postValue(null);
                }else{
                    errorMessage.postValue("Failed to fetch free terms. Code: "+ response.code());
                }
            }

            @Override
            public void onFailure(Call<List<LocalDateTime>> call, Throwable t) {
                errorMessage.postValue(t.getMessage());
            }
        });

    }
}
