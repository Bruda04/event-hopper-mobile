package com.ftn.eventhopper.shared.models;

public class Category {
    private String name;
    private String description;
    private boolean isDeletable;

    public Category(String name, String description, boolean isDeletable) {
        this.name = name;
        this.description = description;
        this.isDeletable = isDeletable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDeletable() {
        return isDeletable;
    }

    public void setDeletable(boolean deletable) {
        isDeletable = deletable;
    }
}
