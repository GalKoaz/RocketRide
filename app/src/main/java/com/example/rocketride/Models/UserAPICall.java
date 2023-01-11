package com.example.rocketride.Models;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserAPICall {
    @GET("api/v1/users/{parameter}")
    Call<UserModel> getUserDetails(@Path("parameter") String UID);

    @POST("api/v1/users")
    Call<HashMap<String, Object>> addUser(@Body UserModel userModel);

    @POST("api/v1/users")
    Call<HashMap<String, Object>> addDriver(@Body DriverModel driverModel);

    @PUT("api/v1/users/{UID}")
    Call<HashMap<String, Object>> updateUserFields(@Path("UID") String UID, @Body HashMap<String, Object> fields);

}
