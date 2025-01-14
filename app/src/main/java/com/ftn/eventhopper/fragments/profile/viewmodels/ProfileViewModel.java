package com.ftn.eventhopper.fragments.profile.viewmodels;

import androidx.lifecycle.ViewModel;

import com.ftn.eventhopper.clients.services.auth.UserService;

import lombok.Getter;

@Getter
public class ProfileViewModel extends ViewModel {
    public void logout(){
        UserService.clearJwtToken();
    }

}
