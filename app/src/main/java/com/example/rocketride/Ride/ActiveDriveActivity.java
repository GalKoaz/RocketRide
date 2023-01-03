package com.example.rocketride.Ride;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rocketride.MenuActivities.ActiveDrives;
import com.example.rocketride.MenuActivities.HomeActivity;
import com.example.rocketride.Models.RideModel;
import com.example.rocketride.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;

import carbon.widget.Button;

public class ActiveDriveActivity extends AppCompatActivity {
    final int Duration_time = 2500;
    private boolean seatSelected = false;
    private ImageView currAvailableSeatSelectedView;
    private ImageView currUnavailableSeatSelectedView;
    private String currSeatSelectionName, currUnavailableSeatSelectedName;

    private int tabPosition;
    final int MY_DRIVES = 0, MY_CREATED_DRIVES = 1;

    private String driverID, rideID, userType;

    private FirebaseFirestore db;

    // Seats Image views
    ImageView carView,
    // View images of the available seats
    driverSeatView,
            nearDriverAvailableSeatView,
            leftBottomAvailableSeatView,
            centerBottomAvailableSeatView,
            rightBottomAvailableSeatView,

    // View images of the unavailable seats
    nearDriverUnavailableSeatView,
            leftBottomUnavailableSeatView,
            centerBottomUnavailableSeatView,
            rightBottomUnavailableSeatView;

