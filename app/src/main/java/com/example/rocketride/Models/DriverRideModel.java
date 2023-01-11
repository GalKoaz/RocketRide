package com.example.rocketride.Models;

import com.google.android.gms.maps.model.LatLng;

import java.util.Comparator;
import java.util.HashMap;

public class DriverRideModel {
    private final String firstName;
    private final String lastName;
    private final String source;
    private final String destination;
    private final String startTime;
    private final String rating;
    private final String pickupName;
    private final String date;
    private LatLng srcPoint, dstPoint, pickupPoint;
    private double sortWeight;
    public long start_in_minutes = 0;
    public double price;
    public Double rating_numerical;
    private String nearDriverSeat, leftBottomSeat, centerBottomSeat, rightBottomSeat;
    private final String driverID;
    private final String rideID;
    private String profileImageURL;

    // Constructor
    public DriverRideModel(String firstName, String lastName, String source, String destination,
                           String startTime, String rating, String pickupName, Double price, String date,
                           String driverID, String rideID) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.source = source;
        this.destination = destination;
        this.startTime = startTime;
        this.rating = rating;
        this.pickupName = pickupName;
        this.date = date;
        this.price = price;
        this.driverID = driverID;
        this.rideID = rideID;
        this.profileImageURL = "";
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getRating() {
        return rating;
    }


    /* User's profile image get & set */
    public String getProfileImageURL() {return profileImageURL;}

    public void setProfileImageURL(String profileImageURL) {this.profileImageURL = profileImageURL;}

    public HashMap<String, Object> getHashDriverDetails(){
        HashMap<String, Object> driverDetails = new HashMap<>();
        driverDetails.put("first_name", this.firstName);
        driverDetails.put("last_name", this.lastName);
        driverDetails.put("source", this.source);
        driverDetails.put("destination", this.destination);
        driverDetails.put("start_time", this.startTime);
        driverDetails.put("date", this.date);
        driverDetails.put("rating", this.rating);
        driverDetails.put("pickup_name", this.pickupName);
        driverDetails.put("price", this.price);
        driverDetails.put("near_driver_seat", this.nearDriverSeat);
        driverDetails.put("left_bottom_seat", this.leftBottomSeat);
        driverDetails.put("center_bottom_seat", this.centerBottomSeat);
        driverDetails.put("right_bottom_seat", this.rightBottomSeat);
        driverDetails.put("driver_id", driverID);
        driverDetails.put("ride_id", rideID);
        return driverDetails;
    }

    /****** Points getter & setters ******/

    public LatLng getSrcPoint() {
        return srcPoint;
    }

    public LatLng getDstPoint() {
        return dstPoint;
    }

    public LatLng getPickupPoint() {
        return pickupPoint;
    }

    public double getSort_weight() {
        return sortWeight;
    }

    /**
     * Set driver associated location points and a sort weight for these fields.
     * @param srcPoint driver's source point.
     * @param dstPoint driver's destination point.
     * @param pickupPoint driver's pickup point.
     * @param sortWeight sort weight for the fields.
     */
    public void setLocationPoints(LatLng srcPoint, LatLng dstPoint, LatLng pickupPoint, double sortWeight) {
        this.srcPoint = srcPoint;
        this.dstPoint = dstPoint;
        this.pickupPoint = pickupPoint;
        this.sortWeight = sortWeight;
    }

    public void setCarSeats(String nearDriverSeat, String leftBottomSeat, String centerBottomSeat, String rightBottomSeat){
        this.nearDriverSeat = nearDriverSeat;
        this.leftBottomSeat = leftBottomSeat;
        this.centerBottomSeat = centerBottomSeat;
        this.rightBottomSeat = rightBottomSeat;
    }

    public void setSrcPoint(LatLng srcPoint) {
        this.srcPoint = srcPoint;
    }

    public void setDstPoint(LatLng dstPoint) {
        this.dstPoint = dstPoint;
    }

    public void setPickupPoint(LatLng pickupPoint) {
        this.pickupPoint = pickupPoint;
    }

    public void setSort_weight(double sort_weight) {
        this.sortWeight = sort_weight;
    }
}

