package com.ftn.eventhopper.models;

import android.graphics.drawable.Drawable;

public class Service {
    Drawable image;
    String title;

    String secondary;
    String description;

    public Service(Drawable image, String title, String secondary, String description) {
        this.image = image;
        this.title = title;
        this.secondary = secondary;
        this.description = description;
    }

    public Drawable getImage() {
        return image;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSecondary() {
        return secondary;
    }

    public void setSecondary(String secondary) {
        this.secondary = secondary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
