package com.example.managerapp;

import androidx.appcompat.app.AlertDialog;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.managerapp.Adapter.ListTripAdapter;
import com.example.managerapp.ApiService.ApiService;
import com.example.managerapp.Model.ApiResponseResult;
import com.example.managerapp.Model.DataStoreModel;
import com.example.managerapp.Model.TripModel;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TripActivity extends SetMenuOption {

    RecyclerView recyclerView;
    List<TripModel> tripModels = new ArrayList<TripModel>();
    ListTripAdapter listTripAdapter;
    DataStoreModel dataStore;
    EditText btnSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip);

        this.recyclerView = (RecyclerView) this.findViewById(R.id.list_trip);
        btnSearch = findViewById(R.id.search_trip);
        
        dataStore = MainActivity.GetStoreData();
        // RecyclerView scroll vertical
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        listTripAdapter = new ListTripAdapter(this, tripModels, getSupportFragmentManager());
        getListData("");
        btnCreateTrip();
        btnClearDataTrip(this);
        btnSearchTrip();
    }

    public void btnClearDataTrip(Context context){
        Button btnClearData = findViewById(R.id.btn_clear_data);

        btnClearData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setTitle("Confirm clear all data");
                alertDialogBuilder.setMessage("Are you sure clear all data !!!");
                alertDialogBuilder.setIcon(R.drawable.ic_baseline_info_24);
                alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ApiService.apiService.clearDataTripApi().enqueue(new Callback<ApiResponseResult<Object>>() {
                            @Override
                            public void onResponse(Call<ApiResponseResult<Object>> call, Response<ApiResponseResult<Object>> response) {
                                if(response.code() == 200){
                                    getListData("");
                                    Toast.makeText(TripActivity.this, response.body().message, Toast.LENGTH_SHORT)
                                            .show();

                                } else {
                                    Toast.makeText(TripActivity.this, "Error sever", Toast.LENGTH_SHORT)
                                            .show();
                                }

                            }

                            @Override
                            public void onFailure(Call<ApiResponseResult<Object>> call, Throwable t) {

                            }
                        });
                    }
                });
                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                alertDialogBuilder.show();
            }
        });
    }



    public void getListData(String nameSearch) {
        ApiService.apiService.getListTrip(nameSearch)
                .enqueue(new Callback<ApiResponseResult<List<TripModel>>>() {
                    @Override
                    public void onResponse(Call<ApiResponseResult<List<TripModel>>> call,
                                           Response<ApiResponseResult<List<TripModel>>> response) {

                        if(response.code() == 200){

                            tripModels = response.body().getResults();
                            listTripAdapter = new ListTripAdapter(TripActivity.this, tripModels,
                                    getSupportFragmentManager());
                            recyclerView.setAdapter(listTripAdapter);
                            listTripAdapter.notifyDataSetChanged();

                        } else {
                            Toast.makeText(TripActivity.this, "Error sever", Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponseResult<List<TripModel>>> call, Throwable t) {
                        Toast.makeText(TripActivity.this, "Fail API", Toast.LENGTH_SHORT)
                                .show();
                    }
                });
    }


    private void btnCreateTrip () {

        Button btnCreate = findViewById(R.id.btn_create);

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(TripActivity.this);
                dialog.setContentView(R.layout.createtripview);

                Button btnCreateForm = dialog.findViewById(R.id.btn_create_form);
                EditText inpTextTrip = dialog.findViewById(R.id.inp_name);
                EditText select_date_inp = dialog.findViewById(R.id.inp_date);

                MaterialDatePicker dateRangePicker =
                        MaterialDatePicker.Builder.dateRangePicker().build();

                select_date_inp.setRawInputType(InputType.TYPE_NULL);
                select_date_inp.setFocusable(true);

                select_date_inp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        select_date_inp.setEnabled(false);
                        dateRangePicker.show(getSupportFragmentManager(), "Create date time");
                        dateRangePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
                            @Override
                            public void onPositiveButtonClick(Object selection) {
                                //set date time
                                Pair selectedDates = (Pair) dateRangePicker.getSelection();
                                final Pair<Date, Date> rangeDate = new Pair<>(new Date((Long) selectedDates.first), new Date((Long) selectedDates.second));
                                Date startDate = rangeDate.first;
                                Date endDate = rangeDate.second;
                                SimpleDateFormat simpleFormat = new SimpleDateFormat("d/MM/y");
                                select_date_inp.setText(simpleFormat.format(startDate) + " - " + simpleFormat.format(endDate));
                                select_date_inp.setEnabled(true);
                                select_date_inp.setError(null);
                            }
                        });
                        dateRangePicker.addOnNegativeButtonClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                select_date_inp.setEnabled(true);
                            }
                        });
                    }
                });

                btnCreateForm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String name = inpTextTrip.getText().toString().trim();
                        Date startDate = null;
                        Date endDate = null;

                        Pair selectedDates = (Pair) dateRangePicker.getSelection();

                        if(selectedDates.first != null && selectedDates.second != null){
                            final Pair<Date, Date> rangeDate = new Pair<>(new Date((Long) selectedDates.first), new Date((Long) selectedDates.second));
                            startDate = rangeDate.first;
                            endDate = rangeDate.second;
                        }

                        if(name.equalsIgnoreCase("") )
                            inpTextTrip.setError("This is required field!");
                        else if(endDate == null || startDate == null){
                            select_date_inp.setError("This is required field");
                        } else {
                            TripModel newList = new TripModel(
                                    name, startDate, endDate);
                            CallApiCreate(newList, dialog);
                        }
                    }

                });
                dialog.show();
            }
        });
    }

    private Timer timer = new Timer();
    private final long DELAY = 1000; // Millisec

    private void btnSearchTrip () {
        btnSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                timer.cancel();
                timer = new Timer();
                timer.schedule(
                        new TimerTask() {
                            @Override
                            public void run() {
                                getListData(editable.toString().trim());
                            }
                        },
                        DELAY
                );
            }
        });
    }
    private void CallApiCreate(TripModel tripModel, Dialog dialog) {
        ApiService.apiService.createTripApi(tripModel).enqueue(new Callback<ApiResponseResult<Object>>() {
            @Override
            public void onResponse(Call<ApiResponseResult<Object>> call, Response<ApiResponseResult<Object>> response) {
                if(response.code() == 200){
                    dialog.dismiss();

                    String searchText = "";

                    if(!btnSearch.getText().toString().trim().equalsIgnoreCase(""))
                        searchText = btnSearch.getText().toString().trim();

                    getListData(searchText);

                    Toast.makeText(TripActivity.this, response.body().message, Toast.LENGTH_SHORT)
                            .show();
                } else {
                    Toast.makeText(TripActivity.this, "Error sever", Toast.LENGTH_SHORT)
                            .show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponseResult<Object>> call, Throwable t) {
                Toast.makeText(TripActivity.this, "Fail API", Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }
    /*public static TripDataStoreModel GetStoreDataTrip(){
        TripDataStoreModel tripDataStoreModel =
                dataStoreManagerTrip.getDataStore().blockingFirst();
        return tripDataStoreModel;
    }

    public static void SetStoreDataTrip(TripDataStoreModel data){
        TripActivity.dataStoreManagerTrip.save(data);
    }*/
}