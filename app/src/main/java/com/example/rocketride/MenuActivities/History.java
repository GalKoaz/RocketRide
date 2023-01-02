package com.example.rocketride.MenuActivities;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.rocketride.Adapters.HistoryRideListener;
import com.example.rocketride.MainActivity;
import com.example.rocketride.Models.DriverRideModel;
import com.example.rocketride.Adapters.HistoryRecyclerViewAdapter;
import com.example.rocketride.Models.RateModel;
import com.example.rocketride.Models.RateModelFirebaseHandler;
import com.example.rocketride.R;
import com.example.rocketride.Models.RideModel;
import com.example.rocketride.Adapters.SelectDriverListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Arrays;

public class History extends AppCompatActivity implements HistoryRideListener {
    private ArrayList<RideModel> expiredRides = new ArrayList<>();
    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;
    private String UID;

    private RecyclerView recyclerView;
    private HistoryRecyclerViewAdapter adapter;

    private LottieAnimationView notFoundAnimation;
    private TextView notFoundTextView;
    RateModelFirebaseHandler firebaseHandler = new RateModelFirebaseHandler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        // Init datastore reference
        db = FirebaseFirestore.getInstance();

        // Init firebaseAuth reference and global UID
        firebaseAuth = FirebaseAuth.getInstance();
        UID = firebaseAuth.getCurrentUser().getUid();

        // lottie animation
        notFoundAnimation = findViewById(R.id.historyNotFoundAnimation);

        // Text view
        notFoundTextView = findViewById(R.id.historyNotFoundTextView);

        // Remove action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        // Recycle view
        recyclerView = findViewById(R.id.historyRecyclerView);

        // Setup drives history list
        setupUserDrivesHistory();

        // Back arrow button
        ImageView backArrow = findViewById(R.id.leftArrowHistory);
        backArrow.setOnClickListener(l -> {
            // Switch back to HomeActivity activity
            this.finish();
            Intent switchActivityBecomeDriverIntent = new Intent(this, HomeActivity.class);
            startActivity(switchActivityBecomeDriverIntent);
        });
    }

    protected void setupUserDrivesHistory(){
        // TODO: here extract closest rides to current user.
        //       build all related objects afterwards and push them to
        //       the associated array list called - "closeRides".

        expiredRides = getExpiredRides();
//        if(expiredRides.isEmpty()){
//            Toast.makeText(this, "input is empty.", Toast.LENGTH_SHORT).show();
//            // TODO: just for testing - remove it when not needed....
//            expiredRides = new ArrayList<>();
//            for (int i = 0; i < 20; i++) {
//                expiredRides.add(new RideModel("Ariel", "Tel-Aviv", "16/12/2022 0:19", "Ariel-University"));
//            }
//        }
    }

    synchronized public ArrayList<RideModel> getExpiredRides(){
        ArrayList<RideModel> expiredRides = new ArrayList<>();

        CollectionReference collectionReference = db.collection("drives");
        Query query = collectionReference.whereEqualTo("alive", false);

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {

                    // Init seats array
                    String[] seatsArr = {
                            document.getString("near_driver_seat"),
                            document.getString("left_bottom_seat"),
                            document.getString("center_bottom_seat"),
                            document.getString("right_bottom_seat")
                    };

                    // If user is located in one of the drive seats then update list accordingly
                    boolean isUserInSeats = Arrays.asList(seatsArr).contains(UID);
                    if (isUserInSeats){
                        String dateDay = document.get("date-d")
                                + "/" + document.get("date-m")
                                + "/" + document.get("date-y");

                        String startTime = document.get("time_h") + ":" + document.get("time_m");

                        expiredRides.add(new RideModel(
                                document.getString("src_name"),
                                document.getString("dst_name"),
                                dateDay + " " + startTime,
                                document.getString("pickup_name"),
                                document.getString("driver-id"),
                                document.getString("_id"),
                                Boolean.TRUE.equals(document.getBoolean("canceled"))
                        ));
                        System.out.println(expiredRides);
                    }

                    Log.d(TAG, document.getId() + " => " + document.getData());
                }
                if (expiredRides.isEmpty()){
                    notFoundAnimation.setVisibility(View.VISIBLE);
                    notFoundTextView.setVisibility(View.VISIBLE);
                    return;
                }

                // Apply changes to the recycler view
                adapter = new HistoryRecyclerViewAdapter(this, expiredRides, this);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
                notFoundAnimation.setVisibility(View.GONE);
                notFoundTextView.setVisibility(View.GONE);
            } else {
                Log.d(TAG, "Error getting documents: ", task.getException());
            }
        });
        System.out.println("expired list : " + expiredRides);
        return expiredRides;
    }

    protected void showRateDialog(String driverID){
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.layout_history_rate_dialog);

        // Dialog buttons
        Button submitButton = dialog.findViewById(R.id.submitRateButton);

        // Dialog Imageview
        ImageView closeView = dialog.findViewById(R.id.closeImageView);

        // Dialog rating bar
        RatingBar ratingBar = dialog.findViewById(R.id.ratingBar);

        // buttons listeners
        submitButton.setOnClickListener(l -> {
            dialog.dismiss();

            double userRate = ratingBar.getRating();
            System.out.println("user rating is: " + userRate);

            firebaseHandler.getRateModel(driverID, task -> {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        System.out.println(document.getString("driver-id") + " "
                                 + document.get("avg") + " " +
                                document.get("voters_num"));

                        RateModel rateModel = new RateModel(
                                document.getString("driver-id"),
                                (double) document.get("avg"),
                                Math.toIntExact(document.getLong("voters_num"))
                        );

                        // Add current user's vote into average
                        rateModel.addVote(userRate);

                        // Update the RateModel object to the RateModelFirebaseHandler
                        firebaseHandler.updateRateModel(rateModel);
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            });
        });

        closeView.setOnClickListener(l -> dialog.dismiss());

        dialog.show();
    }

    @Override
    public void onItemClicked(RideModel rideModel) {
        System.out.println(rideModel);
        System.out.println(rideModel.getDriverID());
        showRateDialog(rideModel.getDriverID());
    }
}