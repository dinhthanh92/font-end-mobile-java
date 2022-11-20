package com.example.managerapp.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class DestinationModel {
    @SerializedName("_id")
    @Expose
    public String _id ;

    @SerializedName("name")
    @Expose
    public String name ;

    @SerializedName("price")
    @Expose
    public Double price;

    @SerializedName("latitude")
    @Expose
    public Double latitude;

    @SerializedName("longitude")
    @Expose
    public Double longitude;

    @SerializedName("type")
    @Expose
    public String type;

    @SerializedName("tripId")
    @Expose
    public String tripId;

    @SerializedName("time")
    @Expose
    public Date time;

    @SerializedName("place")
    @Expose
    public String place;

    @SerializedName("description")
    @Expose
    public String description;

    public DestinationModel( String tripId,
            String name, Date time, String type,
            Double price, String description, String place,
            Double longitude, Double latitude
    ) {
        this.tripId = tripId;
        this.name = name;
        this.type = type;
        this.time = time;
        this.price = price;
        this.description = description;
        this.longitude = longitude;
        this.place = place;
        this.latitude = latitude;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {

        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }
}
