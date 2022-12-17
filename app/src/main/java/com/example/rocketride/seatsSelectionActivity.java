package com.example.rocketride;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class seatsSelectionActivity extends AppCompatActivity {
    final int Duration_time = 2500;
    private boolean seatSelected = false;
    private ImageView currSeatSelectedView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seats_selection);

        Bundle extras = getIntent().getExtras();

        double price = 0.0;
        String firstName = "", lastName = "", source = "", dest = "", startTime = "", rating = "",
               pickupName = "",  date = "";

        if(extras != null){
            firstName = extras.getString("first_name", "");
            lastName = extras.getString("last_name", "");
            source = extras.getString("source", "");
            dest = extras.getString("destination", "");
            startTime = extras.getString("start_time", "");
            rating = extras.getString("rating", "");
            pickupName = extras.getString("pickup_name", "");
            price = extras.getDouble("price", 0.0);
            date = extras.getString("date", "");
            System.out.println(firstName + " " + lastName + " " + source + " " + dest + " " + startTime + " " + rating);
        }

        setRideDetails(firstName, lastName, source, dest, startTime, rating, pickupName, price, date);

        // Hide action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        ImageView carView = findViewById(R.id.imageView4),

                  // View images of the available seats
                  driverSeatView = findViewById(R.id.imageViewDriverUnavailable),
                  nearDriverAvailableSeatView = findViewById(R.id.nearDriverAvailableImageView),
                  leftBottomAvailableSeatView = findViewById(R.id.leftBottomAvailableImageView),
                  centerBottomAvailableSeatView = findViewById(R.id.centerBottomAvailableImageView),
                  rightBottomAvailableSeatView = findViewById(R.id.rightBottomAvailableImageView),

                  // View images of the unavailable seats
                  nearDriverUnavailableSeatView = findViewById(R.id.nearDriverUnavailableImageView),
                  leftBottomUnavailableSeatView = findViewById(R.id.leftBottomUnavailableImageView),
                  centerBottomUnavailableSeatView = findViewById(R.id.centerBottomUnavailableImageView),
                  rightBottomUnavailableSeatView = findViewById(R.id.rightBottomUnavailableImageView);

        TextView driverSeat = findViewById(R.id.textViewDriver),
                 nearDriverSeat = findViewById(R.id.textViewNearDriver),
                 leftBottomSeat = findViewById(R.id.textViewBottomLeft),
                 centerBottomSeat = findViewById(R.id.textViewBottomCenter),
                 rightBottomSeat = findViewById(R.id.textViewBottomRight);

        ImageView backImageView = findViewById(R.id.seatsBackImageView),
                nextImageView = findViewById(R.id.seatsNextImageView);

        driverSeatView.setVisibility(View.GONE);

        // animation
        Animation carGetInside = AnimationUtils.loadAnimation(seatsSelectionActivity.this,R.anim.car_animation);

        carView.setAnimation(carGetInside);

        new Handler().postDelayed(() -> driverSeatView.setVisibility(View.VISIBLE),Duration_time);

        // Update initial seat status
        //new Handler().postDelayed(() -> nearDriverSeatView.setVisibility(View.VISIBLE),Duration_time + 500);



        // Listeners for seats and buttons
        driverSeat.setOnClickListener(l -> {
            Toast.makeText(seatsSelectionActivity.this, "can't be selected!", Toast.LENGTH_LONG).show();
        });

        nearDriverSeat.setOnClickListener(l -> {
            Toast.makeText(seatsSelectionActivity.this, "near driver seat clicked!", Toast.LENGTH_LONG).show();
            availableChecks(nearDriverAvailableSeatView);
        });

        leftBottomSeat.setOnClickListener(l -> {
            Toast.makeText(seatsSelectionActivity.this, "left bottom seat clicked!", Toast.LENGTH_LONG).show();
            availableChecks(leftBottomAvailableSeatView);
        });

        centerBottomSeat.setOnClickListener(l -> {
            Toast.makeText(seatsSelectionActivity.this, "center bottom seat clicked!", Toast.LENGTH_LONG).show();
            availableChecks(centerBottomAvailableSeatView);
        });

        rightBottomSeat.setOnClickListener(l -> {
            Toast.makeText(seatsSelectionActivity.this, "right bottom seat clicked!", Toast.LENGTH_LONG).show();

            availableChecks(rightBottomAvailableSeatView);
        });

        nextImageView.setOnClickListener(l -> {
            Toast.makeText(seatsSelectionActivity.this, "next button clicked!", Toast.LENGTH_LONG).show();
        });

        backImageView.setOnClickListener(l -> {
            Toast.makeText(seatsSelectionActivity.this, "go back button clicked!", Toast.LENGTH_LONG).show();

            // Intent back to searching a ride
            this.finish();
            Intent switchActivityIntent = new Intent(this, RideSearchActivity.class);
            startActivity(switchActivityIntent);
        });
    }

    protected void availableChecks(ImageView seatImageView){
        boolean currSeatSelected = seatImageView.getVisibility() == View.VISIBLE;
        if (currSeatSelected){
            seatImageView.setVisibility(View.GONE);
            seatSelected = false;
            return;
        }

        // User have already selected an available seat
        if (seatSelected){
            currSeatSelectedView.setVisibility(View.GONE);
        }

        // User haven't selected any seat
        seatImageView.setVisibility(View.VISIBLE);
        seatSelected = true;
        currSeatSelectedView = seatImageView;
    }

    public void setRideDetails(String firstName, String lastName, String source, String dest, String startTime, String rating, String pickupName, double price, String date){
        TextView fullNameView = findViewById(R.id.driver_name),
                 sourceView = findViewById(R.id.src),
                 destinationView = findViewById(R.id.dest),
                 pickupView = findViewById(R.id.pickup),
                 priceView = findViewById(R.id.price),
                 dateView = findViewById(R.id.date_and_time);

        String fullName = firstName + " " + lastName;
        fullNameView.setText(fullName);
        sourceView.setText(source);
        destinationView.setText(dest);
        dateView.setText(date + " " + startTime);
        pickupView.setText(pickupName);
        priceView.setText(String.valueOf(price));
    }
}