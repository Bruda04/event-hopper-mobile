package com.ftn.eventhopper.clients;

import android.graphics.Bitmap;
import android.util.Log;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ImageUtils {
    public static Call<String> uploadImage(Bitmap bitmap) {
        // Convert Bitmap to ByteArray
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();

        // Prepare MultipartBody.Part
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpeg"), byteArray);
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image", "image.jpg", requestBody);

        // Call the upload endpoint
        return ClientUtils.ImageService.uploadImage(imagePart);
    }

    public static Call<Void> deleteImage(String imageName) {
        return ClientUtils.ImageService.deleteImage(imageName);
    }

}

