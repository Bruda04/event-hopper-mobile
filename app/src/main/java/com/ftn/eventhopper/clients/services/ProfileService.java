package com.ftn.eventhopper.clients.services;

import java.util.UUID;

import retrofit2.Call;

public interface ProfileService {

    // TODO: Implement this with JWT
    Call<Void> addSolutionToFavorites(UUID id);

    // TODO: Implement this with JWT
    Call<Void> removeSolutionFromFavorites(UUID id);
}
