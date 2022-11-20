package com.example.managerapp.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.managerapp.ApiService.ApiService;
import com.example.managerapp.Model.ApiResponseResult;
import com.example.managerapp.Model.ListUserModel;
import com.example.managerapp.Model.TripModel;
import com.example.managerapp.Model.UserModel;
import com.example.managerapp.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.viewHover> {
    Context context;
    List<ListUserModel> userModels;
    LayoutInflater  layoutInflater;
    FragmentManager fragmentManager;

    public UserAdapter(Context context, List<ListUserModel> userModels, FragmentManager fragmentManager) {
        this.context = context;
        this.userModels = userModels;
        this.layoutInflater = LayoutInflater.from(context);
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public viewHover onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View recyclerViewItem = layoutInflater.inflate(R.layout.list_user, parent, false);
        return new UserAdapter.viewHover(recyclerViewItem);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHover holder, @SuppressLint("RecyclerView") int position) {

        ListUserModel userModel = this.userModels.get(position);

        holder.usernameView.setText(userModel.getUsername());
        holder.emailView.setText(userModel.getEmail());
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        Log.d("userModel", userModel.get_id());

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogBuilder.setTitle("Confirm delete user");
                alertDialogBuilder.setMessage("Are you sure delete user!!!");
                alertDialogBuilder.setIcon(R.drawable.ic_baseline_info_24);
                alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        ApiService.apiService.DeleteUser(userModel.get_id()).enqueue(new Callback<ApiResponseResult<Object>>() {
                            @Override
                            public void onResponse(Call<ApiResponseResult<Object>> call, Response<ApiResponseResult<Object>> response) {
                                if(response.code() == 200){
                                    userModels.remove(position);
                                    notifyItemRemoved(position);
                                    notifyDataSetChanged();
                                    Toast.makeText(context, response.body().message, Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(context, "Delete user fail", Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<ApiResponseResult<Object>> call, Throwable t) {
                                Toast.makeText(context, "Error sever", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                alertDialogBuilder.show();
            }
        });

        holder.btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogBuilder.setTitle("Confirm reset password");
                alertDialogBuilder.setMessage("Are you sure reset password !!!");
                alertDialogBuilder.setIcon(R.drawable.ic_baseline_info_24);
                alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        ApiService.apiService.ResetPassword(userModel.get_id()).enqueue(new Callback<ApiResponseResult<Object>>() {
                            @Override
                            public void onResponse(Call<ApiResponseResult<Object>> call, Response<ApiResponseResult<Object>> response) {
                                if(response.code() == 200){
                                    Toast.makeText(context, response.body().message, Toast.LENGTH_LONG).show();
                                }
                                else if(response.code() == 201){
                                    Toast.makeText(context, response.body().message, Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(context, "Error sever", Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<ApiResponseResult<Object>> call, Throwable t) {
                                Toast.makeText(context, "Error sever", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                alertDialogBuilder.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return userModels.size();
    }

    public static class viewHover extends RecyclerView.ViewHolder {
        TextView usernameView;
        TextView emailView;
        Button btnReset;
        Button btnDelete;
        viewHover(View view){
            super(view);
            this.usernameView = (TextView) view.findViewById(R.id.value_username_view_user);
            this.emailView = (TextView) view.findViewById(R.id.value_email_view_user);
            this.btnReset = (Button) view.findViewById(R.id.btn_reset_user);
            this.btnDelete = (Button) view.findViewById(R.id.btn_delete_user);
        }
    }
}
