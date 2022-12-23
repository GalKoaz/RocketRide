package com.example.rocketride.Models;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.rocketride.Adapters.DriverRideRecyclerViewAdapter;
import com.example.rocketride.Adapters.SelectDriverListener;
import com.example.rocketride.MenuActivities.HomeActivity;
import com.example.rocketride.R;
import com.example.rocketride.Ride.seatsSelectionActivity;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;


public class RideSearchActivity extends AppCompatActivity implements SelectDriverListener {
    private static final String TAG = RideSearchActivity.class.getName();
    private Button GoBack;
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

        // Hide action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //get the spinner from the xml.
        Spinner dropdown = findViewById(R.id.spinner1);
        //create a list of items for the spinner.
        String[] items = new String[]{"Sort By","Best", "Price", "Time","Rating"};
        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        //set the spinners adapter to the previously created one.
        dropdown.setAdapter(adapter1);
        // Recycle view
        recyclerView = findViewById(R.id.myRecyclerView);
        GoBack=findViewById(R.id.back_from_search);

        // Set the adapter
        adapter = new DriverRideRecyclerViewAdapter(this, closeRides, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        GoBack.setOnClickListener(view -> {
            this.finish();
            Intent switchActivityIntent = new Intent(this, HomeActivity.class);
            startActivity(switchActivityIntent);
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

        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                System.out.println(dropdown.getAdapter().getItem(i));
                String choose = (String) dropdown.getAdapter().getItem(i);
                if(choose.equals("Time")){
                    sort_alg = 2;
                    setUpCloseRides();
                }
                else if(choose.equals("Best")){
                    sort_alg = 0;
                    setUpCloseRides();
                }
                else if(choose.equals("Price")){
                    sort_alg = 3;
                    setUpCloseRides();
                }
                else if(choose.equals("Rating")){
                    sort_alg = 1;
                    setUpCloseRides();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                return;
            }

        });

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

        // Directional arrows image view
        ImageView directArrowsImageView = findViewById(R.id.directionalArrowRideSearch);
        directArrowsImageView.setOnClickListener(view -> {
            // Set directional arrows animation
            ObjectAnimator animator = ObjectAnimator.ofFloat(view, "rotation", 0f, 180f);
            animator.setRepeatMode(ValueAnimator.REVERSE); // set the animation to reverse direction each time it repeats
            animator.start(); // start the animation

            // replace fields text
            String src = selectedSourcePlace, dest = selectedDestPlace;
            autocompleteFragment.setText(dest);
            autocompleteFragment2.setText(src);

            // Swap dest and src
            selectedSourcePlace = dest;
            selectedDestPlace = src;
        });
    }

    protected void setUpCloseRides(){
        // TODO: here extract closest rides to current user.
        //       build all related objects afterwards and push them to
        //       the associated array list called - "closeRides".
        if(selectedDestPlace.equals("") || selectedSourcePlace.equals("")) {
            return;
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
                                System.out.println("before driver-id");
                                System.out.println((String) document.get("driver-id"));
                                Task<Map<String, Object>> driverDetailsTask = getDriverDetails((String) document.get("driver-id"));
                                driverDetailsTask.addOnCompleteListener(taskDriverDetails -> {
                                    if (taskDriverDetails.isSuccessful()) {
                                        HashMap<String, Object> driverDetails = (HashMap<String, Object>) taskDriverDetails.getResult();
                                        System.out.println("before driver-id");
                                        String pickupName = (String) document.get("pickup_name");
                                        double price = (double) document.get("price");
                                        String driverID = (String) document.get("driver-id");
                                        String rideId = document.getId();

                                        Long h = (Long) document.get("time_h");
                                        Long min = (Long) document.get("time_m");
                                        Long d = (Long) document.get("date-d");
                                        Long year = (Long) document.get("date-y");
                                        Long month = (Long) document.get("date-m");
                                        h -= HOUR;
                                        min -= MINUTE;
                                        if (min < 0) {
                                            h -= 1;
                                            min = 60 + min;
                                        }
                                        h += (d - DATE) * 24;
                                        String t = h + ":" + min;

                                        System.out.println("first name: " + driverDetails.get("first_name"));

                                        DriverRideModel DRM = new DriverRideModel(
                                                (String) driverDetails.get("first_name"),
                                                (String) driverDetails.get("last_name"),
                                                (String) document.get("src_name"),
                                                (String) document.get("dst_name"),
                                                t,
                                                "7.5",
                                                pickupName,
                                                price,
                                                d + "/" + month + "/" + year,
                                                driverID,
                                                rideId
                                        );

                                        // Set driver's seats status
                                        DRM.setCarSeats(
                                                (String) document.get("near_driver_seat"),
                                                (String) document.get("left_bottom_seat"),
                                                (String) document.get("center_bottom_seat"),
                                                (String) document.get("right_bottom_seat")
                                        );

                                        // Set profile image link
                                        DRM.setProfileImageURL((String) driverDetails.get("profile_image_link"));

                                        DRM.start_in_minutes = h * 60 + min;
                                        DRM.price = (double) document.get("price");
                                        DRM.rating_numerical = 7.5;
                                        double w = (CalculationByDistance(src_p, selectedSourcePlacePoint) + CalculationByDistance(pickup_p, selectedSourcePlacePoint)
                                                + CalculationByDistance(dst_p, selectedDestPlacePoint)) * (1 / (h * 60 + min + 1) - DRM.price);
                                        DRM.setLocationPoints(src_p, dst_p, pickup_p, w);
                                        result.add(
                                                DRM
                                        );
                                        System.out.println("result size: " + result.size());
                                        Log.d(TAG, document.getId() + " => " + document.getData());

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
                                        Log.e(TAG, "Error getting driver details: ", task.getException());
                                    }
                                });
                            }
                        }
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                            if (sort_alg == 0){
//                                result.sort(new sort_by_best_fit());
//                            }
//                            else if (sort_alg == 1){
//                                result.sort(new sort_by_best_rating());
//                            }
//                            else if (sort_alg == 2){
//                                result.sort(new sort_by_best_time());
//                            }
//                            else{
//                                result.sort(new sort_by_best_price());
//                            }
//                        }
//                        adapter = new DriverRideRecyclerViewAdapter(this, result, this);
//                        recyclerView.setAdapter(adapter);
//                        recyclerView.setLayoutManager(new LinearLayoutManager(this));
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });
    }

