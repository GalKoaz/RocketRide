package com.example.rocketride;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;

public class RideModel {
    private final String source;
    private final String destination;
    private final String date;
    private final String pickup;
    public double price;


    // Constructor
    public RideModel(String source, String destination, String date, String pickup) {
        this.source = source;
        this.destination = destination;
        this.date = date;
        this.pickup = pickup;
    }

    // getters
    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }

    public String getDate() {
        return date;
    }

    public String getPickup() {
        return pickup;
    }

    // hashmap
    public HashMap<String, String> getHashRideDetails(){
        HashMap<String, String> RideDetails = new HashMap<>();
        RideDetails.put("source", this.source);
        RideDetails.put("destination", this.destination);
        RideDetails.put("date", this.date);
        RideDetails.put("pickup", this.pickup);
        return RideDetails;
    }

    @Override
    public String toString() {
        return "RideModel{" +
                "source='" + source + '\'' +
                ", destination='" + destination + '\'' +
                ", date='" + date + '\'' +
                ", pickup='" + pickup + '\'' +
                ", price=" + price +
                '}';
    }
}
