package com.example.managerapp;

import androidx.annotation.NonNull;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.rxjava3.RxPreferenceDataStoreBuilder;
import androidx.datastore.rxjava3.RxDataStore;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.managerapp.Adapter.DestinationAdapter;
import com.example.managerapp.ApiService.ApiService;
import com.example.managerapp.Model.ApiResponseResult;
import com.example.managerapp.Model.DestinationModel;
import com.example.managerapp.Model.TripModel;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DestinationActivity extends SetMenuOption  implements OnMapReadyCallback  {
    GoogleMap mMap;
    RecyclerView recyclerView;
    List<DestinationModel> destinationModels = new ArrayList<DestinationModel>();
    DestinationAdapter destinationAdapter;

    Dialog dialog;

    String valuePlace;
    EditText searchText;
    String valueOption;
    Double valueLongitude;
    Double valueLatitude;
    RxDataStore<Preferences> rxDataStoreTrip;


    TextView headerTrip;
    TextView startTimeText;
    TextView endTimeText;
    TextView totalPriceText;
    MaterialDatePicker dateRangePicker;
    Button btnMapVIew;

    TextInputEditText inpName;
    TextInputEditText inpPrice;
    TextInputEditText inpDes;
    TextInputEditText inpTime;

    Spinner optionType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destination);
        Button createBtn = this.findViewById(R.id.btn_create_destination);

        rxDataStoreTrip = new RxPreferenceDataStoreBuilder(DestinationActivity.this, "dataDestination").build();

        headerTrip = findViewById(R.id.header_trip_destination);
        startTimeText = findViewById(R.id.end_date_view_destination);
        endTimeText = findViewById(R.id.start_date_view_destination);
        totalPriceText = findViewById(R.id.total_price_destination);
        searchText = findViewById(R.id.search_destination);
        btnMapVIew = findViewById(R.id.btn_google_map);
        String apiKey = getString(R.string.api_place_google_api);
        Places.initialize(this, apiKey);

        this.recyclerView = (RecyclerView) this.findViewById(R.id.list_destination);
        // RecyclerView scroll vertical

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                this, LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(linearLayoutManager);

        destinationAdapter = new DestinationAdapter(this, destinationModels,
                getSupportFragmentManager(), getResources(),
                DestinationActivity.this, totalPriceText);

        getTripData();
        getListData();

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.create_destination);

        dateRangePicker = MaterialDatePicker.Builder.datePicker().build();

        btnMapVIew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intents = new Intent(DestinationActivity.this,
                        MapViewActivity.class);

                startActivity(intents);
            }
        });

        searchText.addTextChangedListener(new TextWatcher(){

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Timer timer = new Timer();
                timer.cancel();
                timer = new Timer();
                timer.schedule(
                        new TimerTask() {
                            @Override
                            public void run() {
                                getListData();
                            }
                        },
                        1000
                );
            }
        });

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Button btnClose = dialog.findViewById(R.id.destination_btn_cancel_form);

                AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                        getSupportFragmentManager().findFragmentById(R.id.destination_inp_place);

                setGoogleMap(DestinationActivity.this);

                optionType = dialog.findViewById(R.id.destination_inp_type);
                Button btnCreateForm = (Button) dialog.findViewById(R.id.destination_btn_create_form);

                inpName = dialog.findViewById(R.id.destination_inp_name);
                inpPrice = dialog.findViewById(R.id.destination_inp_price);
                inpDes = dialog.findViewById(R.id.destination_inp_des);
                inpTime = dialog.findViewById(R.id.destination_inp_time);

                inpName.setText(null);
                inpName.clearFocus();
                inpPrice.setText(null);
                inpPrice.clearFocus();
                inpDes.setText(null);
                inpDes.clearFocus();
                inpTime.setText(null);
                inpTime.clearFocus();

                autocompleteFragment.setText(null);
                if(valuePlace != null)
                    mMap.clear();

                valuePlace = null;
                valueOption = null;
                valueLongitude = null;
                valueLatitude = null;



                setDateInputCreate();
                setOption();

                setAutoCompletePlace(autocompleteFragment);

                btnClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                onButtonCreateForm(btnCreateForm);

                dialog.show();
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
//        LatLng latLng = new LatLng(-33.867, 151.206);
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
//
//        mMap.addMarker(new MarkerOptions().title("Test").snippet("123").position(latLng));
    }

    public void getListData() {
        ApiService.apiService.getListDestination(
                MainActivity.GetStoreDataTrip(),
                        searchText.getText().toString().trim()
                ).enqueue(new Callback<ApiResponseResult<List<DestinationModel>>>() {
                    @Override
                    public void onResponse(Call<ApiResponseResult<List<DestinationModel>>> call, Response<ApiResponseResult<List<DestinationModel>>> response) {
                        if(response.code() == 200){

                            destinationModels = response.body().getResults();
                            destinationAdapter = new DestinationAdapter(DestinationActivity.this,
                                    destinationModels, getSupportFragmentManager(),
                                    getResources(), DestinationActivity.this, totalPriceText);
                            destinationAdapter.setTotalPriceText();

                            recyclerView.setAdapter(destinationAdapter);
                            destinationAdapter.notifyDataSetChanged();
                        } else
                            Toast.makeText(DestinationActivity.this, "Fail API", Toast.LENGTH_SHORT)
                                    .show();
                    }

                    @Override
                    public void onFailure(Call<ApiResponseResult<List<DestinationModel>>> call, Throwable t) {
                        Toast.makeText(DestinationActivity.this, "Error sever", Toast.LENGTH_SHORT)
                                .show();
                    }
                });
    }

    public void getTripData() {
        ApiService.apiService.getShowTrip(MainActivity.GetStoreDataTrip())
                .enqueue(new Callback<ApiResponseResult<TripModel>>() {
                    @Override
                    public void onResponse(Call<ApiResponseResult<TripModel>> call, Response<ApiResponseResult<TripModel>> response) {
                        if(response.code() == 200){
                            TripModel result = response.body().getResults();

                            String pattern = "MM-dd-yyyy";

                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                            String startTime = simpleDateFormat.format(result.getStartTime());
                            String endTime = simpleDateFormat.format(result.getEndTime());

                            headerTrip.setText(result.getName());
                            startTimeText.setText(startTime);
                            endTimeText.setText(endTime);
                        } else {
                            Toast.makeText(DestinationActivity.this, "Fail API", Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponseResult<TripModel>> call, Throwable t) {
                        Toast.makeText(DestinationActivity.this, "Error sever", Toast.LENGTH_SHORT)
                                .show();
                    }
                });
    }

    private void setOption(){
        String[] option_type = getResources().getStringArray(R.array.option_type);

        ArrayList<String> arrayList =
                new ArrayList<String>(Arrays.asList(option_type));

        ArrayAdapter<String> spinnerArrayAdapter
                = new ArrayAdapter<String>(DestinationActivity.this,
                android.R.layout.simple_dropdown_item_1line,
                arrayList){
            @Override
            public boolean isEnabled(int position){
                // Disable the first item from Spinner
                // First item will be use for hint
                return position != 0;
            }
            @Override
            public View getDropDownView(
                    int position, View convertView,
                    @NonNull ViewGroup parent) {

                // Get the item view
                View view = super.getDropDownView(
                        position, convertView, parent);
                TextView textView = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    textView.setTextColor(Color.GRAY);
                }
                else {
                    textView.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        spinnerArrayAdapter.setDropDownViewResource(
                android.R.layout.simple_dropdown_item_1line
        );
        optionType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SetOptionDestination setOptionDestination = new SetOptionDestination(position);
                valueOption = setOptionDestination.getOption();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Finally, data bind the spinner object with adapter
        optionType.setAdapter(spinnerArrayAdapter);
    }

    private void setAutoCompletePlace(AutocompleteSupportFragment autocompleteFragment){

        autocompleteFragment.setHint("Enter place (*)");

        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME,
                Place.Field.LAT_LNG, Place.Field.ADDRESS));



        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                // TODO: Get info about the selected place.
                LatLng latLng = new LatLng(place.getLatLng().latitude, place.getLatLng().longitude);

                valueLatitude = latLng.latitude;
                valueLongitude = latLng.longitude;
                valuePlace = place.getAddress();

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
                mMap.clear();
                mMap.addMarker(new MarkerOptions().title("Test").snippet("123").position(latLng));
            }


            @Override
            public void onError(@NonNull Status status) {
                // TODO: Handle the error.
                Log.i("TAG", "An error occurred: " + status);
            }
        });
    }

    private void setDateInputCreate(){

        inpTime.setRawInputType(InputType.TYPE_NULL);
        inpTime.setFocusable(true);

        inpTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inpTime.setEnabled(false);

                dateRangePicker.show(getSupportFragmentManager(), "Create date time destination");

                dateRangePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
                    @Override
                    public void onPositiveButtonClick(Long selection) {
                        Long dateNew = (Long) dateRangePicker.getSelection();
                        SimpleDateFormat simpleFormat = new SimpleDateFormat("d/MM/y");
                        inpTime.setText(simpleFormat.format(dateNew));
                        inpTime.setEnabled(true);
                    }
                });

                dateRangePicker.addOnNegativeButtonClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        inpTime.setEnabled(true);
                    }
                });
            }
        });
    }

    private void onButtonCreateForm(Button btn){
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(valueLatitude == null || valueLongitude == null ||
                    valueOption == null || valuePlace == null
                ){
                    Toast.makeText(DestinationActivity.this,
                            valueOption, Toast.LENGTH_SHORT).show();
                } else {
                    Long dateRang = (Long) dateRangePicker.getSelection();
                    DestinationModel newDestinationModel = new DestinationModel(
                            MainActivity.GetStoreDataTrip(),
                            inpName.getText().toString().trim(),new Date(dateRang),
                            valueOption, Double.parseDouble(inpPrice.getText().toString()),
                            inpDes.getText().toString().trim(), valuePlace, valueLongitude, valueLatitude
                    );
                    ApiService.apiService.createDestinationApi(newDestinationModel).enqueue(new Callback<ApiResponseResult<Object>>() {
                        @Override
                        public void onResponse(Call<ApiResponseResult<Object>> call, Response<ApiResponseResult<Object>> response) {
                            if(response.code() == 200){
                                Toast.makeText(DestinationActivity.this,
                                        response.body().message, Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                getListData();
                            } else {
                                Toast.makeText(DestinationActivity.this,
                                        "FAIL API", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ApiResponseResult<Object>> call, Throwable t) {
                            Toast.makeText(DestinationActivity.this,
                                    "Error sever", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    public void setGoogleMap(OnMapReadyCallback onMapReadyCallback){
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.google_map);

        mapFragment.getMapAsync(onMapReadyCallback);
    }

}