    // Management buttons
    Button cancelRideButton, kickButton, startButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_drive);

        db = FirebaseFirestore.getInstance();

        currSeatSelectionName = "";
        currUnavailableSeatSelectedName = "";
        driverID = "";
        rideID = "";
        userType = "";

        Bundle extras = getIntent().getExtras();
        RideModel rideModel = null;
        if(extras != null){
            userType = extras.getString("type");
            tabPosition = extras.getInt("tab_pos");
            rideModel = (RideModel) extras.getSerializable("ride_model");
        }


        // Hide action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        // TODO: for now i have decided to put empty string in first name and last name
        // Set the view to represent the driver's ride details
        if (rideModel == null) return;
        driverID = rideModel.getDriverID();
        rideID = rideModel.getRideID();
        setRideDetails("", "", rideModel.getSource(), rideModel.getDestination(), "", "", rideModel.getPickup(), rideModel.getPrice(), rideModel.getDate());

        carView = findViewById(R.id.imageView4);

        // View images of the available seats
        driverSeatView = findViewById(R.id.imageViewDriverUnavailable);

        // View images of the available seats
        driverSeatView = findViewById(R.id.imageViewDriverUnavailable);
        nearDriverAvailableSeatView = findViewById(R.id.nearDriverAvailableImageView);
        leftBottomAvailableSeatView = findViewById(R.id.leftBottomAvailableImageView);
        centerBottomAvailableSeatView = findViewById(R.id.centerBottomAvailableImageView);
        rightBottomAvailableSeatView = findViewById(R.id.rightBottomAvailableImageView);

        // View images of the unavailable seats
        nearDriverUnavailableSeatView = findViewById(R.id.nearDriverUnavailableImageView);
        leftBottomUnavailableSeatView = findViewById(R.id.leftBottomUnavailableImageView);
        centerBottomUnavailableSeatView = findViewById(R.id.centerBottomUnavailableImageView);
        rightBottomUnavailableSeatView = findViewById(R.id.rightBottomUnavailableImageView);


        TextView driverSeat = findViewById(R.id.textViewDriver),
                nearDriverSeat = findViewById(R.id.textViewNearDriver),
                leftBottomSeat = findViewById(R.id.textViewBottomLeft),
                centerBottomSeat = findViewById(R.id.textViewBottomCenter),
                rightBottomSeat = findViewById(R.id.textViewBottomRight);

        ImageView backImageView = findViewById(R.id.seatsBackImageView);


        driverSeatView.setVisibility(View.GONE);

        // animation
        Animation carGetInside = AnimationUtils.loadAnimation(ActiveDriveActivity.this, R.anim.car_animation);

        carView.setAnimation(carGetInside);

        // set final strings for each seat
        String finalNearDriverSeatStr = rideModel.getNearDriverSeat();
        String finalLeftBottomSeatStr = rideModel.getLeftBottomSeat();
        String finalCenterBottomSeatStr = rideModel.getCenterBottomSeat();
        String finalRightBottomSeatStr = rideModel.getRightBottomSeat();

        new Handler().postDelayed(() -> {
            driverSeatView.setVisibility(View.VISIBLE);

            // Set the view for the driver's seats
            setRideSeats(finalNearDriverSeatStr, finalLeftBottomSeatStr, finalCenterBottomSeatStr, finalRightBottomSeatStr);
        },Duration_time);

        // Listeners for seats and buttons
        driverSeat.setOnClickListener(l -> {
            Toast.makeText(ActiveDriveActivity.this, "can't be selected!", Toast.LENGTH_LONG).show();
        });

        nearDriverSeat.setOnClickListener(l -> {
            availableChecks(nearDriverAvailableSeatView, nearDriverUnavailableSeatView, "near_driver_seat");
        });

        leftBottomSeat.setOnClickListener(l -> {
            availableChecks(leftBottomAvailableSeatView, leftBottomUnavailableSeatView, "left_bottom_seat");
        });

        centerBottomSeat.setOnClickListener(l -> {
            availableChecks(centerBottomAvailableSeatView, centerBottomUnavailableSeatView, "center_bottom_seat");
        });

        rightBottomSeat.setOnClickListener(l -> {
            availableChecks(rightBottomAvailableSeatView, rightBottomUnavailableSeatView,"right_bottom_seat");
        });
        backImageView.setOnClickListener(l -> {
            // Intent back to searching a ride
            this.finish();
            Intent switchActivityIntent = new Intent(this, ActiveDrives.class);
            switchActivityIntent.putExtra("type", userType);
            startActivity(switchActivityIntent);
        });


        // Management buttons init
        startButton = findViewById(R.id.startRideButton);
        kickButton = findViewById(R.id.kickButton);
        startButton.setOnClickListener(l -> {
            StartRide();
        });
        kickButton.setOnClickListener(l -> {
            kickUserFromRide(rideID, currUnavailableSeatSelectedName);
        });
    }

    protected void StartRide(){
        System.out.println(rideID);
        db.collection("drives").document(rideID)
                .update("alive", false).addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        Log.d(TAG, "success with: " + rideID);

                    }else{
                        Log.d(TAG, "Error getting or setting documents: ", task.getException());
                    }
                });
    }

    protected void availableChecks(ImageView seatAvailableImageView, ImageView seatUnavailableImageView,  String seatName){
        //TODO: IF CURRENT USER IS RIDER RETURN - CAN'T SELECT ANYTHING FOR NOW
        if (userType.equals("rider")) {
            return;
        }
        System.out.println("tab position is: " + tabPosition);
        // current user is driver but selected other drivers' rides
        if (tabPosition == MY_DRIVES){
            return;
        }

        // Seat is unavailable and thus cannot be selected
        boolean currSeatUnavailableSelected = seatUnavailableImageView.getVisibility() == View.VISIBLE;
        boolean currSeatAvailableSelected = seatAvailableImageView.getVisibility() == View.VISIBLE;
        if (currSeatUnavailableSelected){
            //TODO: if user is driver then make a cancellation button appear
            // else, do nothing.
            kickButton.setVisibility(View.VISIBLE);
            currUnavailableSeatSelectedName = seatName;
            currUnavailableSeatSelectedView = seatUnavailableImageView;
            return;
        }
        else if (currSeatAvailableSelected){
            System.out.println("unblocked");
            seatAvailableImageView.setVisibility(View.GONE);
            seatSelected = false;
            currSeatSelectionName = "";
            db.collection("drives").document(rideID).update(seatName, "");
            return;
        }
        else{
            System.out.println("blocked");
            db.collection("drives").document(rideID).update(seatName, "X");
        }

        // User haven't selected any seat
        seatAvailableImageView.setVisibility(View.VISIBLE);
        seatSelected = true;
        currAvailableSeatSelectedView = seatAvailableImageView;
        currUnavailableSeatSelectedView = seatUnavailableImageView;
        currSeatSelectionName = seatName;
    }

    public void setRideDetails(String firstName, String lastName, String source, String dest, String startTime, String rating,
                               String pickupName, double price, String date){
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

    public void setRideSeats(String nearDriverSeat, String leftBottomSeat, String centerBottomSeat, String rightBottomSeat){
        setSeatView(nearDriverSeat, nearDriverAvailableSeatView, nearDriverUnavailableSeatView);
        setSeatView(leftBottomSeat, leftBottomAvailableSeatView, leftBottomUnavailableSeatView);
        setSeatView(centerBottomSeat, centerBottomAvailableSeatView, centerBottomUnavailableSeatView);
        setSeatView(rightBottomSeat, rightBottomAvailableSeatView, rightBottomUnavailableSeatView);
    }

    public void setSeatView(String carSeat, ImageView carAvailableSeatView, ImageView carUnavailableSeatView){
        System.out.println("car seat -> " + carSeat);
        if (carSeat.equals("")) carAvailableSeatView.setVisibility(View.GONE);
        else if (carSeat.equals("X")) carAvailableSeatView.setVisibility(View.VISIBLE);
        else carUnavailableSeatView.setVisibility(View.VISIBLE);
    }
    /**
     * Method cancels the given ride.
     * @param rideID ride's document id.
     */
    public void cancelRide(String rideID){
        HashMap<String, Object> setMap = new HashMap<>();
        setMap.put("alive", false);
        setMap.put("canceled", true);
        db.collection("drives").document(rideID)
                .update(setMap).addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        // Intent back to home activity
                        this.finish();
                        Intent switchActivityIntent = new Intent(this, HomeActivity.class);
                        switchActivityIntent.putExtra("type", userType);
                        startActivity(switchActivityIntent);
                    }else{
                        Log.d(TAG, "Error getting or setting documents: ", task.getException());
                    }
                });
    }

    public void cancelRiderRide(String rideID){
        Query query = db.collection("drives").whereEqualTo("_id", rideID);

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String UID = FirebaseAuth.getInstance().getUid();

                    String seats[] = new String[]{
                            document.getString("near_driver_seat"),
                            document.getString("left_bottom_seat"),
                            document.getString("center_bottom_seat"),
                            document.getString("right_bottom_seat")
                    };

                    String[] seatsNames= new String[]{
                            "near_driver_seat",
                            "left_bottom_seat",
                            "center_bottom_seat",
                            "right_bottom_seat"
                    };

                    // Iterate the seats and if user is there then remove him from seat.
                    for (int i = 0; i < seats.length; i++) {
                        String currSeatName = seatsNames[i];
                        String currSeatUserID = seats[i];

                        // Null check on string in the document
                        if (currSeatUserID == null){
                            continue;
                        }

                        // User is located in the current seat
                        if (currSeatUserID.equals(UID)) {
                            clearSeat(currSeatName, rideID);
                        }
                    }

                }
                // Go back to the active drives
                this.finish();
                Intent switchActivityIntent = new Intent(this, ActiveDrives.class);
                switchActivityIntent.putExtra("type", userType);
                startActivity(switchActivityIntent);
            }
            else {
                Log.d(TAG, "Error getting documents: ", task.getException());
            }
        });
    }

    public void clearSeat(String userSeatName, String rideID){
        db.collection("drives").document(rideID)
                .update(userSeatName, "").addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        Log.d(TAG, "success with: " + userSeatName + " " + rideID);

                    }else{
                        Log.d(TAG, "Error getting or setting documents: ", task.getException());
                    }
                });

    }


    public void kickUserFromRide(String rideID, String userSeat){
        db.collection("drives").document(rideID)
                .update(userSeat, "").addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        System.out.println("currUnvavile: " + currUnavailableSeatSelectedView);

                        currUnavailableSeatSelectedView.setVisibility(View.GONE);
                        kickButton.setVisibility(View.GONE);
                        Toast.makeText(ActiveDriveActivity.this, "Success!", Toast.LENGTH_LONG).show();
                    }else{
                        Log.d(TAG, "Error getting or setting documents: ", task.getException());
                    }
                });
    }

}