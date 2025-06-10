package com.example.expensemate.model;

public class MockLocationProvider implements LocationProvider {
    private double firstLatitude = 25;
    private double firstLongitude = 120;
    private double secondLatitude = 30;
    private double secondLongitude = 130;
    private double currentLatitude = firstLatitude;
    private double currentLongitude = firstLongitude;
    public void getCurrentLocation(LocationCallback callback) {
        callback.onLocationResult(currentLatitude, currentLongitude);
    }

    public void changeToFirst() {
        currentLatitude = firstLatitude;
        currentLongitude = firstLongitude;
    }

    public void changeToSecond() {
        currentLatitude = secondLatitude;
        currentLongitude = firstLongitude;
    }
}
