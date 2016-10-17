package com.example.android.warehouse;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/** Preference Manager */
public class WarehouseApplicationSettings {
    SharedPreferences mSharedPreferences;

    public WarehouseApplicationSettings(Context context) {
        // SharedPreferences settings = context.getSharedPreferences("Preferences Name", 0);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    /** Returns preferred storage type.
     * By default use Internal storage */
    public String getStoragePreference() {
        // by default use Internal storage
        return mSharedPreferences.getString("Storage", "INTERNAL");
    }

    /** Set preferred storage type */
    public void setSharedPreference(String storageType) {
        mSharedPreferences
                .edit()
                .putString("Storage", storageType)
                .apply();
        // we could use .commit(), which is sycnrhonous (.apply() is asynchronous)
    }

}
