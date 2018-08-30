package com.openclassrooms.realestatemanager.controlers.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.controlers.fragments.DetailFragment;

public class DetailActivity extends AppCompatActivity {

    //Constant
    public static final String EXTRA_POSITION = "com.openclassrooms.realestatemanager.controlers.fragments.DetailFragment.EXTRA_POSITION";
    // 1 - Declare detail fragment
    private DetailFragment mDetailFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        this.configureAndShowDetailFragment();
    }

    @Override
    protected void onResume() {
        super.onResume();

        this.updateDataFromDetailFragment();
    }

    // --------------
    // FRAGMENTS
    // --------------

    private void configureAndShowDetailFragment(){
        // A - Get FragmentManager (Support) and Try to find existing instance of fragment in FrameLayout container
        mDetailFragment = (DetailFragment) getSupportFragmentManager().findFragmentById(R.id.frame_layout_detail_activity);

        if (mDetailFragment == null) {
            // B - Create new main fragment
            mDetailFragment = new DetailFragment();
            // C - Add it to FrameLayout container
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.frame_layout_detail_activity, mDetailFragment)
                    .commit();
        }
    }

    // --------------
    // UPDATE UI
    // --------------

    private void updateDataFromDetailFragment() {
        int position = getIntent().getIntExtra(EXTRA_POSITION, 0);
        mDetailFragment.updateTextTest(position);
    }
}
