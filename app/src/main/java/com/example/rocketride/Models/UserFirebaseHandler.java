package com.example.rocketride.Models;

import static android.content.ContentValues.TAG;

import android.os.Build;
import android.util.Log;

import java.util.HashMap;
import java.util.function.Consumer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserFirebaseHandler {
    private String BASE_URL = "http://10.0.2.2:5000/";

    // Retrofit builder
    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    // Rate api call object
    private UserAPICall myAPICall = retrofit.create(UserAPICall.class);

    public void getUserModel(String UID, Consumer<Response<UserModel>> onGoodResponse,
                             Consumer<Response<UserModel>> onBadResponse, Consumer<Throwable> onFailure) {
        Call<UserModel> call = myAPICall.getUserDetails(UID);
        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
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
            public void onFailure(Call<UserModel> call, Throwable t) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    onFailure.accept(t);
                }
            }
        });
    }

    public void addUserModel(UserModel userModel, Consumer<Response<HashMap<String, Object>>> onGoodResponse,
                             Consumer<Response<HashMap<String, Object>>> onBadResponse, Consumer<Throwable> onFailure) {
        System.out.println("inner addUSERFUN");

        // Add a new document with a generated ID
        Call<HashMap<String, Object>> call = myAPICall.addUser(userModel);
        call.enqueue(new Callback<HashMap<String, Object>>() {
            @Override
            public void onResponse(Call<HashMap<String, Object>> call, Response<HashMap<String, Object>> response) {
                Log.d(TAG, "response code " + response.code());
                if (response.code() != 201){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        onBadResponse.accept(response);
                        System.out.println("bad addUSERFUN");

                    }
                    return;
                }
                // Good response
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    onGoodResponse.accept(response);
                    System.out.println("good addUSERFUN");

                }
            }

            @Override
            public void onFailure(Call<HashMap<String, Object>> call, Throwable t) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    onFailure.accept(t);
                    System.out.println(t);

                }
            }
        });
    }

    public void addDriverModel(DriverModel driverModel, Consumer<Response<HashMap<String, Object>>> onGoodResponse,
                               Consumer<Response<HashMap<String, Object>>> onBadResponse, Consumer<Throwable> onFailure) {

        // Add a new document with a generated ID
        Call<HashMap<String, Object>> call = myAPICall.addDriver(driverModel);
        call.enqueue(new Callback<HashMap<String, Object>>() {
            @Override
            public void onResponse(Call<HashMap<String, Object>> call, Response<HashMap<String, Object>> response) {
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
            public void onFailure(Call<HashMap<String, Object>> call, Throwable t) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    onFailure.accept(t);
                }
            }
        });
    }

    public void updateUserModelFields(String UID, HashMap<String, Object> fields, Consumer<Response<HashMap<String, Object>>> onGoodResponse,
                                      Consumer<Response<HashMap<String, Object>>> onBadResponse, Consumer<Throwable> onFailure) {
        // Update an existing document or non-existing document - then create it automatically.
        Call<HashMap<String, Object>> call = myAPICall.updateUserFields(UID, fields);
        call.enqueue(new Callback<HashMap<String, Object>>() {
            @Override
            public void onResponse(Call<HashMap<String, Object>> call, Response<HashMap<String, Object>> response) {
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
            public void onFailure(Call<HashMap<String, Object>> call, Throwable t) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    onFailure.accept(t);
                }
            }
        });
    }

    public void addUserByType(UserModel userModel, String type,
                               Consumer<Response<HashMap<String, Object>>> onGoodResponse,
                               Consumer<Response<HashMap<String, Object>>> onBadResponse,
                               Consumer<Throwable> onFailure) {
        if (type.equals("driver")) {
            DriverModel driverModel = (DriverModel) userModel;
            addDriverModel(driverModel, onGoodResponse, onBadResponse, onFailure);
            return;
        }
        System.out.println("ssssssssss addUSERFUN");
        // User is a rider
        addUserModel(userModel, onGoodResponse, onBadResponse, onFailure);
    }

}
