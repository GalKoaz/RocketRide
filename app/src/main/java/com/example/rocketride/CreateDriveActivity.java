package com.example.rocketride;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class CreateDriveActivity extends FragmentActivity{

    Button DateButton, TimeButton, GoBack, Submit;
    TextView DateText, TimeText;
    private FirebaseFirestore db;
    TextInputEditText PriceEntery;
    TextInputEditText detailsEntery;
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
        PriceEntery = findViewById(R.id.create_price);
        detailsEntery = findViewById(R.id.create_pickup);

        DateButton.setOnClickListener(view -> handdle_date_click());
        TimeButton.setOnClickListener(view -> handdle_time_click());
        Submit.setOnClickListener(view -> {
            submit_drive();
        });

        GoBack.setOnClickListener(view -> {
            this.finish();
            Intent switchActivityIntent = new Intent(this, MapsDriverActivity.class);
//            switchActivityIntent.putExtra("message", "From: " + MainActivity.class.getSimpleName());
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
        userMap.put("date", this.date);
        userMap.put("time", this.time);
        userMap.put("alive", true);
        userMap.put("src_name", this.source);
        userMap.put("src", this.s_point);
        userMap.put("dst_name", this.dst);
        userMap.put("dst", this.d_point);
        userMap.put("pickup_name", this.pickup);
        userMap.put("pickup", this.p_point);
        userMap.put("price", this.price);
        userMap.put("details", this.data_drive);

        db.collection("drives").document().set(userMap);
        Toast.makeText(CreateDriveActivity.this, "Drive created.",
                Toast.LENGTH_SHORT).show();
        this.finish();
        Intent switchActivityIntent = new Intent(this, MapsDriverActivity.class);
//            switchActivityIntent.putExtra("message", "From: " + MainActivity.class.getSimpleName());
        startActivity(switchActivityIntent);
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