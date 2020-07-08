package com.example.testlogin.Models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class proImages {

    @SerializedName("result")
    private ArrayList<Image> result;

    public proImages(){

    }

    public ArrayList<Image> getResult(){
        return result;
    }

}
