package com.example.managerapp;

import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import com.example.managerapp.Model.DataStoreModel;
import com.example.managerapp.Model.UserModel;

public class SetMenuOption extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menuOption){
        MenuInflater menuInflater = getMenuInflater();
        if(MainActivity.GetStoreData().getUser().getType().equalsIgnoreCase("ADMIN")){
            menuInflater.inflate(R.menu.menu, menuOption);
        } else {
            menuInflater.inflate(R.menu.menu_staff, menuOption);
        }
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.log_out) {
            DataStoreModel dataStoreModel = new DataStoreModel("", new UserModel());
            MainActivity.SetStoreData(dataStoreModel);
            final Intent intents = new Intent(SetMenuOption.this, LoginActivity.class);
            intents.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            SetMenuOption.this.finish();
            startActivity(intents);
        }
        if (item.getItemId() == R.id.user_manager) {
            final Intent intents = new Intent(SetMenuOption.this, UserActivity.class);
            intents.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intents);
        }
        if (item.getItemId() == R.id.home_action) {
            final Intent intents = new Intent(SetMenuOption.this, TripActivity.class);
            intents.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intents);
        }

        return super.onOptionsItemSelected(item);
    }
}
