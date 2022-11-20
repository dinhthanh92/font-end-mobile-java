package com.example.managerapp.Model;

import com.google.gson.annotations.SerializedName;


public class LoginModel {

    @SerializedName("username")
    public String username;

    @SerializedName("password")
    public String password;

    public LoginModel(String password, String username) {
        this.password = password;
        this.username = username;
    }


}
