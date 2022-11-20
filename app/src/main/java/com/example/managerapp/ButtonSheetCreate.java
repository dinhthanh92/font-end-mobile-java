package com.example.managerapp;
import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.AnyRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.managerapp.Adapter.ListTripAdapter;
import com.example.managerapp.Model.ApiResponseResult;
import com.example.managerapp.Model.TripModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ButtonSheetCreate extends BottomSheetDialogFragment {
//    RecyclerView recyclerView;
//    TripModel tripModel;
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable
//            ViewGroup container, @Nullable Bundle savedInstanceState)
//    {
//        View view = inflater.inflate(R.layout.createtripview,
//                container, false);
//
//        EditText select_date_inp = view.findViewById(R.id.inp_date);
//        EditText inpTextTrip = view.findViewById(R.id.inp_name);
//        Button btnCreateForm = (Button) view.findViewById(R.id.btn_create_form);
//        MaterialDatePicker dateRangePicker =
//                MaterialDatePicker.Builder.dateRangePicker().build();
//
//        recyclerView = view.findViewById(R.id.list_trip);
//
//
//        select_date_inp.setRawInputType(InputType.TYPE_NULL);
//        select_date_inp.setFocusable(true);
//
//        select_date_inp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                select_date_inp.setEnabled(false);
//                dateRangePicker.show(getChildFragmentManager(), "Create date time");
//                dateRangePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
//                    @Override
//                    public void onPositiveButtonClick(Object selection) {
//                        //set date time
//                        Pair selectedDates = (Pair) dateRangePicker.getSelection();
//                        final Pair<Date, Date> rangeDate = new Pair<>(new Date((Long) selectedDates.first), new Date((Long) selectedDates.second));
//                        Date startDate = rangeDate.first;
//                        Date endDate = rangeDate.second;
//                        SimpleDateFormat simpleFormat = new SimpleDateFormat("d/MM/y");
//                        select_date_inp.setText(simpleFormat.format(startDate) + " - " + simpleFormat.format(endDate));
//                        select_date_inp.setEnabled(true);
//                        select_date_inp.setError(null);
//                    }
//                });
//                dateRangePicker.addOnNegativeButtonClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        select_date_inp.setEnabled(true);
//                    }
//                });
//            }
//        });
//
//        btnCreateForm.setOnClickListener(new View.OnClickListener() {
//
//        @Override
//        public void onClick(View view) {
//            String name = inpTextTrip.getText().toString().trim();
//            Date startDate = null;
//            Date endDate = null;
//
//            Pair selectedDates = (Pair) dateRangePicker.getSelection();
//            if(selectedDates.first != null && selectedDates.second != null){
//                final Pair<Date, Date> rangeDate = new Pair<>(new Date((Long) selectedDates.first), new Date((Long) selectedDates.second));
//                startDate = rangeDate.first;
//                endDate = rangeDate.second;
//            }
//
//            if(name.equalsIgnoreCase("") )
//                inpTextTrip.setError("This is required field!");
//
//            else if(endDate == null || startDate == null){
//                select_date_inp.setError("This is required field");
//            } else {
//                TripModel newList = new TripModel(
//                        name, startDate, endDate);
//                onCreateTrip(newList, view.getContext());
//            }
//        }
//    });
//        return view;
//    }
//
//    private void onCreateTrip(TripModel tripModel, Context context) {
//        ApiService.apiService.createTripApi(tripModel).enqueue(new Callback<ApiResponseResult<Object>>() {
//            @Override
//            public void onResponse(Call<ApiResponseResult<Object>> call, Response<ApiResponseResult<Object>> response) {
//                onDismiss(getDialog());
//            }
//
//            @Override
//            public void onFailure(Call<ApiResponseResult<Object>> call, Throwable t) {
//                Toast.makeText(context, "Fail API", Toast.LENGTH_SHORT)
//                        .show();
//            }
//        });
//    }


}

