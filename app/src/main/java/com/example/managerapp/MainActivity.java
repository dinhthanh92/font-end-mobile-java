package com.example.managerapp;


import androidx.appcompat.app.AppCompatActivity;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.rxjava3.RxPreferenceDataStoreBuilder;
import androidx.datastore.rxjava3.RxDataStore;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;

import com.example.managerapp.ApiService.ApiCheckLogin;
import com.example.managerapp.ApiService.ApiService;
import com.example.managerapp.DataStore.DataStoreManager;
import com.example.managerapp.Model.ApiResponseResult;
import com.example.managerapp.Model.DataStoreModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {

    RxDataStore<Preferences> rxDataStore;
    public static DataStoreManager dataStoreManager;
    Menu menu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rxDataStore = new RxPreferenceDataStoreBuilder(this, "dataStore").build();

        dataStoreManager = new DataStoreManager(this, rxDataStore);

        checkLogin();
    }



    public void checkLogin(){

        ApiService.apiService.checkAuth().enqueue(new Callback<ApiResponseResult<Boolean>>() {
            @Override
            public void onResponse(Call<ApiResponseResult<Boolean>> call, Response<ApiResponseResult<Boolean>> response) {
                if(response.code() == 200 && !GetStoreData().getUser().getIsFirst()){
                    final Intent intents = new Intent(MainActivity.this,
                            TripActivity.class);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(intents);
                            finish();
                        }
                    }, 1000);
                } else {
                    final Intent intents = new Intent(MainActivity.this,
                            LoginActivity.class);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(intents);
                            finish();
                        }
                    }, 1000);
                }
            }

            @Override
            public void onFailure(Call<ApiResponseResult<Boolean>> call, Throwable t) {
                final Intent intents = new Intent(MainActivity.this,
                        LoginActivity.class);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(intents);
                        finish();
                    }
                }, 1000);
            }
        });
    }

    public static DataStoreModel GetStoreData(){
        DataStoreModel dataStoreModel =
                dataStoreManager.getDataStore().blockingFirst();
        return dataStoreModel;
    }

    public static void SetStoreData(DataStoreModel data){
        MainActivity.dataStoreManager.save(data);
    }

    public static String GetStoreDataTrip(){
        String data =
                dataStoreManager.getDataStoreTrip().blockingFirst();
        return data;
    }

    public static void SetStoreDataTrip(String tripId){
        MainActivity.dataStoreManager.saveTrip(tripId);
    }
}
