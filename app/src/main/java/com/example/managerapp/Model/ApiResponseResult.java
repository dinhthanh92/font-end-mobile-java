package com.example.managerapp.Model;

import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ApiResponseResult<T> {
    @SerializedName("statusCode")
    @Expose
    public Integer statusCode;

    @SerializedName("results")
    @Expose
    public @Nullable T results;

    @SerializedName("isSuccess")
    @Expose
    public Boolean isSuccess;

    @SerializedName("message")
    @Expose
    public String message;

    public String getMessage() {
        return message;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public T getResults() {
        return results;
    }

    public Boolean getSuccess() {
        return isSuccess;
    }
}
