package com.example.managerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.managerapp.ApiService.ApiService;
import com.example.managerapp.Model.ApiResponseResult;
import com.example.managerapp.Model.DestinationOptionMapModel;
import com.example.managerapp.Model.LatLngModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapViewActivity extends SetMenuOption implements OnMapReadyCallback {
    GoogleMap mMap;
    Spinner spinner;
    List<DestinationOptionMapModel> destinationOptionMapModel;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_view);

        Button btnBack = findViewById(R.id.back_screen_view_map);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intents = new Intent(MapViewActivity.this,
                        DestinationActivity.class);
                intents.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intents);
            }
        });

        spinner = findViewById(R.id.spinner_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.google_map_view);

        mapFragment.getMapAsync( MapViewActivity.this);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LatLng latLng = new LatLng(destinationOptionMapModel.get(position).getLatitude(), destinationOptionMapModel.get(0).getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap=googleMap;
        LoadDataOption(mMap);
    }

    public void loadLatLog(GoogleMap mMaps){
        ApiService.apiService.getLatLogByDestinationId(MainActivity.GetStoreDataTrip()).enqueue(new Callback<ApiResponseResult<List<LatLngModel>>>() {
            @Override
            public void onResponse(Call<ApiResponseResult<List<LatLngModel>>> call, Response<ApiResponseResult<List<LatLngModel>>> response) {

                if(response.code() == 200){
                    List<LatLngModel> result = response.body().results;
                    LatLng latLng;
                    if(result.size() > 0)
                    for (int i = 0; i < result.size(); i++){

                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponseResult<List<LatLngModel>>> call, Throwable t) {

            }
        });
    }

    public void LoadDataOption(GoogleMap mMaps){

        ApiService.apiService.DestinationOptionMapApi(MainActivity.GetStoreDataTrip()).enqueue(new Callback<ApiResponseResult<List<DestinationOptionMapModel>>>() {
            @Override
            public void onResponse(Call<ApiResponseResult<List<DestinationOptionMapModel>>> call, Response<ApiResponseResult<List<DestinationOptionMapModel>>> response) {

                if(response.code() == 200){
                    destinationOptionMapModel = response.body().results;
                    ArrayList<String> latLogs = new ArrayList<String>();
                    if(destinationOptionMapModel.size() > 0){
                        LatLng latLng = new LatLng(0, 0);
                        for (int i = 0; i < destinationOptionMapModel.size(); i++){
                            latLng = new LatLng(destinationOptionMapModel.get(i).getLatitude(), destinationOptionMapModel.get(i).getLongitude());
                            latLogs.add(destinationOptionMapModel.get(i).getDes().get(0).getPlace());
                            double num = 0;
                            for(int d = 0; d < destinationOptionMapModel.get(i).getDes().size(); d++){
                                num = num +destinationOptionMapModel.get(i).getDes().get(d).price;
                            }
                            mMaps.addMarker(new MarkerOptions()
                                    .title(destinationOptionMapModel.get(i).getDes().get(0).getPlace())
                                    .snippet("$ " + num ).position(latLng));
                        }
                        latLng =  new LatLng(destinationOptionMapModel.get(0).getLatitude(), destinationOptionMapModel.get(0).getLongitude());
                        mMaps.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));
                    }

                    ArrayAdapter arrayAdapter = new ArrayAdapter(
                            MapViewActivity.this,
                            android.R.layout.simple_dropdown_item_1line,
                            latLogs);
                    arrayAdapter.setDropDownViewResource(
                            android.R.layout.simple_dropdown_item_1line
                    );

                    spinner.setAdapter(arrayAdapter);
                }
            }

            @Override
            public void onFailure(Call<ApiResponseResult<List<DestinationOptionMapModel>>> call, Throwable t) {

            }
        });
    }
}