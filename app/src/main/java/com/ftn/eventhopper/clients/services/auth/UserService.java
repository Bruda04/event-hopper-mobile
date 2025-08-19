package com.ftn.eventhopper.clients.services.auth;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;
import androidx.security.crypto.MasterKeys;

import com.ftn.eventhopper.shared.models.users.PersonType;

import org.json.JSONException;
import org.json.JSONObject;

public class UserService {
    private static final String PREFERENCES_FILE_NAME = "secure_prefs";
    private static final String JWT_KEY = "jwt_token";

    private static SharedPreferences sharedPreferences;

    /**
     * Initializes the secure shared preferences. Call this in your Application class or Activity.
     */
    public static void initialize(Context context) {
        try {
//            String masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);

            MasterKey masterKey = new MasterKey.Builder(context)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build();

            sharedPreferences = EncryptedSharedPreferences.create(
                    masterKey,
                    PREFERENCES_FILE_NAME,
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

    /**
     * Decodes the JWT payload.
     *
     * @param jwtToken The JWT token string.
     * @return A JSON object representing the payload, or null if decoding fails.
     */
    public static JSONObject decodeJwtPayload(String jwtToken) {
        try {
            // Split the JWT token into parts
            String[] parts = jwtToken.split("\\.");
            if (parts.length < 2) {
                return null; // Invalid JWT format
            }

            // Decode the payload (second part) from Base64
            String payload = new String(Base64.decode(parts[1], Base64.URL_SAFE));

            // Parse the payload as a JSON object
            return new JSONObject(payload);
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Decoding failed
        }
    }

    /**
     * Retrieves a specific claim from the JWT payload.
     *
     * @param jwtToken The JWT token string.
     * @param claimKey The key of the claim to retrieve.
     * @return The value of the claim, or null if not found or decoding fails.
     */
    public static String getJwtClaim(String jwtToken, String claimKey) {
        try {
            JSONObject payload = decodeJwtPayload(jwtToken);
            if (payload != null && payload.has(claimKey)) {
                return payload.getString(claimKey);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null; // Claim not found or decoding failed
    }

    public static boolean isTokenValid(){
        String jwtToken = getJwtToken();
        if(jwtToken == null || jwtToken.isEmpty()){
            return false;
        }

        try{
            JSONObject payload = decodeJwtPayload(jwtToken);
            Log.d("PAYLOAD", String.valueOf(payload));

            if(payload != null && payload.has("exp")){
                long expirationTime = payload.getLong("exp");
                long currentTime = System.currentTimeMillis()/1000;
                return expirationTime > currentTime;
            }

        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Retrieves the user's role from the JWT token.
     *
     * @return The role of the user, or null if the role claim is not found or decoding fails.
     */
    public static PersonType getUserRole() {
        String jwtToken = getJwtToken(); // Retrieve the stored JWT token

        if (jwtToken == null || jwtToken.isEmpty()) {
            return null; // No token found
        }

        try {
            // Decode the payload and extract the "role" claim
            JSONObject payload = decodeJwtPayload(jwtToken);
            if (payload != null && payload.has("role")) {
                String role = payload.getString("role");
                if(role.equals("SERVICE_PROVIDER")){
                    return PersonType.SERVICE_PROVIDER;
                }else if(role.equals("ADMIN")){
                    return PersonType.ADMIN;
                }else if(role.equals("EVENT_ORGANIZER")){
                    return PersonType.EVENT_ORGANIZER;
                }else if(role.equals("AUTHENTICATED_USER")){
                    return PersonType.AUTHENTICATED_USER;
                }
                return null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null; // Role not found or decoding failed
    }

}
