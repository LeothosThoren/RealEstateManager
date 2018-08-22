package com.openclassrooms.realestatemanager.controlers.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.controlers.fragments.DetailFragment;
import com.openclassrooms.realestatemanager.controlers.fragments.RealEstateFragment;

public class RealEstateActivity extends AppCompatActivity implements RealEstateFragment.OnButtonClickedListener {

    // Declare fragment
    private RealEstateFragment mRealEstateFragment;
    private DetailFragment mDetailFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_estate);

        // Configure and show home fragment
        this.configureAndShowRealEstateFragment();
        this.configureAndShowDetailFragment();
    }

    // --------------
    // FRAGMENTS
    // --------------

    private void configureAndShowRealEstateFragment() {
        // A - Get FragmentManager (Support) and Try to find existing instance of fragment in FrameLayout container
        mRealEstateFragment = (RealEstateFragment) getSupportFragmentManager()
                .findFragmentById(R.id.frame_layout_real_estate_activity);

        if (mRealEstateFragment == null) {
            // B - Create new main fragment
            mRealEstateFragment = new RealEstateFragment();
            // C - Add it to FrameLayout container
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.frame_layout_real_estate_activity, mRealEstateFragment)
                    .commit();
        }
    }

    private void configureAndShowDetailFragment() {
        mDetailFragment = (DetailFragment) getSupportFragmentManager().findFragmentById(R.id.frame_layout_detail_activity);

        //A - We only add DetailFragment in Tablet mode (If found frame_layout_detail)
        if (mDetailFragment == null && findViewById(R.id.frame_layout_detail_activity) != null) {
            mDetailFragment = new DetailFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.frame_layout_detail_activity, mDetailFragment)
                    .commit();
        }
    }

    // --------------
    // CallBack
    // --------------

    @Override
    public void onButtonClicked(View view) {
        Log.e(getClass().getSimpleName(), "Button clicked !");
        if (mDetailFragment == null || !mDetailFragment.isVisible()) {
            startActivity(new Intent(this, DetailActivity.class));
        }
    }
}
