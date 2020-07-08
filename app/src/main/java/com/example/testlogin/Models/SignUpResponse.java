package com.example.testlogin.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class SignUpResponse  {

    @SerializedName("result")
    @Expose
    private List<User> result = null;

    public List<User> getResult() {
        return result;
    }

    @SerializedName("response")
    private String response;

    public String getResponse() {
        return response;
    }

    public void setResult(List<User> result) {
        this.result = result;
    }

}