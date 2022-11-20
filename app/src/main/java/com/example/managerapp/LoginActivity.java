package com.example.managerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.managerapp.ApiService.ApiAuth;
import com.example.managerapp.ApiService.ApiCheckLogin;
import com.example.managerapp.ApiService.ApiService;
import com.example.managerapp.Model.ApiResponseResult;
import com.example.managerapp.Model.ChangePasswordModel;
import com.example.managerapp.Model.DataStoreModel;
import com.example.managerapp.Model.LoginModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    EditText username;
    EditText password;
    Button buttonSignIn;
    ProgressBar progressBar;
    ApiService token;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Log.d("LOGIN", MainActivity.GetStoreData().getToken());

        username = this.findViewById(R.id.username);
        password = this.findViewById(R.id.password);
        buttonSignIn = this.findViewById(R.id.login_button);
        progressBar = this.findViewById(R.id.loading);
        dialog = new Dialog(LoginActivity.this);
        dialog.setContentView(R.layout.change_password_form);
        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonSignIn.setEnabled(false);
                progressBar.setVisibility(View.VISIBLE);
                if(username.getText().toString().trim().equalsIgnoreCase("") ||
                        password.getText().toString().trim().equalsIgnoreCase("")){
                    if(username.getText().toString().trim().equalsIgnoreCase("") )
                        username.setError("This is required field!");
                    if(password.getText().toString().trim().equalsIgnoreCase("") )
                        password.setError("This is required field!");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            buttonSignIn.setEnabled(true);
                            progressBar.setVisibility(View.GONE);
                        }
                    }, 500);
                } else {
                    LoginModel loginModel = new LoginModel(
                            password.getText().toString(),
                            username.getText().toString()
                    );

                    RequestLogin(LoginActivity.this, loginModel, buttonSignIn);
                }
            }
        });
    }

    private void CallApiChangePassword(DataStoreModel dataStoreModel, String password){
        ChangePasswordModel changePasswordModel = new ChangePasswordModel(password, dataStoreModel.getToken());
        ApiAuth.apiService.changePassword(changePasswordModel).enqueue(new Callback<ApiResponseResult<Object>>() {
            @Override
            public void onResponse(Call<ApiResponseResult<Object>> call, Response<ApiResponseResult<Object>> response) {
                if(response.code() == 200){
                    MainActivity.SetStoreData(dataStoreModel);
                    final Intent intents = new Intent(LoginActivity.this, TripActivity.class);
                    intents.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    LoginActivity.this.finish();
                    startActivity(intents);
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ApiResponseResult<Object>> call, Throwable t) {

            }
        });
    }

    private void RequestLogin(Context context, LoginModel loginModel, Button buttonSignIn) {
        ApiAuth.apiService.postLogin(loginModel).enqueue(new Callback<ApiResponseResult<DataStoreModel>>() {
            @Override
            public void onResponse(Call<ApiResponseResult<DataStoreModel>> call,
                                   Response<ApiResponseResult<DataStoreModel>> response) {
                if(response.code() == 200) {
                    if(response.body().results.getUser().getIsFirst()){
                        Button btnSuccess = dialog.findViewById(R.id.btn_change_password_form);
                        EditText password = dialog.findViewById(R.id.inp_password_form_change);
                        EditText confirmPass = dialog.findViewById(R.id.inp__confirm_password_form_change);
                        btnSuccess.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Boolean isValid = true;
                                if(password.getText().toString().trim().equalsIgnoreCase("")){
                                    password.setError("This is field required!");
                                    isValid = false;
                                }
                                if(confirmPass.getText().toString().trim().equalsIgnoreCase("")){
                                    confirmPass.setError("This is field required!");
                                    isValid = false;
                                } else if(isValid && !password.getText().toString().trim().equalsIgnoreCase(confirmPass.getText().toString().trim())){
                                    confirmPass.setError("Confirm password not match!");
                                    isValid = false;
                                }
                                Log.d("CHANGEPASS", isValid + "");
                                if(isValid){
                                    CallApiChangePassword(response.body().results, password.getText().toString().trim());
                                }
                            }
                        });
                        dialog.show();
                    } else{
                        MainActivity.SetStoreData(response.body().results);
                        final Intent intents = new Intent(LoginActivity.this, TripActivity.class);
                        intents.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        LoginActivity.this.finish();
                        startActivity(intents);
                    }

                }else
                    Toast.makeText(context, "Username or password wrong", Toast.LENGTH_SHORT)
                            .show();


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        buttonSignIn.setEnabled(true);
                        progressBar.setVisibility(View.GONE);
                    }
                }, 500);
            }

            @Override
            public void onFailure(Call<ApiResponseResult<DataStoreModel>> call, Throwable t) {
                Toast.makeText(context, "Error sever", Toast.LENGTH_SHORT)
                        .show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        buttonSignIn.setEnabled(true);
                        progressBar.setVisibility(View.GONE);
                    }
                }, 500);
            }
        });

    }


}