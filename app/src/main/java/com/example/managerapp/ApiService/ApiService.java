package com.example.managerapp.ApiService;

import com.example.managerapp.Model.ApiResponseResult;
import com.example.managerapp.Model.DestinationModel;
import com.example.managerapp.Model.DestinationOptionMapModel;
import com.example.managerapp.Model.LatLngModel;
import com.example.managerapp.Model.ListUserModel;
import com.example.managerapp.Model.TripModel;
import com.example.managerapp.OAuthInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface ApiService {
//        String BASE_URL = "http://192.168.100.6:8080/api/";

    String BASE_URL = "http://192.168.1.23:8080/api/";

    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
            .create();


    OkHttpClient client = new OkHttpClient.Builder()
            .addInterceptor(new OAuthInterceptor())
            .build();

    ApiService apiService = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
            .create(ApiService.class);


    @GET("check-auth/check-login")
    Call<ApiResponseResult<Boolean>> checkAuth();

    @GET("trip/index")
    Call<ApiResponseResult<List<TripModel>>> getListTrip(@Query("name") String name);

    @GET("trip/show/{id}")
    Call<ApiResponseResult<TripModel>> getShowTrip(@Path("id") String id);

    @POST("trip/create")
    Call<ApiResponseResult<Object>> createTripApi(@Body TripModel tripModel);

    @POST("trip/update/{id}")
    Call<ApiResponseResult<TripModel>> updateTripApi(@Body TripModel tripModel, @Path("id") String id);

    @DELETE("trip/delete/{id}")
    Call<ApiResponseResult<Object>> deleteTripApi(@Path("id") String id);

    @DELETE("trip/delete-async")
    Call<ApiResponseResult<Object>> clearDataTripApi();

//    @POST("auth/login")
//    Call<AnyRes> postLogin(@Body LoginModel loginModel);

    @GET("destination/index")
    Call<ApiResponseResult<List<DestinationModel>>> getListDestination(@Query("tripId") String tripId, @Query("searchText") String searchText);

    @POST("destination/create")
    Call<ApiResponseResult<Object>> createDestinationApi(@Body DestinationModel destinationModel);

    @POST("destination/update/{id}")
    Call<ApiResponseResult<DestinationModel>> UpdateDestinationApi(
            @Body DestinationModel destinationModel,
            @Path("id") String id);

    @DELETE("destination/delete/{id}")
    Call<ApiResponseResult<Object>> DeleteDestinationApi(@Path("id") String id);

    @GET("destination/get-lat-log/{destinationId}")
    Call<ApiResponseResult<List<LatLngModel>>> getLatLogByDestinationId(@Path("destinationId") String destinationId );

    @GET("destination/dashboard-map")
    Call<ApiResponseResult<List<DestinationOptionMapModel>>> DestinationOptionMapApi(@Query("tripId") String tripId);

    @GET("user/index")
    Call<ApiResponseResult<List<ListUserModel>>> IndexUser(@Query("searchText") String searchText );

    @POST("user/create")
    Call<ApiResponseResult<Object>> CreateUser(@Body ListUserModel listUserModel );

    @DELETE("user/delete/{id}")
    Call<ApiResponseResult<Object>> DeleteUser(@Path("id") String userId );

    @POST("user/reset-password/{id}")
    Call<ApiResponseResult<Object>> ResetPassword(@Path("id") String id);
}