//    protected HashMap<String, Object> getDriverDetails(String UID){
//        HashMap<String, Object> userDetails = new HashMap<>();
//
//        // Create a reference to the rides collection
//        CollectionReference rides = db.collection("users");
//
//        Query query = rides.whereEqualTo("UID", UID);
//        System.out.println("enter getDriverDetails");
//        // Store query result
//        query.get()
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        for (QueryDocumentSnapshot document : task.getResult()) {
//                            System.out.println("Success getDriverDetails");
//                            userDetails.put("first_name", document.get("first_name"));
//                            userDetails.put("last_name", document.get("last_name"));
//                            userDetails.put("profile_image_link", document.get("profile_image_link"));
//                            Log.d(TAG, document.getId() + " => " + document.getData());
//                        }
//                    } else {
//                        Log.d(TAG, "Error getting documents: ", task.getException());
//                    }
//                });
//        System.out.println("end getDrierDetails");
//        return userDetails;
//    }

    private Task<Map<String, Object>> getDriverDetails(String driverId) {
        // Create a reference to the drivers collection
        CollectionReference users = db.collection("users");

        // Create a query to retrieve the driver with the specified ID
        Query query = users.whereEqualTo("UID", driverId);

        // Return the task for the query
        return query.get().continueWith(task -> {
            if (task.isSuccessful()) {
                // If the query is successful, get the first result (since we are searching by ID, there should only be one result)
                QuerySnapshot querySnapshot = task.getResult();
                DocumentSnapshot document = querySnapshot.getDocuments().get(0);

                // Create a map to store the driver details
                Map<String, Object> driverDetails = new HashMap<>();

                // Add the driver details to the map
                driverDetails.put("first_name", document.get("first_name"));
                driverDetails.put("last_name", document.get("last_name"));
                driverDetails.put("profile_image_link", document.get("profile_image_link"));
                Log.e(TAG, "success driver details");
                // Return the map
                return driverDetails;
            } else {
                // If the query fails, log the error and return an empty map
                Log.e(TAG, "Error getting documents: ", task.getException());
                return new HashMap<>();
            }
        });
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

        HashMap<String, Object> driverDetailsMap = driverRideModel.getHashDriverDetails();

        // Switch to seat selection activity
        this.finish();
        Intent switchActivityIntent = new Intent(this, seatsSelectionActivity.class);

        // Iterate the map and putExtra on each
        for (Map.Entry<String, Object> set : driverDetailsMap.entrySet()) {
            if (set.getKey().equals("price")) {
                switchActivityIntent.putExtra(set.getKey(), (double) set.getValue());
                continue;
            }
            switchActivityIntent.putExtra(set.getKey(), (String) set.getValue());
        }
        startActivity(switchActivityIntent);
    }
}