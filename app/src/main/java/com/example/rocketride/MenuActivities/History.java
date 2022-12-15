package com.example.rocketride.MenuActivities;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.rocketride.DriverRideModel;
import com.example.rocketride.DriverRideRecyclerViewAdapter;
import com.example.rocketride.HistoryRecyclerViewAdapter;
import com.example.rocketride.MapsDriverActivity;
import com.example.rocketride.R;
import com.example.rocketride.RideModel;
import com.example.rocketride.SelectDriverListener;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        // Init datastore reference
        db = FirebaseFirestore.getInstance();

        // Init firebaseAuth reference and global UID
        firebaseAuth = FirebaseAuth.getInstance();
        UID = firebaseAuth.getCurrentUser().getUid();

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
            // Switch back to MapsDriverActivity activity
            this.finish();
            Intent switchActivityBecomeDriverIntent = new Intent(this, MapsDriverActivity.class);
            startActivity(switchActivityBecomeDriverIntent);
        });
    }

    protected void setupUserDrivesHistory(){
        // TODO: here extract closest rides to current user.
        //       build all related objects afterwards and push them to
        //       the associated array list called - "closeRides".
        if(expiredRides.isEmpty()){
            Toast.makeText(this, "input is empty.", Toast.LENGTH_SHORT).show();
            // TODO: just for testing - remove it when not needed....
            for (int i = 0; i < 20; i++) {
                expiredRides.add(new RideModel("Ariel", "Tel-Aviv", "16/12/2022 0:19", "Ariel-University"));
            }
            adapter = new HistoryRecyclerViewAdapter(this, expiredRides, this);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        }
        else{
            //getAliveRides();
        }
    }

    public ArrayList<RideModel> getExpiredRides(){
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
                        expiredRides.add(new RideModel(
                                document.getString("source"),
                                document.getString("destination"),
                                document.getString("date"),
                                document.getString("pickup_name")
                                ));
                    }

                    Log.d(TAG, document.getId() + " => " + document.getData());
                }
            } else {
                Log.d(TAG, "Error getting documents: ", task.getException());
            }
        });
        return expiredRides;
    }

    @Override
    public void onItemClicked(DriverRideModel driverRideModel) {

    }
}