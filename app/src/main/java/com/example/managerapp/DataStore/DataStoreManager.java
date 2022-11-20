package com.example.managerapp.DataStore;



import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.datastore.preferences.core.MutablePreferences;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.core.PreferencesKeys;
import androidx.datastore.preferences.rxjava3.RxPreferenceDataStoreBuilder;
import androidx.datastore.rxjava3.RxDataStore;


import com.example.managerapp.Model.DataStoreModel;
import com.example.managerapp.Model.UserModel;

import org.reactivestreams.Subscriber;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public class DataStoreManager {

    Context context;
    Preferences.Key<String> TOKEN_KEY = PreferencesKeys.stringKey("token");
    Preferences.Key<String> EMAIL_KEY = PreferencesKeys.stringKey("email");
    Preferences.Key<Boolean> IS_FIRST_KEY = PreferencesKeys.booleanKey("isFirst");
    Preferences.Key<String> USERNAME_KEY = PreferencesKeys.stringKey("username");
    Preferences.Key<String> TYPE_KEY = PreferencesKeys.stringKey("type");
    Preferences.Key<String> TRIP_ID_KEY = PreferencesKeys.stringKey("tripId");
    RxDataStore<Preferences> dataStore;

    public DataStoreManager(Context context, RxDataStore<Preferences> dataStore){
        this.context = context;
        this.dataStore = dataStore;
    }

    public void save(DataStoreModel dataStoreModel) {
       dataStore.updateDataAsync(prefsIn -> {
           MutablePreferences mutablePreferences = prefsIn.toMutablePreferences();
           mutablePreferences.set(TOKEN_KEY, dataStoreModel.token);
           mutablePreferences.set(IS_FIRST_KEY, dataStoreModel.user.isFirst);
           mutablePreferences.set(EMAIL_KEY, dataStoreModel.user.email);
           mutablePreferences.set(USERNAME_KEY, dataStoreModel.user.username);
           mutablePreferences.set(TYPE_KEY, dataStoreModel.user.type);
           return Single.just(mutablePreferences);
        });
    }

    public void saveTrip(String tripId) {
        dataStore.updateDataAsync(prefsIn -> {
            MutablePreferences mutablePreferences = prefsIn.toMutablePreferences();
            mutablePreferences.set(TRIP_ID_KEY, tripId);
            return Single.just(mutablePreferences);
        });
    }

    public Flowable<String> getDataStoreTrip(){
        Flowable<String> exampleCounterFlow =
                dataStore.data()
                        .map(prefs ->
                                prefs.get(TRIP_ID_KEY)
                        );
        return exampleCounterFlow;
    }

    public Flowable<DataStoreModel> getDataStore(){
        Flowable<DataStoreModel> exampleCounterFlow =
                dataStore.data()
                        .map(prefs ->
                                new DataStoreModel(
                                        prefs.get(TOKEN_KEY),
                                        new UserModel(
                                                prefs.get(TYPE_KEY),
                                                prefs.get(USERNAME_KEY),
                                                prefs.get(EMAIL_KEY),
                                                prefs.get(IS_FIRST_KEY)
                                                )
                                )
                        );
        return exampleCounterFlow;
    }
}
