package com.ftn.eventhopper.fragments.upgrades.viewmodels;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ftn.eventhopper.clients.ClientUtils;
import com.ftn.eventhopper.clients.services.auth.UserService;
import com.ftn.eventhopper.shared.dtos.users.serviceProvider.CompanyDetailsDTO;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import lombok.Getter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpgradeViewModel extends ViewModel {

    @Getter
    private final MutableLiveData<Boolean> upgradeSuccess = new MutableLiveData<>();

    private final ArrayList<String> imageFilePaths = new ArrayList<>();

//    public MutableLiveData<Boolean> getUpgradeSuccess() {
//        return upgradeSuccess;
//    }

    public void upgradeToPup(CompanyDetailsDTO detailsDTO) {
        //Log.i("NEXT", "usao u vm");
        Call<Void> call = ClientUtils.profileService.upgradeToPUP(detailsDTO);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    upgradeSuccess.postValue(true); // Notify success
                    logout();
                } else {
                    upgradeSuccess.postValue(false); // Notify failure
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                upgradeSuccess.postValue(false); // Notify failure
            }
        });
        //Log.i("NEXT", "zavrsio vm");
    }

    public void upgradeToOD() {
        Call<Void> call = ClientUtils.profileService.upgradeToOd();
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    upgradeSuccess.postValue(true); // Notify success
                    logout();
                } else {
                    upgradeSuccess.postValue(false); // Notify failure
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                upgradeSuccess.postValue(false); // Notify failure
            }
        });
    }

    public void logout(){
        UserService.clearJwtToken();
    }

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

    public ArrayList<String> getImageFilePaths() {
        return new ArrayList<>(imageFilePaths);
    }

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

    public void cleanupCache(Context context) {
        File cacheDir = context.getCacheDir();
        for (File file : cacheDir.listFiles()) {
            if (file.getName().startsWith("image_")) {
                file.delete();
            }
        }
        imageFilePaths.clear();
    }


}
