package com.example.rocketride.MenuActivities;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rocketride.Ride.CreateDriveActivity;
import com.example.rocketride.MainActivity;
import com.example.rocketride.R;
import com.example.rocketride.Ride.RideSearchActivity;
import com.firebase.geofire.GeoFire;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity implements OnMapReadyCallback , GoogleApiClient.OnConnectionFailedListener
,GoogleApiClient.ConnectionCallbacks, com.google.android.gms.location.LocationListener, NavigationView.OnNavigationItemSelectedListener {
    private final static String SAVE_STATE_KEY = "save_state";
    private Bundle extras;

    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    LocationRequest mLoctionRequest;

    private FirebaseFirestore db;
    private String userType, userProfileImageLink, userFullName;
    private byte[] profileImageBytesArr;

    // Google sign-in client
    private GoogleSignInClient mGoogleSignInClient;

    private FirebaseAuth firebaseAuth;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_driver);

        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);

        firebaseAuth = FirebaseAuth.getInstance();
        System.out.println("home user: " + firebaseAuth.getCurrentUser());

        // Init firebase storage to download profile image
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageRef = firebaseStorage.getReference();

        userType = ""; userProfileImageLink = ""; userFullName = "";

        // Check just in case that the bundle is null
        extras = getIntent().getExtras();
        if (extras != null) {
            // Get information from MainActivity of the current user
            userType = extras.getString("type", "");
            userProfileImageLink = extras.getString("profile_image_link", "");
            userFullName = extras.getString("full_name", "");
            System.out.println("maps activity: " + userProfileImageLink + " " + userType);
        }
        else{
            getDataState();
        }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Set the navigation view
        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);

        // Set user views according to his type (default is rider)
        if (userType.equals("driver")){
            // Set visibility of become a driver menu item to true
            navigationView.getMenu().findItem(R.id.menuBecomeDriver).setVisible(false);

            // Set join ride button visibility to gone
            findViewById(R.id.search_drive).setVisibility(View.GONE);

            // Set create ride button visibility to visible
            findViewById(R.id.create_drive).setVisibility(View.VISIBLE);
        }

        // Set user's full name
        TextView fullNameTextView = navigationView.getHeaderView(0).findViewById(R.id.fullNameMenuTextView);
        fullNameTextView.setText(userFullName);

        // Check if the current user image link isn't empty.
        // If it isn't empty change the profile image in side menu bar
        RoundedImageView profileImageView = navigationView.getHeaderView(0).findViewById(R.id.profileRoundedView);
        if (!userProfileImageLink.equals("")) {
            if (profileImageBytesArr != null) {
                setImageView(profileImageView, profileImageBytesArr);
            }
            else {
                downloadImageToView(userProfileImageLink, storageRef, profileImageView);
            }
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Remove action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        db = FirebaseFirestore.getInstance();

        // Buttons
        Button create_button = findViewById(R.id.create_drive);
        Button search_button = findViewById(R.id.search_drive);

        create_button.setOnClickListener(signUp -> {
            this.finish();
            Intent switchActivityIntent = new Intent(this, CreateDriveActivity.class);
            switchActivityIntent.putExtra("message", "From: " + MainActivity.class.getSimpleName());
            startActivity(switchActivityIntent);
        });

        search_button.setOnClickListener(signUp -> {
            this.finish();
            Intent switchActivityIntent = new Intent(this, RideSearchActivity.class);
            switchActivityIntent.putExtra("message", "From: " + MainActivity.class.getSimpleName());
            startActivity(switchActivityIntent);
        });

        // ImageViews
        ImageView sideMenuBar = findViewById(R.id.sideMenuBar);

        sideMenuBar.setOnClickListener(l -> {
            System.out.println("Side bar pressed");
            drawerLayout.openDrawer(GravityCompat.START);
        });

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            return;
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            return;
        buildGoogleApiClient();
        mMap.setMyLocationEnabled(true);
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, true);
        Location location = locationManager.getLastKnownLocation(provider);

        if (location == null){
            System.out.println("location is null in HomeActivity");
            return;
        }
        LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(19));
    }
    protected synchronized void buildGoogleApiClient(){
        mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        mGoogleApiClient.connect();
    }
    @Override
    public void onLocationChanged(@NonNull Location location) {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            System.out.println("current user is null - location change event");
            return;
        }

        mLastLocation = location;

        LatLng latlng = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(19));

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("id", userId);
        userMap.put("Longitude",  location.getLongitude());
        userMap.put("Latitude", location.getLatitude());
        userMap.put("driving",true);
        db.collection("driversAvailable").document(userId).set(userMap);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        int time_to_refresh = 30;
        mLoctionRequest = new LocationRequest();
        mLoctionRequest.setInterval(time_to_refresh*1000);
        mLoctionRequest.setFastestInterval(time_to_refresh*1000);
        mLoctionRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            return;
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLoctionRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onStop() {
        super.onStop();

        if (FirebaseAuth.getInstance().getCurrentUser() == null){
            System.out.println("current user is null!");
            return;
        }

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("driversAvailable");
        System.out.println(userId);
        GeoFire geoFire = new GeoFire(ref);
        geoFire.removeLocation(userId);
        db.collection("driversAvailable").document(userId).update("driving", false);
    }

    protected void saveDataState() {
        sharedPreferences = getSharedPreferences("my_prefs", MODE_PRIVATE);

        // get an editor for the Shared Preferences object
        SharedPreferences.Editor editor = sharedPreferences.edit();

        String encoded = Base64.encodeToString(profileImageBytesArr, Base64.DEFAULT);
        System.out.println("picture arrat: " + encoded);

        editor.putString("profile_image", encoded)
                .putString("type", userType)
                .putString("full_name", userFullName)
                .putString("profile_image_link", userProfileImageLink);
        editor.apply();
    }

    protected void getDataState(){
        sharedPreferences = getSharedPreferences("my_prefs", MODE_PRIVATE);

        String encoded = sharedPreferences.getString("profile_image", null);

        // Decode the string back into an array of bytes
        profileImageBytesArr = Base64.decode(encoded, Base64.DEFAULT);

        userType = sharedPreferences.getString("type", "");
        userFullName = sharedPreferences.getString("full_name", "");
        userProfileImageLink = sharedPreferences.getString("profile_image_link", "");
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuHome:
                Toast.makeText(this, "menuHome", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menuHistory:
                Toast.makeText(this, "menuHistory", Toast.LENGTH_SHORT).show();

                // Switch to the history activity
                //moveTaskToBack(false);
                this.finish();
                Intent switchActivityHistoryIntent = new Intent(this, History.class);
                startActivity(switchActivityHistoryIntent);
                break;
            case R.id.menuActiveDrives:
                Toast.makeText(this, "menuActiveDrives", Toast.LENGTH_SHORT).show();

                // Switch to the active drives activity
                //moveTaskToBack(false);
                this.finish();
                Intent switchActivityActiveDrivesIntent = new Intent(this, ActiveDrives.class);
                switchActivityActiveDrivesIntent.putExtra("type", userType);
                startActivity(switchActivityActiveDrivesIntent);
                break;
            case R.id.menuBecomeDriver:
                Toast.makeText(this, "menuBecomeDriver", Toast.LENGTH_SHORT).show();

                // Switch to the become a driver activity
                //moveTaskToBack(false);
                this.finish();
                Intent switchActivityBecomeDriverIntent = new Intent(this, BecomeDriver.class);
                startActivity(switchActivityBecomeDriverIntent);
                break;
            case R.id.menuSearchRide:
                Toast.makeText(this, "menuSearchRide", Toast.LENGTH_SHORT).show();

                // Switch to the ride search activity
                //moveTaskToBack(false);
                this.finish();
                Intent switchActivitySearchRideIntent = new Intent(this, RideSearchActivity.class);
                startActivity(switchActivitySearchRideIntent);
                break;

            case R.id.menuLogout:
                Toast.makeText(this, "menuLogout", Toast.LENGTH_SHORT).show();
                showLogoutDialog();
                break;
        }
        return true;
    }


    protected void showLogoutDialog(){
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.layout_logout_dialog);

        // Dialog buttons
        Button acceptButton = dialog.findViewById(R.id.acceptLogoutButton);
        Button cancelButton = dialog.findViewById(R.id.cancelLogoutButton);

        // buttons listeners
        acceptButton.setOnClickListener(l -> {
            dialog.dismiss();

            Log.d(TAG, "onStop fired ..............");
            signOut();
            // Log.d(TAG, "isConnected ...............: " + mGoogleApiClient.isConnected());

            // Switch to sign in activity
            this.finish();
            Intent switchActivityIntent = new Intent(this, MainActivity.class);
            switchActivityIntent.putExtra("ViewFlag", false);
            startActivity(switchActivityIntent);
        });

        cancelButton.setOnClickListener(l -> dialog.dismiss());

        dialog.show();
    }

    protected void signOut() {
        // Firebase sign out
        firebaseAuth.signOut();

        // Google sign out
        mGoogleSignInClient.signOut();

        System.out.println("disconnect: " + mGoogleApiClient);
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
        mMap.clear();
        System.out.println("disconnect: " + mGoogleApiClient);
    }

    public void downloadImageToView(String imageURL, StorageReference storageRef, RoundedImageView imageView){
        storageRef.child(imageURL).getBytes(Long.MAX_VALUE).addOnSuccessListener(imageBytes -> {
            this.profileImageBytesArr = imageBytes;
            saveDataState();
            setImageView(imageView, imageBytes);
        }).addOnFailureListener(exception -> Log.d(TAG, "error in downloading the image!"));
    }

    public void setImageView(RoundedImageView imageView, byte[] profileImageBytesArr){
        // Use the bytes to display the image
        // Convert the byte array into a Bitmap object
        Bitmap bitmap = BitmapFactory.decodeByteArray(profileImageBytesArr, 0, profileImageBytesArr.length);

        // Set the Bitmap as the image for the ImageView
        imageView.setImageBitmap(bitmap);
    }
}