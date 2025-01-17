package com.ftn.eventhopper.fragments.registration.viewmodels;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.lifecycle.ViewModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class PupImageUploadViewModel extends ViewModel {

    private final ArrayList<String> imageFilePaths = new ArrayList<>();

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
}
