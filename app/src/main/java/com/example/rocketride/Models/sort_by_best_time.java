package com.example.rocketride.Models;

import java.util.Comparator;

public class sort_by_best_time implements Comparator<DriverRideModel> {
    public int compare(DriverRideModel a, DriverRideModel b) {
        return Long.compare(a.start_in_minutes, b.start_in_minutes);
    }
}
