package com.example.rocketride.Models;

import java.util.Comparator;

public class sort_by_best_fit implements Comparator<DriverRideModel> {
    public int compare(DriverRideModel a, DriverRideModel b) {
        return Double.compare(a.getSort_weight(), b.getSort_weight());
    }
}
