package com.example.rocketride.Models;

import java.util.HashMap;

public class RateModel {
    private final String driverID;
    private double avg;
    private int voters_num;

    public RateModel(String driverID, double avg, int voters_num) {
        this.driverID = driverID;
        this.avg = avg;
        this.voters_num = voters_num;

    }

    public int getVoters_num() {
        return voters_num;
    }

    public void setVoters_num(int voters_num) {
        this.voters_num = voters_num;
    }

    public double getAvg() {
        return avg;
    }

    public void setAvg(double avg) {
        this.avg = avg;
    }

    public String getDriverID() {
        return driverID;
    }

    void addVote(double newVote) {
        avg = (avg * voters_num + newVote) / (voters_num + 1);
        voters_num++;
    }

    public HashMap<String, String> getHashRateDetails(){
        HashMap<String, String> RateDetails = new HashMap<>();
        RateDetails.put("driver-id", this.driverID);
        RateDetails.put("avg", String.valueOf(this.avg));
        RateDetails.put("voters_num", String.valueOf(this.voters_num));
        return RateDetails;
    }

    @Override
    public String toString() {
        return "RateModel{" +
                "driverID='" + driverID + '\'' +
                ", avg='" + avg + '\'' +
                ", voters_num='" + voters_num + '\'' +
                '}';
    }
}
