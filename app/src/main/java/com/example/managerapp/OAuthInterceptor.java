package com.example.managerapp;

import androidx.annotation.NonNull;

import com.example.managerapp.ApiService.ApiService;
import com.example.managerapp.Model.DataStoreModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OAuthInterceptor implements Interceptor {

    public OAuthInterceptor(){}
    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
        request = request.newBuilder()
                .header("bearer", MainActivity.GetStoreData().token)
                .build();
        return chain.proceed(request);
    }
}
