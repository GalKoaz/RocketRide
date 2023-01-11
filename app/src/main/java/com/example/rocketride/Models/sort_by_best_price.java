package com.example.rocketride.Models;

import java.util.Comparator;

public class sort_by_best_price implements Comparator<DriverRideModel> {
    public int compare(DriverRideModel a, DriverRideModel b) {
        return Double.compare(a.price, b.price);
    }
}
