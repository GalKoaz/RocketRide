package com.example.rocketride.MenuActivities;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.rocketride.Adapters.ActiveDriveListener;
import com.example.rocketride.Adapters.ActiveDrivesActivityViewAdapter;
import com.example.rocketride.R;
import com.example.rocketride.Models.RideModel;
import com.example.rocketride.Ride.ActiveDriveActivity;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class ActiveDrives extends AppCompatActivity implements ActiveDriveListener {
    private ArrayList<RideModel> ActDrives = new ArrayList<>();
    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private String selectedSourcePlace="", selectedDestPlace="", UID;
    private ActiveDrivesActivityViewAdapter adapter;
    private LottieAnimationView notFoundAnimation;
    private TextView notFoundTextView;
    private String userType;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_drives);

        // Get user type
        Bundle extras = getIntent().getExtras();
        userType = "";
        if (extras != null){
            userType = extras.getString("type");
        }

        // Driver's tab layout
        tabLayout = findViewById(R.id.driverActiveDrivesTab);
        if(userType.equals("driver")){
            tabLayout.setVisibility(View.VISIBLE);
        }

        db = FirebaseFirestore.getInstance();

        UID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        // lottie animation
        notFoundAnimation = findViewById(R.id.activeDrivesNotFoundAnimation);

        // Text view
        notFoundTextView = findViewById(R.id.activeDrivesNotFoundTextView);

        // Remove action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        // Recycle view
        recyclerView = findViewById(R.id.ActDriveRecyclerView);

        // Setup drives history list
        setupUserActDrives();

        // Back arrow button
        ImageView backArrow = findViewById(R.id.leftArrowActiveDrives);
        backArrow.setOnClickListener(l -> {
            // Switch back to HomeActivity activity
            this.finish();
            Intent switchActivityBecomeDriverIntent = new Intent(this, HomeActivity.class);
            startActivity(switchActivityBecomeDriverIntent);
        });

        CollectionReference collectionReference = db.collection("drives");


        final int MY_DRIVES = 0,
                  MY_CREATED_DRIVES = 1;

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            final Query query = collectionReference.whereEqualTo("alive", true);
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    // My drives tab listener
                    case MY_DRIVES:
                        System.out.println("my drives tab");
                        getActDrives(query, false);
                        break;
                    // My created drives tab listener
                    case MY_CREATED_DRIVES:
                        System.out.println("my created drives tab");
                        getActDrives(query, true);
                        break;
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

    }

    protected void setupUserActDrives(){
        // TODO: here extract closest rides to current user.
        //       build all related objects afterwards and push them to
        //       the associated array list called - "closeRides".
        CollectionReference collectionReference = db.collection("drives");
        Query query = collectionReference.whereEqualTo("alive", true);
        getActDrives(query, false);
//
//        if(ActDrives.isEmpty()){
//            Toast.makeText(this, "input is empty.", Toast.LENGTH_SHORT).show();
//            // TODO: just for testing - remove it when not needed....
//            for (int i = 0; i < 20; i++) {
//                ActDrives.add(new RideModel("Ariel", "Tel-Aviv", "16/12/2022 0:19", "Ariel-University","678","gfda",false));
//            }
//            adapter = new ActiveDrivesActivityViewAdapter(this, ActDrives, this);
//            recyclerView.setAdapter(adapter);
//            recyclerView.setLayoutManager(new LinearLayoutManager(this));
//            recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
//        }
    }


    @SuppressLint("NotifyDataSetChanged")
    public void getActDrives(Query activeDrivesQuery, boolean isMyCreatedDrive){
        // Clear array list in order to start from a clear perspective
        ActDrives.clear();

        activeDrivesQuery.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String driverID = document.getString("driver-id");

                    // Init seats array
                    String[] seatsArr = {
                            document.getString("near_driver_seat"),
                            document.getString("left_bottom_seat"),
                            document.getString("center_bottom_seat"),
                            document.getString("right_bottom_seat")
                    };
                    System.out.println(Arrays.toString(seatsArr));
                    // If user is located in one of the drive seats then update list accordingly
                    boolean isUserInSeats = Arrays.asList(seatsArr).contains(UID);
                    if ((isUserInSeats && !isMyCreatedDrive) || (driverID.equals(UID) && isMyCreatedDrive)){
                        RideModel rideModel = setRideByDocument(document);
                        // Set car seats
                        rideModel.setCarSeats(seatsArr[0], seatsArr[1], seatsArr[2], seatsArr[3]);
                        ActDrives.add(rideModel);
                    }
                    System.out.println("current document... " + ActDrives.size());
                    Log.d(TAG, document.getId() + " => " + document.getData());
                }
                if (ActDrives.isEmpty()){
                    System.out.println("active drives is empty!");
                    // Apply changes to the recycler view
                    adapter = new ActiveDrivesActivityViewAdapter(this, ActDrives, this);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(this));
                    recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
                    notFoundAnimation.setVisibility(View.VISIBLE);
                    notFoundTextView.setVisibility(View.VISIBLE);
                    return;
                }

                // Apply changes to the recycler view
                adapter = new ActiveDrivesActivityViewAdapter(this, ActDrives, this);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
                notFoundAnimation.setVisibility(View.GONE);
                notFoundTextView.setVisibility(View.GONE);

            } else {
                Log.d(TAG, "Error getting documents: ", task.getException());
            }
        });
    }

    public RideModel setRideByDocument(DocumentSnapshot document){
        String dateDay = document.get("date-d")
                + "/" + document.get("date-m")
                + "/" + document.get("date-y");

        String startTime = document.get("time_h") + ":" + document.get("time_m");

        return new RideModel(
                document.getString("src_name"),
                document.getString("dst_name"),
                dateDay + " " + startTime,
                document.getString("pickup_name"),
                document.getString("driver-id"),
                document.getString("_id"),
                Boolean.TRUE.equals(document.getBoolean("canceled")),
                document.getDouble("price"));
    }

    @Override
    public void onItemClicked(RideModel rideModel) {
        // TODO: move all active driver details with put_extra()
        System.out.println(5);
        // Switch back to active driver activity
        this.finish();
        Intent switchActivityBecomeDriverIntent = new Intent(this, ActiveDriveActivity.class);

        switchActivityBecomeDriverIntent.putExtra("type", userType);
        switchActivityBecomeDriverIntent.putExtra("ride_model", rideModel);
        switchActivityBecomeDriverIntent.putExtra("tab_pos", tabLayout.getSelectedTabPosition());

        startActivity(switchActivityBecomeDriverIntent);
    }
}