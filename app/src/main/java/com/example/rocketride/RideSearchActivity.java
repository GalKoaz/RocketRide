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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.core.FirestoreClient;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;


public class RideSearchActivity extends AppCompatActivity {

    private String selectedSourcePlace, selectedDestPlace;
    private LatLng selectedSourcePlacePoint, selectedDestPlacePoint;
    private ArrayList<DriverRideModel> closeRides = new ArrayList<>();
    private FirebaseFirestore db;
    private  RecyclerView recyclerView;
    private DriverRideRecyclerViewAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_search);
        db = FirebaseFirestore.getInstance();

        System.out.println("the lang distance is:" +CalculationByDistance(new LatLng(32.177033, 34.852330),new LatLng(32.178005, 34.923150)));

        // Hide action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        recyclerView = findViewById(R.id.myRecyclerView);

        // Set the adapter
        adapter = new DriverRideRecyclerViewAdapter(this, closeRides);
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
            setUpCloseRides();
        });
    }

    protected void setUpCloseRides(){
        // TODO: here extract closest rides to current user.
        //       build all related objects afterwards and push them to
        //       the associated array list called - "closeRides".
        getAliveRides();
    }

    /**
     * Method sends a firestore query that extracts the current alive rides.
     * @return
     */
    synchronized protected void getAliveRides(){
        // Create a reference to the rides collection
        CollectionReference rides = db.collection("drives");
        Calendar calendar = Calendar.getInstance();

        int YEAR = calendar.get(Calendar.YEAR);
        int MONTH = calendar.get(Calendar.MONTH);
        int DATE = calendar.get(Calendar.DATE);
        int HOUR = calendar.get(Calendar.HOUR);
        int MINUTE = calendar.get(Calendar.MINUTE);
        Query query = rides.whereEqualTo("alive", true).whereEqualTo("date-d",DATE).whereEqualTo("date-m",MONTH).whereEqualTo("date-y",YEAR);
        // Store query result
        query.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        ArrayList<DriverRideModel> result = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            HashMap<String, Object> driverDetails = getDriverDetails((String) document.get("driver-id"));
                            Long h = (Long)document.get("time_h");
                            Long m = (Long)document.get("time_m");
                            Long d = (Long)document.get("date-d");
                            h -= HOUR;
                            m -= MINUTE;
                            if(m < 0){
                                h -= 1;
                                m = 60 + m;
                            }
                            h += d*24;
                            String t = h+":"+m;


                            result.add(
                                    new DriverRideModel(
                                            (String) driverDetails.get("first_name"),
                                            (String) driverDetails.get("last_name"),
                                            (String) document.get("src_name"),
                                            (String) document.get("dst_name"),
                                            t,
                                            "7.5"
                                    )
                            );
                            Log.d(TAG, document.getId() + " => " + document.getData());

                            Log.d(TAG, document.getId() + " => " + document.getData());

                        }
                        adapter = new DriverRideRecyclerViewAdapter(this, result);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(this));
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });
    }

    protected HashMap<String, Object> getDriverDetails(String UID){
        HashMap<String, Object> userDetails = new HashMap<>();

        // Create a reference to the rides collection
        CollectionReference rides = db.collection("users");

        Query query = rides.whereEqualTo("UID", UID);

        // Store query result
        query.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            userDetails.put("first_name", document.get("first_name"));
                            userDetails.put("last_name", document.get("last_name"));
                            userDetails.put("profile_image_link", document.get("profile_image_link"));
                            Log.d(TAG, document.getId() + " => " + document.getData());
                        }
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });

        return userDetails;
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