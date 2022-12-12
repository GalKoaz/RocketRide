package com.example.rocketride;

import com.google.android.gms.maps.model.LatLng;

import java.util.Comparator;

public class DriverRideModel {
    private final String firstName;
    private final String lastName;
    private final String source;
    private final String destination;
    private final String startTime;
    private final String rating;
    private LatLng srcPoint, dstPoint, pickupPoint;
    private double sortWeight;
    public long start_in_minutes = 0;
    public double price;
    public Double rating_numerical;
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

    public void setSort_weight(double sort_weight) {
        this.sortWeight = sort_weight;
    }
}

class sort_by_best_fit implements Comparator<DriverRideModel>{
    public int compare(DriverRideModel a, DriverRideModel b){
        return Double.compare(a.getSort_weight(), b.getSort_weight());
    }
}
class sort_by_best_time implements Comparator<DriverRideModel>{
    public int compare(DriverRideModel a, DriverRideModel b){
        return Long.compare(a.start_in_minutes, b.start_in_minutes);
    }
}
class sort_by_best_rating implements Comparator<DriverRideModel>{
    public int compare(DriverRideModel a, DriverRideModel b){
        return Double.compare(a.rating_numerical, b.rating_numerical);
    }
}
class sort_by_best_price implements Comparator<DriverRideModel>{
    public int compare(DriverRideModel a, DriverRideModel b){
        return Double.compare(a.price, b.price);
    }
}
