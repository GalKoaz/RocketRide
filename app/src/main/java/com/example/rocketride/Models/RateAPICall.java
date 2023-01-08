package com.example.rocketride.Models;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface RateAPICall {

    @GET("api/v1/rate_driver/{parameter}")
    Call<RateModel> getDriverRate(@Path("parameter") String driverID);

    @POST("api/v1/rate_driver")
    Call<RateModel> addDriverRate();

    @PUT("api/v1/rate_driver")
    Call<RateModel> updateDriverRate();
}
