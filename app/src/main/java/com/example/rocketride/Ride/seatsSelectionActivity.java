package com.example.rocketride.Ride;

import static android.content.ContentValues.TAG;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rocketride.MenuActivities.HomeActivity;
import com.example.rocketride.Models.RemindBroadcast;
import com.example.rocketride.Models.RideSearchActivity;
import com.example.rocketride.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class seatsSelectionActivity extends AppCompatActivity {
    final int Duration_time = 2500;
    private boolean seatSelected = false;
    private ImageView currAvailableSeatSelectedView;
    private ImageView currUnavailableSeatSelectedView;
    private String currSeatSelectionName;

    private String driverID, rideID;
    private String s,d;
    private FirebaseFirestore db;
    private int start_time = 1;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seats_selection);

        db = FirebaseFirestore.getInstance();

        currSeatSelectionName = "";
        driverID = "";
        rideID = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            createNotifyChannel();
        }
        Bundle extras = getIntent().getExtras();

        double price = 0.0;
        String firstName = "", lastName = "", source = "", dest = "", startTime = "", rating = "",
               pickupName = "",  date = "";

        String nearDriverSeatStr = "", leftBottomSeatStr = "", centerBottomSeatStr = "", rightBottomSeatStr = "";

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
            driverID = extras.getString("driver_id", "");
            rideID = extras.getString("ride_id", "");
            System.out.println(firstName + " " + lastName + " " + source + " " + dest + " " + startTime + " " + rating);

            // Driver's seats status
            nearDriverSeatStr = extras.getString("near_driver_seat", "");
            leftBottomSeatStr = extras.getString("left_bottom_seat", "");
            centerBottomSeatStr = extras.getString("center_bottom_seat", "");
            rightBottomSeatStr = extras.getString("right_bottom_seat", "");
        }

        // Hide action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        s = source;
        d = dest;
        // Set the view to represent the driver's ride details
        setRideDetails(firstName, lastName, source, dest, startTime, rating, pickupName, price, date);
        String s = ":";
        int hours = Integer.parseInt(startTime.split(s)[0]);
        start_time = Integer.parseInt(startTime.split(s)[1]) + 60*hours;
      carView = findViewById(R.id.imageView4);

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

      ImageView backImageView = findViewById(R.id.seatsBackImageView),
                nextImageView = findViewById(R.id.seatsNextImageView);


        driverSeatView.setVisibility(View.GONE);

        // animation
        Animation carGetInside = AnimationUtils.loadAnimation(seatsSelectionActivity.this, R.anim.car_animation);

        carView.setAnimation(carGetInside);

        // set final strings for each seat
        String finalNearDriverSeatStr = nearDriverSeatStr;
        String finalLeftBottomSeatStr = leftBottomSeatStr;
        String finalCenterBottomSeatStr = centerBottomSeatStr;
        String finalRightBottomSeatStr = rightBottomSeatStr;

        new Handler().postDelayed(() -> {
            driverSeatView.setVisibility(View.VISIBLE);

            // Set the view for the driver's seats
            setRideSeats(finalNearDriverSeatStr, finalLeftBottomSeatStr, finalCenterBottomSeatStr, finalRightBottomSeatStr);
        },Duration_time);

        // Listeners for seats and buttons
        driverSeat.setOnClickListener(l -> {
            Toast.makeText(seatsSelectionActivity.this, "can't be selected!", Toast.LENGTH_LONG).show();
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

        nextImageView.setOnClickListener(l -> {
            completeSeatSelection();
            Toast.makeText(seatsSelectionActivity.this, "next button clicked!", Toast.LENGTH_LONG).show();
        });

        backImageView.setOnClickListener(l -> {
            // Intent back to searching a ride
            this.finish();
            Intent switchActivityIntent = new Intent(this, RideSearchActivity.class);
            startActivity(switchActivityIntent);
        });
    }

    protected void availableChecks(ImageView seatAvailableImageView, ImageView seatUnavailableImageView,  String seatName){
        // Seat is unavailable and thus cannot be selected
        boolean currSeatUnavailableSelected = seatUnavailableImageView.getVisibility() == View.VISIBLE;
        if (currSeatUnavailableSelected){
            return;
        }

        boolean currSeatAvailableSelected = seatAvailableImageView.getVisibility() == View.VISIBLE;
        if (currSeatAvailableSelected){
            seatAvailableImageView.setVisibility(View.GONE);
            seatSelected = false;
            currSeatSelectionName = "";
            return;
        }

        // User have already selected an available seat
        if (seatSelected){
            currAvailableSeatSelectedView.setVisibility(View.GONE);
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
        else carUnavailableSeatView.setVisibility(View.VISIBLE);
    }
    public static PendingIntent createPendingIntentGetBroadCast(Context context, int id, Intent intent, int flag) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            return PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_IMMUTABLE | flag);
        } else {
            return PendingIntent.getBroadcast(context, id, intent, flag);
        }
    }
    public void completeSeatSelection(){
        // User tries to complete seat selection process without a seat selected
        if (currSeatSelectionName.equals("")) {
            Toast.makeText(seatsSelectionActivity.this, "You haven't selected a seat!", Toast.LENGTH_LONG).show();
            return;
        }

        // TODO: ADD THIS FIELD TO ANY OF DRIVES DOCUMENTS
        // Check if the current seat status is available and didn't caught by other user.
        Query query = db.collection("drives").whereEqualTo("_id", rideID);

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String seatStatus = document.getString(currSeatSelectionName);
                    System.out.println("seat status: " + currSeatSelectionName + " " + seatStatus);
                    RemindBroadcast.from = s;
                    RemindBroadcast.to = d;
                    if (seatStatus.equals("")){
                        System.out.println("seat is available - can complete process");
                        Intent intent = new Intent(seatsSelectionActivity.this, RemindBroadcast.class);
                        PendingIntent pendingIntent = createPendingIntentGetBroadCast(seatsSelectionActivity.this,0,intent,0);
                        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

                        long timeAtButtonClicked = System.currentTimeMillis();
                        long bounus;
                        if (start_time - 30 <= 0){
                            bounus = 1000 * 5;
                        }
                        else{
                            bounus = 1000L * (start_time-30) * 60;
                        }

                        alarmManager.set(AlarmManager.RTC_WAKEUP,timeAtButtonClicked+bounus, pendingIntent);
                        // TODO: CATCHING FOR NOW THE SEAT WITHOUT PAYMENT FOR TESTING PURPOSES
                        setUserSeat(currSeatSelectionName, document.getId(), FirebaseAuth.getInstance().getUid());
                    }
                    else{
                        currAvailableSeatSelectedView.setVisibility(View.GONE);
                        currUnavailableSeatSelectedView.setVisibility(View.VISIBLE);
                    }
                }
            }
            else {
                Log.d(TAG, "Error getting documents: ", task.getException());
            }
        });
    }

    public void setUserSeat(String seatName, String documentID, String UID){
        db.collection("drives").document(documentID).update(seatName, UID)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        Toast.makeText(seatsSelectionActivity.this, "Success!", Toast.LENGTH_LONG).show();
                        // Switch to the home activity
                        this.finish();
                        Intent switchActivitySearchRideIntent = new Intent(this, HomeActivity.class);
                        startActivity(switchActivitySearchRideIntent);
                    }else{
                        Log.d(TAG, "Error getting or setting documents: ", task.getException());
                    }
                });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void createNotifyChannel(){
        CharSequence name = "remChannel";
        String description = "remind about drives";
        int importnce = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            channel = new NotificationChannel("rem",name,importnce);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}