package com.youthlive.youthlive;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

class AppConfig {

    private static String BASE_URL = "http://nationproducts.in/youthlive/api/";

    static Retrofit getRetrofit() {

        return new Retrofit.Builder()
                .baseUrl(AppConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
