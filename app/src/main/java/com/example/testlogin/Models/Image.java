package com.example.testlogin.Models;

import com.google.gson.annotations.SerializedName;

public class Image {
    @SerializedName("name")
    private String name;

    @SerializedName("images")
    private String images;

    public Image(String name, String images) {
        this.name = name;
        this.images = images;
    }

    public String getImages() {
        return images;
    }

    public String getName() {
        return name;
    }


}
