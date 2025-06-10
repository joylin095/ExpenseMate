package com.example.expensemate.model;

import android.util.Log;

public class NotifyHandler {
    private LocationProvider locationProvider = null;
    private Location location = null;

    public interface NotifyCallback {
        void onResult(boolean shouldNotify);
    }
    public NotifyHandler(LocationProvider locationProvider, Location location) {
        this.locationProvider = locationProvider;
        this.location = location;
    }
    public void checkLocationAndNotify(NotifyCallback callback) {
        locationProvider.getCurrentLocation((latitude, longitude) -> {

            boolean isSamePlace = location.analyzeLocation(latitude, longitude);
            callback.onResult(isSamePlace);
        });
    }
}
