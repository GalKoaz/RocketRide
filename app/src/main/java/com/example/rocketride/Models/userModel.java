package com.example.rocketride.Models;

import java.util.HashMap;

public class userModel {

    protected String UID, type, firstName, lastName, phoneNumber, profileImageLink, email;

    public userModel(String UID, String type, String firstName, String lastName, String phoneNumber, String profileImageLink, String email) {
        this.UID = UID;
        this.type = type;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.profileImageLink = profileImageLink;
        this.email = email;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getProfileImageLink() {
        return profileImageLink;
    }

    public void setProfileImageLink(String profileImageLink) {
        this.profileImageLink = profileImageLink;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public HashMap<String, Object> getUserHashMap() {
        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("UID", UID);
        userMap.put("type", type);
        userMap.put("first_name", firstName);
        userMap.put("last_name", lastName);
        userMap.put("phone_number", phoneNumber);
        userMap.put("profile_image_link", profileImageLink);
        userMap.put("email", email);
        return userMap;
    }

    public void setUserHashMap(HashMap<String, Object> userMap) {
        this.UID = (String) userMap.get("UID");
        this.type = (String) userMap.get("type");
        this.firstName = (String) userMap.get("first_name");
        this.lastName = (String) userMap.get("last_name");
        this.phoneNumber = (String) userMap.get("phone_number");
        this.profileImageLink = (String) userMap.get("profile_image_link");
        this.email = (String) userMap.get("email");
    }

    @Override
    public String toString() {
        return "userModel{" +
                "UID='" + UID + '\'' +
                ", type='" + type + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", profileImageLink='" + profileImageLink + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
