package com.example.rocketride.Models;

import static android.content.ContentValues.TAG;

import android.os.Build;
import android.util.Log;

import java.util.function.Consumer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RateModelFirebaseHandler {
    private String BASE_URL = "http://10.0.2.2:5000/";

    // Retrofit builder
    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    // Rate api call object
    private RateAPICall myAPICall = retrofit.create(RateAPICall.class);

    public void addRateModel(RateModel rateModel, Consumer<Response<RateModel>> onGoodResponse,
                             Consumer<Response<RateModel>> onBadResponse, Consumer<Throwable> onFailure) {
        // Add a new document with a generated ID
        Call<RateModel> call = myAPICall.addDriverRate(rateModel);
        call.enqueue(new Callback<RateModel>() {
            @Override
            public void onResponse(Call<RateModel> call, Response<RateModel> response) {
                Log.d(TAG, "response code " + response.code());
                if (response.code() != 201){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        onBadResponse.accept(response);
                    }
                    return;
                }
                // Good response
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    onGoodResponse.accept(response);
                }
            }

            @Override
            public void onFailure(Call<RateModel> call, Throwable t) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    onFailure.accept(t);
                }
            }
        });
    }

    public void updateRateModel(RateModel rateModel, Consumer<Response<RateModel>> onGoodResponse,
                               Consumer<Response<RateModel>> onBadResponse, Consumer<Throwable> onFailure) {
        // Update an existing document or non-existing document - then create it automatically.
        Call<RateModel> call = myAPICall.updateDriverRate(rateModel);
        call.enqueue(new Callback<RateModel>() {
            @Override
            public void onResponse(Call<RateModel> call, Response<RateModel> response) {
                Log.d(TAG, "response code " + response.code());
                if (response.code() != 200){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        onBadResponse.accept(response);
                    }
                    return;
                }
                // Good response
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    onGoodResponse.accept(response);
                }
            }

            @Override
            public void onFailure(Call<RateModel> call, Throwable t) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    onFailure.accept(t);
                }
            }
        });
    }

    public void getRateModel(String driverID, Consumer<Response<RateModel>> onGoodResponse,
                             Consumer<Response<RateModel>> onBadResponse, Consumer<Throwable> onFailure) {
        Call<RateModel> call = myAPICall.getDriverRate(driverID);
        call.enqueue(new Callback<RateModel>() {
            @Override
            public void onResponse(Call<RateModel> call, Response<RateModel> response) {
                Log.d(TAG, "response code " + response.code());
                if (response.code() != 200){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        onBadResponse.accept(response);
                    }
                    return;
                }
                // Good response
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    onGoodResponse.accept(response);
                }
            }

            @Override
            public void onFailure(Call<RateModel> call, Throwable t) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    onFailure.accept(t);
                }
            }
        });
    }
}
