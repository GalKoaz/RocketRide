package com.example.rocketride;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresPermission;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.rocketride.databinding.ActivityMapsDriverBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.HashMap;
import java.util.Map;

public class MapsDriverActivity extends AppCompatActivity implements OnMapReadyCallback , GoogleApiClient.OnConnectionFailedListener
,GoogleApiClient.ConnectionCallbacks, com.google.android.gms.location.LocationListener {

    private GoogleMap mMap;
    private ActivityMapsDriverBinding binding;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    LocationRequest mLoctionRequest;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_driver);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Remove action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        db = FirebaseFirestore.getInstance();

        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);

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

        LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(19));
    }
    protected  synchronized void buildGoogleApiClient(){
        mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        mGoogleApiClient.connect();
    }
    @Override
    public void onLocationChanged(@NonNull Location location) {
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
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("driversAvailable");
        System.out.println(userId);
        GeoFire geoFire = new GeoFire(ref);
        geoFire.removeLocation(userId);
        db.collection("driversAvailable").document(userId).update("driving", false);
    }
}