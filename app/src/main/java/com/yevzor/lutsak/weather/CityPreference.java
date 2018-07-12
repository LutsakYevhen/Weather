package com.yevzor.lutsak.weather;

import android.app.Activity;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.util.Log;

public class CityPreference {

    private static final String TAG = CityPreference.class.getSimpleName();

    private SharedPreferences prefs;

    CityPreference(@Nullable Activity activity){
        Log.d(TAG, "Preference constructor");
        if (activity != null) {
            prefs = activity.getPreferences(Activity.MODE_PRIVATE);
        }
    }

    // If the user has not chosen a city yet, return
    // Lviv as the default city
    public String getCity(){
        Log.d(TAG, "getCity");
        return prefs.getString("city", "Lviv, UA");
    }

    void setCity(String city){
        Log.d(TAG, "setCity");
        prefs.edit().putString("city", city).apply();
    }
}