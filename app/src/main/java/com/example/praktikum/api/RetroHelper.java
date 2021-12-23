package com.example.praktikum.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetroHelper {
    private static final String baseURL = "http://10.0.2.2:8000/api/"; //ganti url setiap membuka android studio, dari ipconfigs
    private static Retrofit retro;

    public static Retrofit connectRetrofit(){
        if(retro == null){
            retro = new Retrofit.Builder()
                    .baseUrl(baseURL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retro;
    }
}
