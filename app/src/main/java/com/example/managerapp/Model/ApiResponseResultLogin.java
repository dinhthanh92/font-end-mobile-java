package com.example.managerapp.Model;

import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ApiResponseResultLogin {
    @SerializedName("message")
    @Expose
    public String message;

    @SerializedName("results")
    @Expose
    public UserModel results;

    public String getMessage() {
        return message;
    }

    public UserModel getResults() {
        return results;
    }
}
