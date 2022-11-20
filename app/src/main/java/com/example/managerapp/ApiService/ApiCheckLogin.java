package com.example.managerapp.ApiService;

import com.example.managerapp.Model.ApiResponseResult;
import com.example.managerapp.OAuthInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public interface ApiCheckLogin {
    String BASE_URL = "http://192.168.1.23:8080/api/";

    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
            .create();

    HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
    OkHttpClient client = new OkHttpClient.Builder()
            .addInterceptor(new OAuthInterceptor())
            .build();

    ApiCheckLogin apiService = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
            .create(ApiCheckLogin.class);

    /*@GET("check-auth/check-login")
    Call<ApiResponseResult<Boolean>> checkAuth();*/
}
