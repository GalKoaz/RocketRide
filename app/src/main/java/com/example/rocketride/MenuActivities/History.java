package com.example.rocketride.MenuActivities;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.rocketride.Models.DriverRideModel;
import com.example.rocketride.Adapters.HistoryRecyclerViewAdapter;
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

public class History extends AppCompatActivity implements SelectDriverListener {
    private ArrayList<RideModel> expiredRides = new ArrayList<>();
    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;
    private String UID;

    private RecyclerView recyclerView;
    private HistoryRecyclerViewAdapter adapter;

    private LottieAnimationView notFoundAnimation;
    private TextView notFoundTextView;

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

    @Override
    public void onItemClicked(DriverRideModel driverRideModel) {

    }
}