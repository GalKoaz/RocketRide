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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seats_selection);

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

        Button nextButton = findViewById(R.id.nextButton),
                goBackButton = findViewById(R.id.goBackButton);

        driverSeatView.setVisibility(View.GONE);

        // animation
        Animation carGetInside = AnimationUtils.loadAnimation(seatsSelectionActivity.this,R.anim.car_animation);

        carView.setAnimation(carGetInside);

        new Handler().postDelayed(() -> driverSeatView.setVisibility(View.VISIBLE),Duration_time);

        // Update initial seat status
        //new Handler().postDelayed(() -> nearDriverSeatView.setVisibility(View.VISIBLE),Duration_time + 500);

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


        nextButton.setOnClickListener(l -> {
            Toast.makeText(seatsSelectionActivity.this, "next button clicked!", Toast.LENGTH_LONG).show();
        });

        goBackButton.setOnClickListener(l -> {
            Toast.makeText(seatsSelectionActivity.this, "go back button clicked!", Toast.LENGTH_LONG).show();
        });
    }


    protected void availableChecks(ImageView seatImageView){
        Boolean currSeatSelected = seatImageView.getVisibility() == View.VISIBLE;
        if (currSeatSelected){
            seatImageView.setVisibility(View.GONE);
            seatSelected = false;
            return;
        }

        // User have already selected an available seat
        if (seatSelected){
            Toast.makeText(seatsSelectionActivity.this, "Seat already selected!", Toast.LENGTH_LONG).show();
            return;
        }
        seatImageView.setVisibility(View.VISIBLE);
        seatSelected = true;
    }
}