package com.example.rocketride.Ride;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rocketride.MenuActivities.HomeActivity;
import com.example.rocketride.R;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class CreateDriveActivity extends AppCompatActivity{

    Button DateButton, TimeButton, Submit;
    TextView DateText, TimeText;
    ImageView GoBack;
    NumberPicker seats;
    private FirebaseFirestore db;
    EditText PriceEntery;
    EditText detailsEntery;
    String date = "";
    String time = "";
    String source;
    GeoPoint s_point;
    String dst;
    GeoPoint d_point;
    String pickup;
    GeoPoint p_point;
    String price;
    String data_drive;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        date = "";
        time = "";
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_drive);
        db = FirebaseFirestore.getInstance();
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), getString(R.string.api_key), Locale.US);
        }
        Submit = findViewById(R.id.drive_submit);
        GoBack = findViewById(R.id.back_from_create);
        DateButton = findViewById(R.id.date_pick_button);
        TimeButton = findViewById(R.id.time_pick_button);
        DateText = findViewById(R.id.date_text);
        TimeText = findViewById(R.id.time_text);
        seats = findViewById(R.id.seats);
        PriceEntery = findViewById(R.id.create_price);
        detailsEntery = findViewById(R.id.create_pickup);

        seats.setMinValue(1);
        seats.setMaxValue(4);

        DateButton.setOnClickListener(view -> handdle_date_click());
        TimeButton.setOnClickListener(view -> handdle_time_click());
        Submit.setOnClickListener(view -> {
            submit_drive();
        });

        GoBack.setOnClickListener(view -> {
            // Move user to home activity
            this.finish();
            Intent switchActivityIntent = new Intent(this, HomeActivity.class);
            startActivity(switchActivityIntent);
        });
        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                // TODO: Get info about the selected place.
                source = place.getName();
                s_point = new GeoPoint(place.getLatLng().latitude, place.getLatLng().longitude);
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
            }
            @Override
            public void onError(@NonNull Status status) {
                // TODO: Handle the error.
                source = "";
                Log.i(TAG, "An error occurred: " + status);
            }
        });

        AutocompleteSupportFragment autocompleteFragment2 = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment2);

        // Specify the types of place data to return.
        autocompleteFragment2.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment2.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                // TODO: Get info about the selected place.
                dst = place.getName();
                d_point = new GeoPoint(place.getLatLng().latitude, place.getLatLng().longitude);
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getLatLng());
            }
            @Override
            public void onError(@NonNull Status status) {
                // TODO: Handle the error.
                dst = "";
                Log.i(TAG, "An error occurred: " + status);
            }
        });

        AutocompleteSupportFragment autocompleteFragment3 = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment3);

        // Specify the types of place data to return.
        autocompleteFragment3.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment3.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                // TODO: Get info about the selected place.
                pickup = place.getName();
                p_point = new GeoPoint(place.getLatLng().latitude, place.getLatLng().longitude);
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
            }
            @Override
            public void onError(@NonNull Status status) {
                pickup = "";
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });
        autocompleteFragment.setHint("Choose Source");
        autocompleteFragment2.setHint("Choose Destination");
        autocompleteFragment3.setHint("Pick Up Location");
    }

    public void submit_drive(){
        if(this.PriceEntery.getText() != null){
            this.price = this.PriceEntery.getText().toString();
        }
        else{
            this.price = "";
        }
        if(this.detailsEntery.getText() != null) {
            this.data_drive = this.detailsEntery.getText().toString();
        }
        else{
            this.data_drive = "";
        }
        if(this.price.equals("") || this.data_drive.equals("") || this.pickup.equals("") || this.dst.equals("") || this.source.equals("")
            || this.date.equals("") || this.time.equals("")){
            Toast.makeText(CreateDriveActivity.this, "Fill all the data.",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("driver-id", userId);

        String[] date_s = this.date.split("\\.");
        int year = Integer.parseInt(date_s[2]);
        int month = Integer.parseInt(date_s[1]);
        int day = Integer.parseInt(date_s[0]);

        userMap.put("date-y", year);
        userMap.put("date-m", month);
        userMap.put("date-d", day);

        String[] time_s = this.time.split(":");
        int hour = Integer.parseInt(time_s[0]);
        int min = Integer.parseInt(time_s[1]);

        userMap.put("time_h", hour);
        userMap.put("time_m", min);

        userMap.put("alive", true);
        userMap.put("canceled", false);
        userMap.put("src_name", this.source);
        userMap.put("src_lat", this.s_point.getLatitude());
        userMap.put("src_lon", this.s_point.getLongitude());
        userMap.put("dst_name", this.dst);
        userMap.put("dst_lat", this.d_point.getLatitude());
        userMap.put("dst_lon", this.d_point.getLongitude());
        userMap.put("pickup_name", this.pickup);
        userMap.put("pickup_lat", this.p_point.getLatitude());
        userMap.put("pickup_lon", this.p_point.getLongitude());

        double price_num = Integer.parseInt(this.price);
        userMap.put("price", price_num);
        userMap.put("details", this.data_drive);

        // Add car seats
        userMap.put("near_driver_seat", "");
        userMap.put("left_bottom_seat", "");
        userMap.put("center_bottom_seat", "");
        userMap.put("right_bottom_seat", "");

        // Add ride as a document in drives collection
        addRide(userMap);

        // Move to home activity
        this.finish();
        Intent switchActivityIntent = new Intent(this, HomeActivity.class);
        startActivity(switchActivityIntent);
    }

    public void addRide( Map<String, Object> rideMap){
        // Add a new document with a generated ID
        db.collection("drives")
                .add(rideMap)
                .addOnSuccessListener(documentReference -> {
                    String documentID = documentReference.getId();
                    Log.d(TAG, "DocumentSnapshot added with ID: " + documentID);

                    // Update: add document id as a field in the ride document
                    documentReference.update("_id", documentID);

                    Toast.makeText(CreateDriveActivity.this, "Drive created!", Toast.LENGTH_SHORT).show();
                });
    }

    public void handdle_date_click(){

        Calendar calendar = Calendar.getInstance();

        int YEAR = calendar.get(calendar.YEAR);
        int MONTH = calendar.get(calendar.MONTH);
        int DATE = calendar.get(calendar.DATE);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (datePicker, year, month, date) -> {
            String  str = date + "." +  month + "." + year;
            this.date = str;
            DateText.setText(str);
        },YEAR,MONTH,DATE);

        datePickerDialog.show();
    }
    public void handdle_time_click(){
        Calendar calendar = Calendar.getInstance();

        int HOUR = calendar.get(calendar.HOUR);
        int MINUTE = calendar.get(calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, ((timePicker, hour, minute) -> {
            String str = hour +":"+minute;
            this.time = str;
            TimeText.setText(str);
        }),HOUR,MINUTE,true);

        timePickerDialog.show();
    }
}