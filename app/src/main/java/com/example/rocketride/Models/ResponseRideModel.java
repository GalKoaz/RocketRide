package com.example.rocketride.Models;

import java.util.HashMap;


/**
 * A POGO class for the response from the server.
 */
public class ResponseRideModel {
    private String center_bottom_seat, left_bottom_seat, right_bottom_seat, near_driver_seat;
    private double price;
    private double dst_lon, pickup_lon, src_lon;
    private double dst_lat, src_lat, pickup_lat;
    private int date_m, date_d, date_y, time_h, time_m;
    private boolean alive, canceled;
    private String driver_id, _id, dst_name, pickup_name, src_name, details;

    public ResponseRideModel(String center_bottom_seat, String left_bottom_seat, String right_bottom_seat, String near_driver_seat,
                             double price, double dst_lon, double pickup_lon, double src_lon,
                             double dst_lat, double src_lat, double pickup_lat,
                             int date_m, int date_d, int date_y, int time_h, int time_m,
                             boolean alive, boolean canceled,
                             String driver_id, String _id, String dst_name, String pickup_name, String src_name, String details) {
        this.center_bottom_seat = center_bottom_seat;
        this.left_bottom_seat = left_bottom_seat;
        this.right_bottom_seat = right_bottom_seat;
        this.near_driver_seat = near_driver_seat;
        this.price = price;
        this.dst_lon = dst_lon;
        this.pickup_lon = pickup_lon;
        this.src_lon = src_lon;
        this.dst_lat = dst_lat;
        this.src_lat = src_lat;
        this.pickup_lat = pickup_lat;
        this.date_m = date_m;
        this.date_d = date_d;
        this.date_y = date_y;
        this.time_h = time_h;
        this.time_m = time_m;
        this.alive = alive;
        this.canceled = canceled;
        this.driver_id = driver_id;
        this._id = _id;
        this.dst_name = dst_name;
        this.pickup_name = pickup_name;
        this.src_name = src_name;
        this.details = details;
    }

    /* getters and setters */
    public String getCenter_bottom_seat() {
        return center_bottom_seat;
    }

    public void setCenter_bottom_seat(String center_bottom_seat) {
        this.center_bottom_seat = center_bottom_seat;
    }

    public String getLeft_bottom_seat() {
        return left_bottom_seat;
    }

