package com.example.testlogin;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    /********
     * URLS
     *******/
 //  private static final String ROOT_URL = "http://192.168.1.15/myProject/";
 // private static final String ROOT_URL = "http://192.168.43.113/myProject/";
   private static final String ROOT_URL = "https://pgnhostels.com/myproject/";

 //   private static final String ROOT_URL = "http://192.168.44.126/shareit/";
    //192.168.44.126

    /**
     * Get Retrofit Instance
     */
    private static Retrofit getRetrofitInstance() {
        return new Retrofit.Builder()
                .baseUrl(ROOT_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    /**
     * Get API Service
     * @return API Service
     */
    public static ApiInterface getApiService() {
        return getRetrofitInstance().create(ApiInterface.class);
    }
}
