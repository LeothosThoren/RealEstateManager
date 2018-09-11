package com.openclassrooms.realestatemanager.controlers.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.adapters.CustomInfoWindowAdapter;
import com.openclassrooms.realestatemanager.base.BaseActivity;
import com.openclassrooms.realestatemanager.entities.RealEstate;
import com.openclassrooms.realestatemanager.injections.Injection;
import com.openclassrooms.realestatemanager.injections.ViewModelFactory;
import com.openclassrooms.realestatemanager.utils.Utils;
import com.openclassrooms.realestatemanager.viewmodels.RealEstateViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

public class MapsActivity extends BaseActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    public static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 10;
    public static final int USER_ID = 1;
    public static final float DEFAULT_ZOOM = 5f;
    private static final String TAG = "MapsActivity";
    //Widget
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    private GoogleMap mMap;
    //VAR
    private boolean mLocationPermissionGranted;
    private Location mLastKnownLocation;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    // Data
    private RealEstateViewModel mViewModel;
    private Marker mMarker;
    private HashMap<String, RealEstate> mMarkerMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.initMap();
        this.configureToolbar();
        this.configureViewModel();
        this.getRealEstateItems(USER_ID);
    }


    @Override
    public int getFragmentLayout() {
        return R.layout.activity_maps;
    }

    @Override
    protected void configureToolbar() {
        setSupportActionBar(mToolbar);
        super.configureToolbar();

    }

    //---------------------
    // Map
    //---------------------

    private void initMap() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        getLocationPermission();
        updateUI();
        mMap.setOnMarkerClickListener(this);
        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(MapsActivity.this));

    }

    //---------------------
    // Permission
    //---------------------

    private void getLocationPermission() {

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions
            , @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                    //Update Ui
                    updateUI();
                }
            }
        }

    }

    //---------------------
    // Location
    //---------------------

    private void getDeviceLocation() {
        Log.d(TAG, "getDeviceLocation: Ok");

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Set the map's camera position to the current location of the device.
                        mLastKnownLocation = task.getResult();
                        if (mLastKnownLocation != null) {
                            // Store device coordinates
                            Double latitude = mLastKnownLocation.getLatitude();
                            Double longitude = mLastKnownLocation.getLongitude();

                            //Move camera toward device position
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(latitude, longitude), DEFAULT_ZOOM));

                        } else {
                            Toast.makeText(this, getString(R.string.message_geolocation),
                                    Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Log.d(TAG, "Current location is null. Using defaults.");
                        Log.e(TAG, "getDeviceLocation => Exception: %s" + task.getException());
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());

        }
    }

    //---------------------
    // Data
    //---------------------

    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(this);
        this.mViewModel = ViewModelProviders.of(this, viewModelFactory).get(RealEstateViewModel.class);
    }

    // Get all items for a user
    private void getRealEstateItems(int userId) {
        this.mViewModel.getRealEstate(userId).observe(this, this::storeData);
        Log.d(TAG, "getRealEstateItems: ");
    }

    private void storeData(List<RealEstate> realEstateList) {

        for (int i = 0; i < realEstateList.size(); i++) {
            String address = realEstateList.get(i).getAddress().line1 + " " + realEstateList.get(i).getAddress().line2
                    + realEstateList.get(i).getAddress().city + " " + realEstateList.get(i).getAddress().state + " "
                    + realEstateList.get(i).getAddress().zip;

            String snippet = Utils.formatSnippet(realEstateList.get(i).getType(), realEstateList.get(i).getArea(),
                    realEstateList.get(i).getPrice(),realEstateList.get(i).getSurface(), realEstateList.get(i).getRoom(),
                    realEstateList.get(i).getBathroom(), realEstateList.get(i).getBedroom());
            geoLocate(address, snippet);


//            mMarkerMap.put(mMarker.getId(), realEstateList.get(i));
        }
    }

    //---------------------
    // Action
    //---------------------

    private void geoLocate(String address, String snippet) {
        Log.d(TAG, "geoLocate: geolocating");

        Geocoder geocoder = new Geocoder(MapsActivity.this);
        List<Address> list = new ArrayList<>();
        try {
            list = geocoder.getFromLocationName(address, 1);
        } catch (IOException e) {
            Log.e(TAG, "geoLocate: IOException: " + e.getMessage());
        }

        if (list.size() > 0) {
            Address re_address = list.get(0);

            Log.d(TAG, "geoLocate: found a location: " + re_address.toString());

            mMarker = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(re_address.getLatitude(), re_address.getLongitude()))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                    .title(re_address.getAddressLine(0))
                    .snippet(snippet));


        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        try {
            if (mMarker.isInfoWindowShown()) {
                mMarker.hideInfoWindow();
            } else {
                mMarker.showInfoWindow();
            }
        } catch (NullPointerException e) {
            Log.e(TAG, "onClick: NullPointerException: " + e.getMessage());
        }
        return false;
    }

    //---------------------
    // Ui
    //---------------------


    private void updateUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
                getDeviceLocation();
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show();
                mLastKnownLocation = null;
                //Try to obtain location permission
                getLocationPermission();
            }
        } catch (SecurityException e) {
            Log.e(TAG, "updateUI: SecurityException " + e.getMessage());
        }
    }

}
