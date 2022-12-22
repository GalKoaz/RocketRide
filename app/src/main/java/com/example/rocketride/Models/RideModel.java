package com.example.rocketride.Models;

import java.io.Serializable;
import java.util.HashMap;

import kotlinx.android.parcel.Parcelize;

public class RideModel implements Serializable {
    private final String source;
    private final String destination;
    private final String date;
    private final String pickup;
    public double price;
    private String nearDriverSeat, leftBottomSeat, centerBottomSeat, rightBottomSeat;
    private final String driverID, rideID;


    // Constructor
    public RideModel(String source, String destination, String date, String pickup, String driverID, String rideID) {
        this.source = source;
        this.destination = destination;
        this.date = date;
        this.pickup = pickup;
        this.driverID = driverID;
        this.rideID = rideID;
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

    public double getPrice() {return price;}

    public String getDriverID() {return driverID;}

    public String getRideID() {return rideID;}

    // hashmap
    public HashMap<String, String> getHashRideDetails(){
        HashMap<String, String> RideDetails = new HashMap<>();
        RideDetails.put("source", this.source);
        RideDetails.put("destination", this.destination);
        RideDetails.put("date", this.date);
        RideDetails.put("pickup", this.pickup);
        return RideDetails;
    }

    public void setCarSeats(String nearDriverSeat, String leftBottomSeat, String centerBottomSeat, String rightBottomSeat){
        this.nearDriverSeat = nearDriverSeat;
        this.leftBottomSeat = leftBottomSeat;
        this.centerBottomSeat = centerBottomSeat;
        this.rightBottomSeat = rightBottomSeat;
    }

    public String getNearDriverSeat() {return nearDriverSeat;}

    public String getLeftBottomSeat() {return leftBottomSeat;}

    public String getCenterBottomSeat() {return centerBottomSeat;}

    public String getRightBottomSeat() {return rightBottomSeat;}

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
