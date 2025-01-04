package com.ftn.eventhopper.shared.dtos.login;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    private boolean success;
    private String message;
    private String token;
    private Long expiresIn;


    public boolean isSuccessful(){
        try{
            return success;
        }catch(Error error){
            return false;
        }
    }
}
