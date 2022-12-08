package com.example.rocketride;

import com.google.type.LatLng;

public class DriverRideModel {
    private String firstName, lastName, source, destination, startTime, rating;
    private LatLng srcPoint, dstPoint, pickupPoint;
    private double sortWeight;

    public DriverRideModel(String firstName, String lastName, String source, String destination, String startTime, String rating) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.source = source;
        this.destination = destination;
        this.startTime = startTime;
        this.rating = rating;
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

    public void setSrcPoint(LatLng srcPoint) {
        this.srcPoint = srcPoint;
    }

    public void setDstPoint(LatLng dstPoint) {
        this.dstPoint = dstPoint;
    }

    public void setPickupPoint(LatLng pickupPoint) {
        this.pickupPoint = pickupPoint;
    }

    public void setSort_weight(int sort_weight) {
        this.sortWeight = sort_weight;
    }
}
