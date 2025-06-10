package com.example.expensemate.model;

public interface LocationProvider {
    interface LocationCallback {
        void onLocationResult(double latitude, double longitude);
    }

    void getCurrentLocation(LocationCallback callback);
}
