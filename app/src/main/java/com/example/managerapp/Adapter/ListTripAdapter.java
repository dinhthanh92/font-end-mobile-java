package com.example.managerapp.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.util.Pair;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.managerapp.ApiService.ApiService;
import com.example.managerapp.DestinationActivity;
import com.example.managerapp.MainActivity;
import com.example.managerapp.Model.ApiResponseResult;
import com.example.managerapp.Model.TripDataStoreModel;
import com.example.managerapp.Model.TripModel;
import com.example.managerapp.R;
import com.example.managerapp.TripActivity;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListTripAdapter  extends RecyclerView.Adapter<ListTripAdapter.viewHover> {

    private List<TripModel> tripModels = new ArrayList<TripModel>();
    private LayoutInflater  layoutInflater;
    private Context context;
    FragmentManager fragmentManager;

    public ListTripAdapter(Context context, List<TripModel> datas, FragmentManager fragmentManager) {
        this.context = context;
        this.tripModels = datas;
        this.layoutInflater = LayoutInflater.from(context);
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public ListTripAdapter.viewHover onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View recyclerViewItem = layoutInflater.inflate(R.layout.listview, parent, false);

//        recyclerViewItem.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                handleRecyclerItemClick( (RecyclerView)parent, v);
//            }
//        });
        return new ListTripAdapter.viewHover(recyclerViewItem);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHover holder, int position) {
// Cet country in countries via position
        TripModel tripModel = this.tripModels.get(position);

        String pattern = "MM-dd-yyyy";

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String startDate = simpleDateFormat.format(tripModel.getStartTime());
        String endDate = simpleDateFormat.format(tripModel.getEndTime());

        // Bind data to viewholder
        holder.nameView.setText(tripModel.getName());
        holder.startTimeView.setText(startDate);
        holder.endTimeView.setText(endDate);

        //update
        onUpdateItem(tripModel, holder, position);
        onDeleteItem(tripModel.get_id(), holder, position);
        onViewItem(holder, tripModel);
    }

    @Override
    public int getItemCount() {
        return this.tripModels.size();
    }

    // Find Image ID corresponding to the name of the image (in the directory drawable).
    public int getDrawableResIdByName(String resName)  {
        String pkgName = context.getPackageName();
        // Return 0 if not found.
        int resID = context.getResources().getIdentifier(resName , "drawable", pkgName);
        Log.i("MainActivi", "Res Name: "+ resName+"==> Res ID = "+ resID);
        return resID;
    }

    public static class viewHover extends RecyclerView.ViewHolder {
        TextView nameView;
        TextView startTimeView;
        Button btnUpdate;
        Button btnDelete;
        Button btnView;
        TextView endTimeView;
        viewHover(View view){
            super(view);
            this.nameView = (TextView) view.findViewById(R.id.name_view);
            this.startTimeView = (TextView) view.findViewById(R.id.start_date_view);
            this.endTimeView = (TextView) view.findViewById(R.id.end_date_view);
            this.btnUpdate = (Button) view.findViewById(R.id.btn_update);
            this.btnDelete = (Button) view.findViewById(R.id.btn_delete);
            this.btnView = (Button) view.findViewById(R.id.btn_view);
        }
    }

    private void handleRecyclerItemClick(RecyclerView recyclerView, View itemView) {
        int itemPosition = recyclerView.getChildLayoutPosition(itemView);
        TripModel tripModel  = this.tripModels.get(itemPosition);

    }

    private void onUpdateItem(TripModel tripModel, viewHover holder, int index){
        holder.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.update_trip);

                EditText inp_name_update = dialog.findViewById(R.id.inp_name_update);
                Button btn_update_form = dialog.findViewById(R.id.btn_update_form);
                EditText inp_date_update = dialog.findViewById(R.id.inp_date_update);

                inp_date_update.setRawInputType(InputType.TYPE_NULL);
                inp_date_update.setFocusable(true);
                SimpleDateFormat simpleFormat = new SimpleDateFormat("d/MM/y");

                inp_name_update.setText(tripModel.getName());
                inp_date_update.setText(
                        simpleFormat.format(tripModel.getStartTime()) + " - " +
                                simpleFormat.format(tripModel.getEndTime()));

                MaterialDatePicker.Builder<Pair<Long, Long>>
                        dateRangePickerBuilder = MaterialDatePicker.Builder
                        .dateRangePicker();
                MaterialDatePicker materialDatePicker = dateRangePickerBuilder
                        .setSelection(
                                new Pair(tripModel.getStartTime().getTime(), tripModel.getEndTime().getTime())).build();

                inp_date_update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        inp_date_update.setEnabled(false);

                        materialDatePicker.show(fragmentManager, "RANGE DATE");
                        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
                            @Override
                            public void onPositiveButtonClick(Object selection) {
                                //set date time
                                Pair selectedDates = (Pair) materialDatePicker.getSelection();
                                final Pair<Date, Date> rangeDate = new Pair<>(new Date((Long) selectedDates.first), new Date((Long) selectedDates.second));
                                Date startDate = rangeDate.first;
                                Date endDate = rangeDate.second;
                                SimpleDateFormat simpleFormat = new SimpleDateFormat("d/MM/y");
                                inp_date_update.setText(simpleFormat.format(startDate) + " - " + simpleFormat.format(endDate));
                                inp_date_update.setEnabled(true);
                                inp_date_update.setError(null);
                            }
                        });
                        materialDatePicker.addOnNegativeButtonClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                inp_date_update.setEnabled(true);
                            }
                        });
                    }
                });
                btn_update_form.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String name = inp_name_update.getText().toString().trim();
                        Date startDate = null;
                        Date endDate = null;

                        Pair selectedDates = (Pair) materialDatePicker.getSelection();

                        if(selectedDates.first != null && selectedDates.second != null){
                            final Pair<Date, Date> rangeDate = new Pair<>(new Date((Long) selectedDates.first), new Date((Long) selectedDates.second));
                            startDate = rangeDate.first;
                            endDate = rangeDate.second;
                        }

                        if(name.equalsIgnoreCase("") )
                            inp_name_update.setError("This is required field!");
                        else if(endDate == null || startDate == null){
                            inp_date_update.setError("This is required field");
                        } else {
                            TripModel newTripReq = new TripModel(name, startDate, endDate);
                            onCallAPIUpdateItem(newTripReq,tripModel.get_id(), index, dialog);
                        }
                    }

                });
                dialog.show();
            }
        });
    }
    private void onCallAPIUpdateItem(TripModel newTripReq,String id, int index, Dialog dialog){
        ApiService.apiService.updateTripApi(newTripReq, id).enqueue(new Callback<ApiResponseResult<TripModel>>() {
            @Override
            public void onResponse(Call<ApiResponseResult<TripModel>> call, Response<ApiResponseResult<TripModel>> response) {
                if(response.code() == 200){
                    tripModels.set(index, response.body().results);
                    Toast.makeText(context, response.body().message, Toast.LENGTH_LONG).show();
                    notifyItemChanged(index);
                    dialog.dismiss();
                } else {
                    Toast.makeText(context, response.body().message, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponseResult<TripModel>> call, Throwable t) {
                Toast.makeText(context, "Error sever", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void onDeleteItem(String id, viewHover holder, int index){
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setTitle("Confirm delete trip");
                alertDialogBuilder.setMessage("Are you sure delete !!!");
                alertDialogBuilder.setIcon(R.drawable.ic_baseline_info_24);
                alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        ApiService.apiService.deleteTripApi(id).enqueue(new Callback<ApiResponseResult<Object>>() {
                            @Override
                            public void onResponse(Call<ApiResponseResult<Object>> call, Response<ApiResponseResult<Object>> response) {
                                if(response.code() == 200){
                                    tripModels.remove(index);
                                    notifyItemRemoved(index);
                                    notifyDataSetChanged();
                                }
                                Toast.makeText(context, response.body().message, Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onFailure(Call<ApiResponseResult<Object>> call, Throwable t) {
                                Toast.makeText(context, "Error sever", Toast.LENGTH_LONG).show();
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
    private void onViewItem(viewHover holder, TripModel tripModels){
        holder.btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.SetStoreDataTrip(tripModels.get_id());
                final Intent intents = new Intent(context,
                        DestinationActivity.class);
                intents.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intents);
            }
        });
    }
}
