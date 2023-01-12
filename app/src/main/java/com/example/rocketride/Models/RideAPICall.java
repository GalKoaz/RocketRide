package com.example.rocketride.Models;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RideAPICall {
    @GET("api/v1/rides/{ride_id}")
    Call<ResponseRideModel> getRide(@Path("ride_id") String rideID);

    @GET("api/v1/rides/alive")
    Call<List<ResponseRideModel>> getAliveRides();

    @GET("api/v1/rides/alive")
    Call<List<ResponseRideModel>> getAliveRidesInDate(@Query("day") String day, @Query("month") String month, @Query("year") String year);

    @POST("api/v1/rides")
    Call<HashMap<String, Object>> addRide(@Body RideModel rideModel);

    @PUT("api/v1/rides/{ride_id}")
    Call<HashMap<String, Object>> updateRideFields(@Path("ride_id") String rideID, @Body HashMap<String, Object> fields);
}
