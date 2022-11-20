package com.example.managerapp.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataStoreDestinationModel {
    @SerializedName("_id")
    @Expose
    public String _id ;

    public DataStoreDestinationModel(String _id){
        this._id = _id;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }
}
