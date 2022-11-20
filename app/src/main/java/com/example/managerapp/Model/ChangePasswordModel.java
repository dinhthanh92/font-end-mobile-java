package com.example.managerapp.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChangePasswordModel {
    @SerializedName("password")
    @Expose
    public String password;

    @SerializedName("token")
    @Expose
    public String token;

    public ChangePasswordModel(String password, String token){
        this.password = password;
        this.token = token;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
