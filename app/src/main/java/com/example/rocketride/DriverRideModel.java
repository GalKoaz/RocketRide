package com.example.rocketride;

public class DriverRideModel {
    String firstName, lastName, source, destination, startTime, rating;

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
}