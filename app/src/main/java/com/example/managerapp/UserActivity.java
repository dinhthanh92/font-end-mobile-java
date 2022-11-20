package com.example.managerapp;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.managerapp.Adapter.UserAdapter;
import com.example.managerapp.ApiService.ApiService;
import com.example.managerapp.Model.ApiResponseResult;
import com.example.managerapp.Model.ListUserModel;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserActivity extends SetMenuOption {

    Button btn_create_user;

    List<ListUserModel> listUserModels = new ArrayList<ListUserModel>();
    UserAdapter userAdapter;
    String textSearch;
    RecyclerView recyclerView;

    TextInputEditText inpUsername;
    TextInputEditText inpEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        btn_create_user = findViewById(R.id.btn_create_user);
        this.recyclerView = (RecyclerView) this.findViewById(R.id.list_user);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                this, LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(linearLayoutManager);

        userAdapter = new UserAdapter(this, listUserModels,
                getSupportFragmentManager());
        getListData();
        buttonCreate();
    }

    public void buttonCreate(){
        Dialog dialog = new Dialog(UserActivity.this);
        dialog.setContentView(R.layout.form_create_user);
        btn_create_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inpUsername = dialog.findViewById(R.id.inp_username_view_user);
                inpEmail = dialog.findViewById(R.id.inp_email_view_user);
                inpUsername.clearFocus();
                inpUsername.setError(null);
                inpUsername.setText("");
                inpEmail.clearFocus();
                inpEmail.setError(null);
                inpEmail.setText("");
                Button btn_create_form_user = dialog.findViewById(R.id.btn_create_form_user);

                btn_create_form_user.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(checkValid()){
                            ListUserModel listUserModel = new ListUserModel(
                                    inpEmail.getText().toString().trim(),
                                    inpUsername.getText().toString().trim(),
                                    true
                            );
                            ApiService.apiService.CreateUser(listUserModel).enqueue(new Callback<ApiResponseResult<Object>>() {
                                @Override
                                public void onResponse(Call<ApiResponseResult<Object>> call, Response<ApiResponseResult<Object>> response) {
                                    if(response.code() == 200){
                                        Toast.makeText(UserActivity.this,
                                                        response.body().message, Toast.LENGTH_SHORT)
                                                .show();
                                        dialog.dismiss();
                                    }
                                    else if(response.code() < 300) {
                                        if(response.code() == 202){
                                            dialog.dismiss();
                                        }
                                        Toast.makeText(UserActivity.this,
                                                        response.body().message, Toast.LENGTH_SHORT)
                                                .show();
                                    }
                                    Toast.makeText(UserActivity.this,
                                                    "Error sever", Toast.LENGTH_SHORT)
                                            .show();
                                }

                                @Override
                                public void onFailure(Call<ApiResponseResult<Object>> call, Throwable t) {
                                    Toast.makeText(UserActivity.this,
                                                    "Error sever", Toast.LENGTH_SHORT)
                                            .show();
                                }
                            });
                        }
                    }
                });

                dialog.show();
            }
        });
    }

    public Boolean checkValid(){
        Boolean isValid =true;
        String userName = inpUsername.getText().toString().trim();
        String email = inpEmail.getText().toString().trim();
        if(email.equalsIgnoreCase("")){
            inpEmail.setError("This is field required!");
            isValid = false;
        } else if(!ClassUntil.isEmailValid(email)){
            inpEmail.setError("Example@gmail.com");
            isValid = false;
        }
        if(userName.equalsIgnoreCase("")){
            inpUsername.setError("This is field required!");
            isValid = false;
        }
        return  isValid;
    }

    public void getListData(){
        ApiService.apiService.IndexUser(textSearch).enqueue(new Callback<ApiResponseResult<List<ListUserModel>>>() {
            @Override
            public void onResponse(Call<ApiResponseResult<List<ListUserModel>>> call, Response<ApiResponseResult<List<ListUserModel>>> response) {
                if(response.code() == 200){
                    listUserModels = response.body().getResults();
                    userAdapter = new UserAdapter(UserActivity.this, listUserModels,
                            getSupportFragmentManager());
                    recyclerView.setAdapter(userAdapter);
                    userAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onFailure(Call<ApiResponseResult<List<ListUserModel>>> call, Throwable t) {

            }
        });
    }
}