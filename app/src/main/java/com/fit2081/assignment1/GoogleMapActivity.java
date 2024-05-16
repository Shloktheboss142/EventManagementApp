package com.fit2081.assignment1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.fit2081.assignment1.databinding.ActivityGoogleMapBinding;

import java.io.IOException;
import java.util.List;

public class GoogleMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityGoogleMapBinding binding;
    private Geocoder geocoder;
    private String defaultLocation = "Monash University Malaysia";
    String categoryLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivityGoogleMapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        categoryLocation = getIntent().getExtras().getString("categoryLocation");
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
        geocoder = new Geocoder(this);

        // Convert default location name into coordinates
        List<Address> defaultAddress = null;
        try {
            defaultAddress = geocoder.getFromLocationName(defaultLocation, 1);
        } catch (IOException ignored) {}

        // Get the default location coordinates
        Address defaultAddressDetails = defaultAddress.get(0);
        LatLng defaultLatLng = new LatLng(defaultAddressDetails.getLatitude(), defaultAddressDetails.getLongitude());

        try {
            // Convert location name into coordinates
            List<Address> addresses = geocoder.getFromLocationName(categoryLocation, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

                // Add marker on the map
                mMap.addMarker(new MarkerOptions().position(latLng).title(categoryLocation));

                // Move camera to the marker
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10f));
            } else {
                Toast.makeText(this, "Category address not found", Toast.LENGTH_SHORT).show();

                // Add marker on the map for default location
                mMap.addMarker(new MarkerOptions().position(defaultLatLng).title(defaultLocation));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLatLng, 10f));
            }
        } catch (IOException e) {
            //Make a new toast
            Toast.makeText(this, "Category address not found", Toast.LENGTH_SHORT).show();

            // Add marker on the map for default location
            mMap.addMarker(new MarkerOptions().position(defaultLatLng).title(defaultLocation));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLatLng, 10f));

            e.printStackTrace();
        }

        // Set a listener for the map
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener(){

            // When the map is clicked
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                String country = "There is no country at this location";
                try {

                    // Get the country of the location and display it in a toast
                    List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                    if (addresses != null && !addresses.isEmpty()) {
                        country = "Country: ";
                        country += addresses.get(0).getCountryName();
                    }

                    Toast.makeText(GoogleMapActivity.this, country, Toast.LENGTH_SHORT).show();

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    // Method to handle the back arrow being pressed on the toolbar
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }

        return true;
    }
}