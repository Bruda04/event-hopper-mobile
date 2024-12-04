package com.ftn.eventhopper.models;

public class CategorySuggestion {
    private String name;
    private String forProduct;

    public CategorySuggestion(String name, String forProduct) {
        this.name = name;
        this.forProduct = forProduct;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getForProduct() {
        return forProduct;
    }

    public void setForProduct(String forProduct) {
        this.forProduct = forProduct;
    }
}
