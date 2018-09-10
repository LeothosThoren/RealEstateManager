package com.openclassrooms.realestatemanager.controlers.activities;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.base.BaseActivity;

import butterknife.BindView;

public class MapsActivity extends BaseActivity implements OnMapReadyCallback {

    //Widget
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.initMap();
        this.configureToolbar();
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

    private void initMap() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng paris = new LatLng(48.858093, 2.294694);
        mMap.addMarker(new MarkerOptions().position(paris).title("Marker in Paris"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(paris));
    }
}
