package com.example.managerapp.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class TripModel {
    @SerializedName("_id")
    @Expose
    private String _id;
    
    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("startTime")
    @Expose
    private Date startTime;

    @SerializedName("totalPrice")
    @Expose
    private Number totalPrice;

    @SerializedName("endTime")
    @Expose
    private Date endTime;

    public TripModel(String name, Date startTime, Date endTime) {
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Number getTotalPrice() {
        return totalPrice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }
}
