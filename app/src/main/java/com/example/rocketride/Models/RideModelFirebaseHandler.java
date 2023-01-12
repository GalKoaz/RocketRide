package com.example.rocketride.Models;

import static android.content.ContentValues.TAG;

import android.os.Build;
import android.util.Log;

import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RideModelFirebaseHandler {
    private String BASE_URL = "http://10.0.2.2:5000/";

    // Retrofit builder
    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    // Rate api call object
    private RideAPICall myAPICall = retrofit.create(RideAPICall.class);

    public void getRideModel(String rideID, Consumer<Response<ResponseRideModel>> onGoodResponse,
                             Consumer<Response<ResponseRideModel>> onBadResponse, Consumer<Throwable> onFailure) {
        Call<ResponseRideModel> call = myAPICall.getRide(rideID);
        call.enqueue(new GenericCallback<>(onGoodResponse, onBadResponse, onFailure));
    }

    public void getAliveRides(Consumer<Response<List<ResponseRideModel>>> onGoodResponse,
                              Consumer<Response<List<ResponseRideModel>>> onBadResponse, Consumer<Throwable> onFailure){
        Call<List<ResponseRideModel>> call = myAPICall.getAliveRides();
        call.enqueue(new GenericCallback<>(onGoodResponse, onBadResponse, onFailure));
    }

    public void getAliveRidesInDate(String day, String month, String year,
                                    Consumer<Response<List<ResponseRideModel>>> onGoodResponse,
                                    Consumer<Response<List<ResponseRideModel>>> onBadResponse, Consumer<Throwable> onFailure) {
        Call<List<ResponseRideModel>> call = myAPICall.getAliveRidesInDate(day, month, year);
        call.enqueue(new GenericCallback<>(onGoodResponse, onBadResponse, onFailure));
    }

    public void addRideModel(RideModel rideModel, Consumer<Response<HashMap<String, Object>>> onGoodResponse,
                             Consumer<Response<HashMap<String, Object>>> onBadResponse, Consumer<Throwable> onFailure){
        Call<HashMap<String, Object>> call = myAPICall.addRide(rideModel);
        call.enqueue(new GenericCallback<>(onGoodResponse, onBadResponse, onFailure));
    }

    public void updateRideFields(String rideID, HashMap<String, Object> fields,
                                 Consumer<Response<HashMap<String, Object>>> onGoodResponse,
                                 Consumer<Response<HashMap<String, Object>>> onBadResponse, Consumer<Throwable> onFailure){
        Call<HashMap<String, Object>> call = myAPICall.updateRideFields(rideID, fields);
        call.enqueue(new GenericCallback<>(onGoodResponse, onBadResponse, onFailure));
    }

    public static class GenericCallback<T> implements Callback<T> {
        private Consumer<Response<T>> onGoodResponse, onBadResponse;
        private Consumer<Throwable> onFailure;

        public GenericCallback(Consumer<Response<T>> onGoodResponse, Consumer<Response<T>> onBadResponse, Consumer<Throwable> onFailure) {
            this.onGoodResponse = onGoodResponse;
            this.onBadResponse = onBadResponse;
            this.onFailure = onFailure;
        }

        @Override
        public void onResponse(Call<T> call, Response<T> response) {
            Log.d(TAG, "response code " + response.code());
            if (response.isSuccessful()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    onGoodResponse.accept(response);
                }
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    onBadResponse.accept(response);
                }
            }
        }

        @Override
        public void onFailure(Call<T> call, Throwable t) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                onFailure.accept(t);
            }
        }
    }
}
