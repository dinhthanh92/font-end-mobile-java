package com.example.managerapp.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataStoreModel {

    @SerializedName("token")
    @Expose
    public String token ;

    @SerializedName("user")
    @Expose
    public UserModel user;

    public DataStoreModel(String token, UserModel user) {
        this.token = token;
        this.user = user;
    }

    public DataStoreModel() {}

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }
}
