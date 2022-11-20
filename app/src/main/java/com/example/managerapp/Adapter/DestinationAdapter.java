package com.example.managerapp.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.managerapp.ApiService.ApiService;
import com.example.managerapp.Model.ApiResponseResult;
import com.example.managerapp.Model.DestinationModel;
import com.example.managerapp.R;
import com.example.managerapp.SetOptionDestination;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DestinationAdapter extends RecyclerView.Adapter<DestinationAdapter.viewHover>{

    private List<DestinationModel> destinationModels = new ArrayList<DestinationModel>();
    private LayoutInflater  layoutInflater;
    private Context context;

    FragmentManager fragmentManager;
    GoogleMap googleMap;

    Spinner optionType;

    String valuePlace;
    String valueOption;
    Double valueLongitude;
    Boolean isFirst = false;
    Double valueLatitude;
    Button btnUpdate;
    Button btnDelete;
    AutocompleteSupportFragment autocompleteFragment;

    Dialog dialog;

    MaterialDatePicker dateRangePicker;
    Resources resources;

    TextInputEditText inpName;
    TextInputEditText inpPrice;
    TextInputEditText inpDes;
    TextInputEditText inpTime;

    TextView totalPriceText;


    OnMapReadyCallback onMapReadyCallback;

    public DestinationAdapter(Context context,
                              List<DestinationModel> datas,
                              FragmentManager fragmentManager,
                              Resources resources,
                              OnMapReadyCallback onMapReadyCallback,TextView totalPriceText) {
        this.context = context;
        this.destinationModels = datas;
        this.layoutInflater = LayoutInflater.from(context);
        this.fragmentManager = fragmentManager;
        this.resources = resources;
        this.onMapReadyCallback = onMapReadyCallback;
        this.totalPriceText = totalPriceText;
    }

    @NonNull
    @Override
    public DestinationAdapter.viewHover onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View recyclerViewItem = layoutInflater.inflate(R.layout.list_view_destination, parent, false);
        return new DestinationAdapter.viewHover(recyclerViewItem);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHover holder, int position) {
        DestinationModel destinationModel = this.destinationModels.get(position);
        String pattern = "MM-dd-yyyy";

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String date = simpleDateFormat.format(destinationModel.time);

        holder.descriptionView.setText(destinationModel.description);
        holder.placeView.setText(destinationModel.place);
        holder.priceView.setText("$ "+destinationModel.price);
        holder.typeNameView.setText(destinationModel.type + " - " + destinationModel.name);
        holder.timeView.setText(date);

        //update item btn
        onUpdateItem(destinationModel, holder, position);
        //delete item btn
        onDeleteItem(destinationModel.get_id(), position, holder);
    }

    @Override
    public int getItemCount() {
        return this.destinationModels.size();
    }


    public class viewHover extends RecyclerView.ViewHolder {

        TextView priceView;
        TextView timeView;
        TextView descriptionView;
        TextView placeView;
        TextView typeNameView;
        TextView btnUpdate;
        TextView btnDelete;

        viewHover(View view){
            super(view);
            this.priceView = (TextView) view.findViewById(R.id.value_price_destination);
            this.timeView = (TextView) view.findViewById(R.id.value_date_destination);
            this.descriptionView = (TextView) view.findViewById(R.id.value_description_destination);
            this.placeView = (TextView) view.findViewById(R.id.value_place_destination);
            this.typeNameView = (TextView) view.findViewById(R.id.title_list_destination);
            this.btnUpdate = (Button) view.findViewById(R.id.btn_update_destination);
            this.btnDelete = (Button) view.findViewById(R.id.btn_delete_destination);
        }
    }

    private void onUpdateItem(DestinationModel result, viewHover holder, int index){
        holder.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isFirst)
                {
                    dialog = new Dialog(context);

                    dialog.setContentView(R.layout.update_destination);
                }

                isFirst = true;

                autocompleteFragment = (AutocompleteSupportFragment)
                        fragmentManager.findFragmentById(R.id.destination_inp_place_update);

                SupportMapFragment mapFragment = (SupportMapFragment) fragmentManager
                        .findFragmentById(R.id.google_map_update);

                Button btnClose = dialog.findViewById(R.id.destination_btn_cancel_update_form);

                btnUpdate = dialog.findViewById(R.id.destination_btn_update_form);
                optionType = (Spinner) dialog.findViewById(R.id.destination_inp_type_update);
                inpName = dialog.findViewById(R.id.destination_inp_name_update);
                inpPrice = dialog.findViewById(R.id.destination_inp_price_update);
                inpDes = dialog.findViewById(R.id.destination_inp_des_update);
                inpTime = dialog.findViewById(R.id.destination_inp_time_update);

                Log.d("DDDD", result.getName());

                inpName.setText(result.getName());
                inpName.clearFocus();
                inpName.setError(null);
                inpPrice.setText(result.getPrice().toString());
                inpPrice.clearFocus();
                inpPrice.setError(null);
                inpDes.setText(result.getDescription());
                inpDes.clearFocus();
                inpDes.setError(null);
                SimpleDateFormat simpleFormat = new SimpleDateFormat("d/MM/y");
                inpTime.setText(simpleFormat.format(result.getTime()));
                inpTime.clearFocus();
                inpTime.setError(null);

                dateRangePicker = MaterialDatePicker.Builder
                        .datePicker().setSelection(new Long(result.getTime().getTime())).build();

                mapFragment.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(@NonNull GoogleMap googleMaps) {
                        LatLng latLng = new LatLng(result.getLatitude(), result.getLongitude());
                        googleMap = googleMaps;
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
                        googleMap.addMarker(new MarkerOptions().title("Test").snippet("123").position(latLng));

                    }
                });



                autocompleteFragment.setText(result.getPlace());

                valuePlace = result.getPlace();
                valueOption = result.getType();
                valueLongitude = result.getLongitude();
                valueLatitude = result.getLatitude();

                setDateInputCreate();
                setOption();

                SetOptionDestination setOptionDestination = new SetOptionDestination(result.getType());

                optionType.setSelection(setOptionDestination.getOptionByCode());

                setAutoCompletePlace(autocompleteFragment);

                btnClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                onUpdate(result, index);

                dialog.show();
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
                dateRangePicker.show(fragmentManager, "Create date time destination");

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
    private void setOption(){
        String[] option_type = resources.getStringArray(R.array.option_type);

        ArrayList<String> arrayList =
                new ArrayList<String>(Arrays.asList(option_type));

        ArrayAdapter<String> spinnerArrayAdapter
                = new ArrayAdapter<String>(context,
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

    private void onUpdate(DestinationModel destinationModel, int index){
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValidDateForm()){
                    Long dateRang = (Long) dateRangePicker.getSelection();
                    DestinationModel dataReq = new DestinationModel(
                            destinationModel.tripId, inpName.getText().toString(),
                            new Date(dateRang), valueOption, Double.parseDouble(inpPrice.getText().toString()),
                            inpDes.getText().toString(), valuePlace,
                            valueLongitude, valueLatitude
                    );
                    ApiService.apiService.UpdateDestinationApi(dataReq, destinationModel._id).enqueue(new Callback<ApiResponseResult<DestinationModel>>() {
                        @Override
                        public void onResponse(Call<ApiResponseResult<DestinationModel>> call, Response<ApiResponseResult<DestinationModel>> response) {
                            if(response.code() == 200){
                                destinationModels.set(index, response.body().results);
                                notifyItemChanged(index);
                                Toast.makeText(context, response.body().message, Toast.LENGTH_LONG).show();
                                setTotalPriceText();
                                dialog.dismiss();

                            } else{
                                Toast.makeText(context, "Update fail", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ApiResponseResult<DestinationModel>> call, Throwable t) {
                            Toast.makeText(context, "Update fail", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
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

                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
                googleMap.clear();
                googleMap.addMarker(new MarkerOptions().title("Test").snippet("123").position(latLng));
            }


            @Override
            public void onError(@NonNull Status status) {
                // TODO: Handle the error.
                Log.i("TAG", "An error occurred: " + status);
            }
        });
    }

    private void onDeleteItem(String id, int index, DestinationAdapter.viewHover viewHover){
        viewHover.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setTitle("Confirm delete destination");
                alertDialogBuilder.setMessage("Are you sure delete !!!");
                alertDialogBuilder.setIcon(R.drawable.ic_baseline_info_24);
                alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        ApiService.apiService.DeleteDestinationApi(id).enqueue(new Callback<ApiResponseResult<Object>>() {
                            @Override
                            public void onResponse(Call<ApiResponseResult<Object>> call, Response<ApiResponseResult<Object>> response) {
                                if(response.code() == 200){
                                    destinationModels.remove(index);
                                    notifyItemRemoved(index);
                                    notifyDataSetChanged();
                                    setTotalPriceText();
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

    private Boolean isValidDateForm(){

        Boolean isValid = true;

        if(inpName.getText().toString().trim().equalsIgnoreCase("")){
            inpName.setError("This is field required!");
            isValid = false;
        }

        if(inpTime.getText().toString().trim().equalsIgnoreCase("")){
            inpTime.setError("This is field required!");
            isValid = false;
        }

        if(inpPrice.getText().toString().trim().equalsIgnoreCase("")){
            inpPrice.setError("This is field required!");
            isValid = false;
        }
        if(valueOption.equalsIgnoreCase("")){
            TextView errorText = (TextView) optionType.getSelectedView();
            errorText.setError("This is field required!");
        }

        return isValid;
    }

    public void setTotalPriceText(){
        double num = 0;
        for(int i = 0; i < destinationModels.size(); i++){
            num = num + destinationModels.get(i).price;
        }
        totalPriceText.setText("$ " + num);
    }

}
