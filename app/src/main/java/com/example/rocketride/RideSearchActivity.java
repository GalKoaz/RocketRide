package com.example.rocketride;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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

import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class RideSearchActivity extends FragmentActivity{

    Button DateButton, TimeButton;
    TextView DateText, TimeText;
    GoogleMap map;
    SupportMapFragment mapFragment;
    SearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_search);
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), getString(R.string.api_key), Locale.US);
        }

        DateButton = findViewById(R.id.date_pick_button);
        TimeButton = findViewById(R.id.time_pick_button);
        DateText = findViewById(R.id.date_text);
        TimeText = findViewById(R.id.time_text);
        //mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map_picker);

        DateButton.setOnClickListener(view -> handdle_date_click());
        TimeButton.setOnClickListener(view -> handdle_time_click());

        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                // TODO: Get info about the selected place.
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
            }
            @Override
            public void onError(@NonNull Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });
    }

    public void handdle_date_click(){

        Calendar calendar = Calendar.getInstance();

        int YEAR = calendar.get(calendar.YEAR);
        int MONTH = calendar.get(calendar.MONTH);
        int DATE = calendar.get(calendar.DATE);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (datePicker, year, month, date) -> {
            String  str = date + "." +  month + "." + year;
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
            TimeText.setText(str);
        }),HOUR,MINUTE,true);

        timePickerDialog.show();

    }
}