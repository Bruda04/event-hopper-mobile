package com.ftn.eventhopper.fragments.registration.viewmodels;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.ftn.eventhopper.clients.ClientUtils;
import com.ftn.eventhopper.clients.ImageUtils;
import com.ftn.eventhopper.shared.dtos.users.account.CreateServiceProviderAccountDTO;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PupImageUploadViewModel extends ViewModel {

    private final ArrayList<String> imageFilePaths = new ArrayList<>();

    private ArrayList<String> existingImagesToKeep = new ArrayList<>();

    /**
     * Saves the provided bitmaps to the cache directory and stores their file paths.
     *
     * @param context Application context for accessing the cache directory.
     * @param bitmaps List of Bitmaps to save.
     */
    public void saveBitmapsToCache(Context context, ArrayList<Bitmap> bitmaps) {
        File cacheDir = context.getCacheDir();
        for (Bitmap bitmap : bitmaps) {
            try {
                File tempFile = File.createTempFile("image_", ".png", cacheDir);
                FileOutputStream fos = new FileOutputStream(tempFile);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.close();
                imageFilePaths.add(tempFile.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void addExistingImageToFilePaths(String imageUrl) {
        existingImagesToKeep.add(imageUrl);
    }

    //returns filepath
    public String saveBitmapToCache(Context context, Bitmap bitmap) {
        try {
            // Create a unique file name for the image
            String fileName = "image_" + System.currentTimeMillis() + ".png";

            // Get the cache directory
            File cacheDir = context.getCacheDir();
            File imageFile = new File(cacheDir, fileName);

            // Write the bitmap to the file
            FileOutputStream fos = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();

            // Save the file path for later use
            imageFilePaths.add(imageFile.getAbsolutePath());
            return imageFile.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String saveSingleBitmapToCache(Context context, Bitmap bitmap) {
        try {
            // Create a unique file name for the image
            String fileName = "image_" + System.currentTimeMillis() + ".png";

            // Get the cache directory
            File cacheDir = context.getCacheDir();
            File imageFile = new File(cacheDir, fileName);

            // Write the bitmap to the file
            FileOutputStream fos = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();

            // Save the file path for later use
            return imageFile.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }


    /**
     * Retrieves the file paths of cached images.
     *
     * @return List of file paths to the cached images.
     */
    public ArrayList<String> getImageFilePaths() {
        return new ArrayList<>(imageFilePaths);
    }

    /**
     * Loads bitmaps from the cached file paths.
     *
     * @return List of loaded Bitmaps.
     */
    public ArrayList<Bitmap> loadBitmapsFromCache() {
        ArrayList<Bitmap> bitmaps = new ArrayList<>();
        for (String path : imageFilePaths) {
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            if (bitmap != null) {
                bitmaps.add(bitmap);
            }
        }
        return bitmaps;
    }

    /**
     * Cleans up temporary image files in the cache directory.
     *
     * @param context Application context for accessing the cache directory.
     */
    public void cleanupCache(Context context) {
        File cacheDir = context.getCacheDir();
        for (File file : cacheDir.listFiles()) {
            if (file.getName().startsWith("image_")) {
                file.delete();
            }
        }
        imageFilePaths.clear();
    }




    public void submitCompanyPhotos(ArrayList<String> imageFilePaths, Runnable onSuccess) {
        AtomicInteger remainingUploads = new AtomicInteger(imageFilePaths.size());
        AtomicBoolean hasUploadFailed = new AtomicBoolean(false);

        // Shared pictures list
        List<String> pictures = Collections.synchronizedList(new ArrayList<>());

        if (imageFilePaths.isEmpty()) {
            replaceCompanyPictures(new ArrayList<>(), onSuccess);
            return;
        }

        for (String filePath : imageFilePaths) {
            Bitmap bitmap = BitmapFactory.decodeFile(filePath);
            if (bitmap == null) {
                Log.e("Image Load Error", "Failed to load bitmap from file: " + filePath);
                if (remainingUploads.decrementAndGet() == 0 && !hasUploadFailed.get()) {
                    replaceCompanyPictures(new ArrayList<>(pictures), onSuccess); // Send what we have
                }
                continue;
            }

            Call<String> call = ImageUtils.uploadImage(bitmap);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        pictures.add(response.body());
                    } else {
                        Log.e("Image Upload Error", "Failed to upload image: " + filePath);
                        hasUploadFailed.set(true);
                    }

                    if (remainingUploads.decrementAndGet() == 0 && !hasUploadFailed.get()) {
                        replaceCompanyPictures(new ArrayList<>(pictures), onSuccess);
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.e("Image Upload Failure", "Error uploading image: " + filePath, t);
                    hasUploadFailed.set(true);

                    if (remainingUploads.decrementAndGet() == 0 && !hasUploadFailed.get()) {
                        replaceCompanyPictures(new ArrayList<>(pictures), onSuccess);
                    }
                }
            });
        }
    }


    private void replaceCompanyPictures(List<String> pictures, Runnable onSuccess){
        for(String picture : existingImagesToKeep){
            pictures.add(picture);
        }
        this.existingImagesToKeep.clear();
        Log.d("Image: Sending to server", pictures.toString());
        Call<Void> updateDataCall = ClientUtils.profileService.changeCompanyPictures(pictures);
        updateDataCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> callResponse) {
                if (callResponse.isSuccessful()) {
                    Log.d("Company replace images", "Successful");
                    onSuccess.run();
                } else {
                    Log.d("Company replace images", "Failed");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("Company replace images", "Server error occurred.");
            }
        });

    }



}
