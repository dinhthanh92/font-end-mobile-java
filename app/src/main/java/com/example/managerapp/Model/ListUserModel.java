package com.example.managerapp.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ListUserModel {
    @SerializedName("_id")
    @Expose
    public String _id;

    @SerializedName("email")
    @Expose
    public String email;

    @SerializedName("username")
    @Expose
    public String username;

    @SerializedName("status")
    @Expose
    public Boolean status;

    public ListUserModel(String email,String username, Boolean status){
        this.email = email;
        this.username = username;
        this.status = status;
    }

    public ListUserModel(String email,String username, Boolean status, String _id){
        this.email = email;
        this.username = username;
        this.status = status;
        this._id = _id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }
}
