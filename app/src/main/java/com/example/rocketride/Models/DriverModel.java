package com.example.rocketride.Models;

import java.util.HashMap;

public class DriverModel extends userModel{
    private String ID, driverLicenseImageLink, plateNumber;

    public DriverModel(String UID, String firstName, String lastName, String phoneNumber, String profileImageLink, String email, String ID, String driverLicenseImageLink, String plateNumber) {
        super(UID, "driver", firstName, lastName, phoneNumber, profileImageLink, email);
        this.ID = ID;
        this.driverLicenseImageLink = driverLicenseImageLink;
        this.plateNumber = plateNumber;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getDriverLicenseImageLink() {
        return driverLicenseImageLink;
    }

    public void setDriverLicenseImageLink(String driverLicenseImageLink) {
        this.driverLicenseImageLink = driverLicenseImageLink;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public HashMap<String, Object> getDriverHashMap() {
        HashMap<String, Object> userMap = getUserHashMap();
        userMap.put("ID", ID);
        userMap.put("driver_license_link", driverLicenseImageLink);
        userMap.put("plate_number", plateNumber);
        return userMap;
    }

    public void setDriverHashMap(HashMap<String, Object> driverMap) {
        setUserHashMap(driverMap);
        this.ID = (String) driverMap.get("ID");
        this.driverLicenseImageLink = (String) driverMap.get("driver_license_link");
        this.plateNumber = (String) driverMap.get("plate_number");

    }

    @Override
    public String toString() {
        return "DriverModel{" +
                "ID='" + ID + '\'' +
                ", driverLicenseImageLink='" + driverLicenseImageLink + '\'' +
                ", plateNumber='" + plateNumber + '\'' +
                ", UID='" + UID + '\'' +
                ", type='" + type + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", profileImageLink='" + profileImageLink + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
