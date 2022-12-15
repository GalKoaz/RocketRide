package com.example.rocketride;

import static java.security.AccessController.getContext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Build;
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
import java.util.Map;


public class RideSearchActivity extends AppCompatActivity implements SelectDriverListener {
    private static final String TAG = RideSearchActivity.class.getName();
    private Button by_price, by_time, by_best, by_stars, GoBack;
    private String selectedSourcePlace="", selectedDestPlace="";
    private LatLng selectedSourcePlacePoint, selectedDestPlacePoint;
    private ArrayList<DriverRideModel> closeRides = new ArrayList<>();
    private FirebaseFirestore db;
    private  RecyclerView recyclerView;
    private DriverRideRecyclerViewAdapter adapter;
    private int sort_alg = 0;
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

        //sort buttons
        by_best=findViewById(R.id.best_sort);
        by_price=findViewById(R.id.price_sort);
        by_stars=findViewById(R.id.stars_sort);
        by_time=findViewById(R.id.time_sort);
        GoBack=findViewById(R.id.back_from_search);
        // Set the adapter
        adapter = new DriverRideRecyclerViewAdapter(this, closeRides, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        GoBack.setOnClickListener(view -> {
            this.finish();
            Intent switchActivityIntent = new Intent(this, MapsDriverActivity.class);
//            switchActivityIntent.putExtra("message", "From: " + MainActivity.class.getSimpleName());
            startActivity(switchActivityIntent);
        });
        by_best.setOnClickListener(view -> {
            sort_alg = 0;
            setUpCloseRides();
        });
        by_stars.setOnClickListener(view -> {
            sort_alg = 1;
            setUpCloseRides();
        });
        by_time.setOnClickListener(view -> {
            sort_alg = 2;
            setUpCloseRides();
        });
        by_price.setOnClickListener(view -> {
            sort_alg = 3;
            setUpCloseRides();
        });
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
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                // TODO: Get info about the selected place.
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
                selectedSourcePlace = place.getName();
                selectedSourcePlacePoint = place.getLatLng();
                System.out.println(selectedSourcePlacePoint);
            }

            @Override
            public void onError(@NonNull Status status) {
                // TODO: Handle the error.
                Log.d(TAG, "An error occurred: " + status);
            }
        });

        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment2 = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        // Specify the types of place data to return.
        autocompleteFragment2.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));

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
                System.out.println(selectedDestPlacePoint);
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
        if(selectedDestPlace.equals("") || selectedSourcePlace.equals("")){
            Toast.makeText(this, "input is empty.",
                    Toast.LENGTH_SHORT).show();
            // TODO: just for testing - remove it when not needed....
            for (int i = 0; i < 100; i++) {
                closeRides.add(new DriverRideModel("Amir", "Gill", "Ariel", "Tel-Aviv", "14:05", "3.5/5"));
                closeRides.add(new DriverRideModel("Gal", "KO", "Ariel", "Kfar-Saba", "16:18", "4.9/5"));
            }
            adapter = new DriverRideRecyclerViewAdapter(this, closeRides, this);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }
        else{
            getAliveRides();
        }
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
        int src_radius = 5;
        int dst_radius = 7;
        Query query = rides.whereEqualTo("alive", true).whereEqualTo("date-d",DATE).whereEqualTo("date-m",MONTH).whereEqualTo("date-y",YEAR);
        // Store query result
        query.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        ArrayList<DriverRideModel> result = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            LatLng src_p = new LatLng((double) document.get("src_lat"),(double) document.get("src_lon"));
                            LatLng dst_p = new LatLng((double) document.get("dst_lat"),(double) document.get("dst_lon"));
                            LatLng pickup_p = new LatLng((double) document.get("pickup_lat"),(double) document.get("pickup_lon"));
                            if ((CalculationByDistance(src_p,selectedSourcePlacePoint) > src_radius && CalculationByDistance(pickup_p,selectedSourcePlacePoint)>src_radius)
                                    || CalculationByDistance(dst_p,selectedDestPlacePoint) > dst_radius){
                                    continue;
                            }
                            else {
                                HashMap<String, Object> driverDetails = getDriverDetails((String) document.get("driver-id"));
                                Long h = (Long) document.get("time_h");
                                Long m = (Long) document.get("time_m");
                                Long d = (Long) document.get("date-d");
                                h -= HOUR;
                                m -= MINUTE;
                                if (m < 0) {
                                    h -= 1;
                                    m = 60 + m;
                                }
                                h += (d - DATE) * 24;
                                String t = h + ":" + m;

                                DriverRideModel DRM = new DriverRideModel(
                                        (String) driverDetails.get("first_name"),
                                        (String) driverDetails.get("last_name"),
                                        (String) document.get("src_name"),
                                        (String) document.get("dst_name"),
                                        t,
                                        "7.5"
                                );
                                DRM.start_in_minutes = h * 60 + m;
                                DRM.price = (double) document.get("price");
                                DRM.rating_numerical = 7.5;
                                double w = (CalculationByDistance(src_p, selectedSourcePlacePoint) + CalculationByDistance(pickup_p, selectedSourcePlacePoint)
                                        + CalculationByDistance(dst_p, selectedDestPlacePoint)) * (1 / (h * 60 + m + 1) - DRM.price);
                                DRM.setLocationPoints(src_p, dst_p, pickup_p, w);
                                result.add(
                                        DRM
                                );
                                Log.d(TAG, document.getId() + " => " + document.getData());

                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        }
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            if (sort_alg == 0){
                                result.sort(new sort_by_best_fit());
                            }
                            else if (sort_alg == 1){
                                result.sort(new sort_by_best_rating());
                            }
                            else if (sort_alg == 2){
                                result.sort(new sort_by_best_time());
                            }
                            else{
                                result.sort(new sort_by_best_price());
                            }
                        }
                        adapter = new DriverRideRecyclerViewAdapter(this, result, this);
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
        try {
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
        } catch (Exception e) {
            return 123;
        }

    }

    @Override
    public void onItemClicked(DriverRideModel driverRideModel) {
        Toast.makeText(this, driverRideModel.getFirstName() + " " + driverRideModel.getLastName(), Toast.LENGTH_SHORT).show();

        HashMap<String, String> driverDetailsMap = driverRideModel.getHashDriverDetails();

        // Switch to seat selection activity
        this.finish();
        Intent switchActivityIntent = new Intent(this, seatsSelectionActivity.class);

        // Iterate the map and putExtra on each
        for (Map.Entry<String, String> set : driverDetailsMap.entrySet()) {
            switchActivityIntent.putExtra(set.getKey(), set.getValue());
        }
        startActivity(switchActivityIntent);
    }
}