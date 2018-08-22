package com.openclassrooms.realestatemanager.models;

import android.support.annotation.Nullable;

public class Immovable {

    private String name;
    private String location;
    private String price;
    @Nullable
    private String PictureUrl;

    public Immovable() {
    }

    public Immovable(String name, String location, String price, @Nullable String pictureUrl) {
        this.name = name;
        this.location = location;
        this.price = price;
        PictureUrl = pictureUrl;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Nullable
    public String getPictureUrl() {
        return PictureUrl;
    }

    public void setPictureUrl(@Nullable String pictureUrl) {
        PictureUrl = pictureUrl;
    }
}
