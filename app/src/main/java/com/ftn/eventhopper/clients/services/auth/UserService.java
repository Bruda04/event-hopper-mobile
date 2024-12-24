package com.ftn.eventhopper.clients.services.auth;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

public class UserService {
    private static final String PREFERENCES_FILE_NAME = "secure_prefs";
    private static final String JWT_KEY = "jwt_token";

    private static SharedPreferences sharedPreferences;

    /**
     * Initializes the secure shared preferences. Call this in your Application class or Activity.
     */
    public static void initialize(Context context) {
        try {
            String masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);

            sharedPreferences = EncryptedSharedPreferences.create(
                    PREFERENCES_FILE_NAME,
                    masterKeyAlias,
                    context,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize EncryptedSharedPreferences");
        }
    }

    /**
     * Saves the JWT token securely.
     */
    public static void setJwtToken(String jwtToken) {
        if (sharedPreferences != null) {
            sharedPreferences.edit().putString(JWT_KEY, jwtToken).apply();
        } else {
            throw new IllegalStateException("EncryptedSharedPreferences not initialized. Call initialize() first.");
        }
    }

    /**
     * Retrieves the JWT token securely.
     */
    public static String getJwtToken() {
        if (sharedPreferences != null) {
            return sharedPreferences.getString(JWT_KEY, "");
        } else {
            throw new IllegalStateException("EncryptedSharedPreferences not initialized. Call initialize() first.");
        }
    }

    /**
     * Clears the JWT token (e.g., during logout).
     */
    public static void clearJwtToken() {
        if (sharedPreferences != null) {
            sharedPreferences.edit().remove(JWT_KEY).apply();
        } else {
            throw new IllegalStateException("EncryptedSharedPreferences not initialized. Call initialize() first.");
        }
    }
}