    public void setLeft_bottom_seat(String left_bottom_seat) {
        this.left_bottom_seat = left_bottom_seat;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getDst_lon() {
        return dst_lon;
    }

    public void setDst_lon(double dst_lon) {
        this.dst_lon = dst_lon;
    }

    public String getDriver_id() {
        return driver_id;
    }

    public void setDriver_id(String driver_id) {
        this.driver_id = driver_id;
    }

    public double getPickup_lon() {
        return pickup_lon;
    }

    public void setPickup_lon(double pickup_lon) {
        this.pickup_lon = pickup_lon;
    }

    public double getDst_lat() {
        return dst_lat;
    }

    public void setDst_lat(double dst_lat) {
        this.dst_lat = dst_lat;
    }

    public double getSrc_lat() {
        return src_lat;
    }

    public void setSrc_lat(double src_lat) {
        this.src_lat = src_lat;
    }

    public String getRight_bottom_seat() {
        return right_bottom_seat;
    }

    public void setRight_bottom_seat(String right_bottom_seat) {
        this.right_bottom_seat = right_bottom_seat;
    }

    public int getDate_m() {
        return date_m;
    }

    public void setDate_m(int date_m) {
        this.date_m = date_m;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public int getDate_d() {
        return date_d;
    }

    public void setDate_d(int date_d) {
        this.date_d = date_d;
    }

    public int getDate_y() {
        return date_y;
    }

    public void setDate_y(int date_y) {
        this.date_y = date_y;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getDst_name() {
        return dst_name;
    }

    public void setDst_name(String dst_name) {
        this.dst_name = dst_name;
    }

    public String getPickup_name() {
        return pickup_name;
    }

    public void setPickup_name(String pickup_name) {
        this.pickup_name = pickup_name;
    }

    public int getTime_h() {
        return time_h;
    }

    public void setTime_h(int time_h) {
        this.time_h = time_h;
    }

    public double getSrc_lon() {
        return src_lon;
    }

    public void setSrc_lon(double src_lon) {
        this.src_lon = src_lon;
    }

    public String getNear_driver_seat() {
        return near_driver_seat;
    }

    public void setNear_driver_seat(String near_driver_seat) {
        this.near_driver_seat = near_driver_seat;
    }

    public String getSrc_name() {
        return src_name;
    }

    public void setSrc_name(String src_name) {
        this.src_name = src_name;
    }

    public double getPickup_lat() {
        return pickup_lat;
    }

    public void setPickup_lat(double pickup_lat) {
        this.pickup_lat = pickup_lat;
    }

    public int getTime_m() {
        return time_m;
    }

    public void setTime_m(int time_m) {
        this.time_m = time_m;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public boolean isCanceled() {
        return canceled;
    }

    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }

    public HashMap<String, Object> getRideHashMap() {
        HashMap<String, Object> ride = new HashMap<>();
        ride.put("center_bottom_seat", center_bottom_seat);
        ride.put("left_bottom_seat", left_bottom_seat);
        ride.put("price", price);
        ride.put("dst_lon", dst_lon);
        ride.put("driver_id", driver_id);
        ride.put("pickup_lon", pickup_lon);
        ride.put("dst_lat", dst_lat);
        ride.put("src_lat", src_lat);
        ride.put("right_bottom_seat", right_bottom_seat);
        ride.put("date_m", date_m);
        ride.put("alive", alive);
        ride.put("date_d", date_d);
        ride.put("date_y", date_y);
        ride.put("_id", _id);
        ride.put("dst_name", dst_name);
        ride.put("pickup_name", pickup_name);
        ride.put("time_h", time_h);
        ride.put("src_lon", src_lon);
        ride.put("near_driver_seat", near_driver_seat);
        ride.put("src_name", src_name);
        ride.put("pickup_lat", pickup_lat);
        ride.put("time_m", time_m);
        ride.put("details", details);
        ride.put("canceled", canceled);
        return ride;
    }

    public void setRideHashMap(HashMap<String, Object> ride) {
        this.center_bottom_seat = (String) ride.get("center_bottom_seat");
        this.left_bottom_seat = (String) ride.get("left_bottom_seat");
        this.price = (double) ride.get("price");
        this.dst_lon = (double) ride.get("dst_lon");
        this.driver_id = (String) ride.get("driver_id");
        this.pickup_lon = (double) ride.get("pickup_lon");
        this.dst_lat = (double) ride.get("dst_lat");
        this.src_lat = (double) ride.get("src_lat");
        this.right_bottom_seat = (String) ride.get("right_bottom_seat");
        this.date_m = (int) ride.get("date_m");
        this.alive = (boolean) ride.get("alive");
        this.date_d = (int) ride.get("date_d");
        this.date_y = (int) ride.get("date_y");
        this._id = (String) ride.get("_id");
        this.dst_name = (String) ride.get("dst_name");
        this.pickup_name = (String) ride.get("pickup_name");
        this.time_h = (int) ride.get("time_h");
        this.src_lon = (double) ride.get("src_lon");
        this.near_driver_seat = (String) ride.get("near_driver_seat");
        this.src_name = (String) ride.get("src_name");
        this.pickup_lat = (double) ride.get("pickup_lat");
        this.time_m = (int) ride.get("time_m");
        this.details = (String) ride.get("details");
        this.canceled = (boolean) ride.get("canceled");
    }

    @Override
    public String toString() {
        return "ResponseRideModel{" +
                "center_bottom_seat='" + center_bottom_seat + '\'' +
                ", left_bottom_seat='" + left_bottom_seat + '\'' +
                ", right_bottom_seat='" + right_bottom_seat + '\'' +
                ", near_driver_seat='" + near_driver_seat + '\'' +
                ", price=" + price +
                ", dst_lon=" + dst_lon +
                ", pickup_lon=" + pickup_lon +
                ", src_lon=" + src_lon +
                ", dst_lat=" + dst_lat +
                ", src_lat=" + src_lat +
                ", pickup_lat=" + pickup_lat +
                ", date_m=" + date_m +
                ", date_d=" + date_d +
                ", date_y=" + date_y +
                ", time_h=" + time_h +
                ", time_m=" + time_m +
                ", alive=" + alive +
                ", canceled=" + canceled +
                ", driver_id='" + driver_id + '\'' +
                ", _id='" + _id + '\'' +
                ", dst_name='" + dst_name + '\'' +
                ", pickup_name='" + pickup_name + '\'' +
                ", src_name='" + src_name + '\'' +
                ", details='" + details + '\'' +
                '}';
    }
}
