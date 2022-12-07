package com.example.rocketride;

import static com.facebook.login.widget.ProfilePictureView.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;


public class RideSearchActivity extends AppCompatActivity {

    private String selectedSourcePlace, selectedDestPlace;
    private LatLng selectedSourcePlacePoint, selectedDestPlacePoint;
    private ArrayList<DriverRideModel> closeRides = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_search);

        System.out.println("the lang distance is:" +CalculationByDistance(new LatLng(32.177033, 34.852330),new LatLng(32.178005, 34.923150)));

        // Hide action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        RecyclerView recyclerView = findViewById(R.id.myRecyclerView);

        // set up the closest drivers around
        setUpCloseRides();

        // Set the adapter
        DriverRideRecyclerViewAdapter adapter = new DriverRideRecyclerViewAdapter(this, closeRides);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        // Initialize places
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), getString(R.string.api_key), Locale.US);
        }

        Button searchButton = findViewById(R.id.searchButton);
        //mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map_picker);

        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment2);

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                // TODO: Get info about the selected place.
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
                selectedSourcePlace = place.getName();
                selectedSourcePlacePoint = place.getLatLng();
            }

            @Override
            public void onError(@NonNull Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });

        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment2 = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        // Specify the types of place data to return.
        autocompleteFragment2.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment2.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                // TODO: Get info about the selected place.

                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
                Toast.makeText(RideSearchActivity.this, "Place: " + place.getName() + ", " + place.getId(),
                        Toast.LENGTH_SHORT).show();
                selectedDestPlace = place.getName();
                selectedDestPlacePoint = place.getLatLng();
            }

            @Override
            public void onError(@NonNull Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });

        // Set default string in auto complete fragments
        autocompleteFragment.setHint("Search for source...");
        autocompleteFragment2.setHint("Search for destination...");

        // Drivers search button action listener
        searchButton.setOnClickListener(l -> {
            Toast.makeText(RideSearchActivity.this, "search button clicked",
                    Toast.LENGTH_SHORT).show();

            Toast.makeText(RideSearchActivity.this, selectedSourcePlace + ", " + selectedDestPlace,
                    Toast.LENGTH_SHORT).show();

        });
    }

    protected void setUpCloseRides(){
        // TODO: here extract closest rides to current user.
        //      build all related objects afterwards and push them to
        ///     the associated array list called - "closeRides".
        closeRides.add(new DriverRideModel("Gal", "Koaz", "Meron", "King-Meat", "2 min", "10"));
        closeRides.add(new DriverRideModel("Amir", "Gillette", "Golan", "King-Meat", "10 min", "2.5"));
    }

    // function calculate the distance from one point to other in map.
    public double CalculationByDistance(LatLng StartP, LatLng EndP) {
        int Radius = 6371;// radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
        Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
                + " Meter   " + meterInDec);
        return Radius * c;
    }

}