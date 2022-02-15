package com.example.fa_sravansriramoju_c0828149_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.widget.TextView;

import com.example.fa_sravansriramoju_c0828149_android.Model.Place;
import com.example.fa_sravansriramoju_c0828149_android.databinding.ActivityDisplayMapBinding;
import com.example.fa_sravansriramoju_c0828149_android.utilities.DBHelper;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.SphericalUtil;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;

public class PlaceDetailsActivity extends AppCompatActivity implements OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback {

    TextView placeName, address, distance, duration;
    GoogleMap gMap;
    private double latitude;
    private double longitude;
    SupportMapFragment mapFragment;
    Place place;
    private static final DecimalFormat df = new DecimalFormat("0.00");


    private FusedLocationProviderClient client;
    Marker marker;
    LocationRequest locationRequest;
    LocationCallback locationCallback;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_details);

        placeName = findViewById(R.id.placenamevalue);
        address = findViewById(R.id.addressvalue);
        distance = findViewById(R.id.distancevalue);
        duration = findViewById(R.id.durationvalue);

        place = getIntent().getParcelableExtra("place");

        placeName.setText(place.getTitle());
        address.setText(place.getAddress());
        latitude = place.getLatitude();
        longitude = place.getLongitude();

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.maps);
        mapFragment.getMapAsync(this);

        getUserLocation();
        if (!isGrantedLocationPer()) {
            requestLocationPermission();

        } else {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            client.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
        }

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        gMap = googleMap;
        LatLng placeLocation = new LatLng(latitude, longitude);
        gMap.addMarker(new MarkerOptions()
                .position(placeLocation)
                .title(place.getTitle()));
        gMap.moveCamera(CameraUpdateFactory.newLatLng(placeLocation));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setUserMark();
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                client.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());

            }
        }
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
    }

    private boolean isGrantedLocationPer() {
        int permissionState = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void getUserLocation() {
        client = LocationServices.getFusedLocationProviderClient(this);
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setSmallestDisplacement(10);
        setUserMark();

    }

    private void setUserMark() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                for (Location location : locationResult.getLocations()) {
                    LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                    if (marker != null)
                        marker.remove();
                    LatLngBounds bounds = new LatLngBounds(new LatLng(location.getLatitude(), location.getLongitude()), new LatLng(place.getLatitude(), place.getLongitude()));
                    CameraPosition cameraPosition = CameraPosition.builder()
                            .target(userLocation)
                            .zoom(10)
                            .bearing(0)
                            .tilt(45)
                            .build();
                    gMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    gMap.addMarker(new MarkerOptions().position(userLocation)
                            .title("Your Location")
                            .draggable(true)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                            .snippet("Home")
                    );
                    df.setRoundingMode(RoundingMode.DOWN);
                    distance.setText(String.valueOf(df.format(SphericalUtil.computeDistanceBetween(new LatLng(location.getLatitude(),location.getLongitude()), new LatLng(place.getLatitude(),place.getLongitude()))/1000)) + " " + "KMS");
                }
            }

        };
    }
}