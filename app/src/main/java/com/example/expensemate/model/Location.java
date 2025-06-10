package com.example.expensemate.model;

import android.util.Log;

public class Location {
    private double lastLatitude = 0.0;
    private double lastLongitude = 0.0;
    private boolean hasNotified = false;
    private static final double DISTANCE_THRESHOLD = 0.1; // (kilometers)

    public boolean analyzeLocation(double latitude, double longitude) {
        double distance = calculateDistance(lastLatitude, lastLongitude, latitude, longitude);
        boolean isSamePlace = distance < DISTANCE_THRESHOLD;

        if (isSamePlace && !hasNotified) {
            hasNotified = true;
            return true;
        } else if (!isSamePlace) {
            lastLatitude = latitude;
            lastLongitude = longitude;
            hasNotified = false;
        }
        return false;
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Earth's radius in kilometers
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
}
