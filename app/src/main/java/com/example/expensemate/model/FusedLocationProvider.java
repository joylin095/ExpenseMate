package com.example.expensemate.model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

public class FusedLocationProvider implements LocationProvider {
    private final FusedLocationProviderClient fusedClient;

    public FusedLocationProvider(Context context) {
        this.fusedClient = LocationServices.getFusedLocationProviderClient(context);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void getCurrentLocation(LocationCallback callback) {
        fusedClient.getLastLocation().addOnSuccessListener(location -> {
            if (location != null) {
                callback.onLocationResult(location.getLatitude(), location.getLongitude());
            }
        });
    }
}
