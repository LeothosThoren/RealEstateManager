package com.openclassrooms.realestatemanager.controlers.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.base.BaseActivity;
import com.openclassrooms.realestatemanager.controlers.fragments.DetailFragment;
import com.openclassrooms.realestatemanager.controlers.fragments.RealEstateFragment;
import com.openclassrooms.realestatemanager.entities.RealEstate;
import com.openclassrooms.realestatemanager.injections.Injection;
import com.openclassrooms.realestatemanager.injections.ViewModelFactory;
import com.openclassrooms.realestatemanager.viewmodels.RealEstateViewModel;

import butterknife.BindView;

public class RealEstateActivity extends BaseActivity implements RealEstateFragment.OnButtonClickedListener, RealEstateFragment.OnItemClickListenerCustom {


    private static final String TAG = RealEstateActivity.class.getSimpleName();
    // WIDGET
    @BindView(R.id.toolbar)
    android.support.v7.widget.Toolbar mToolbar;

    // VAR
    private RealEstateFragment mRealEstateFragment;
    private DetailFragment mDetailFragment;

    private RealEstateViewModel mRealEstateViewModel; //ask
    public static int USER_ID = 1; // ASk


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Configure and show home fragment
        this.configureToolbar();
        this.configureAndShowRealEstateFragment();
        this.configureAndShowDetailFragment();
        this.configureViewModel();
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.activity_real_estate;
    }


    // --------------
    // TOOLBAR
    // --------------

    //Configure toolbar
    public void configureToolbar() {
        setSupportActionBar(this.mToolbar);
    }

    // Inflate the menu and add it to the Toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    // Configure click on menu Toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_add:
                createRealEstateItem();
                Toast.makeText(this, "Test add", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_update:
                Toast.makeText(this, "Test update", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_search:
                Toast.makeText(this, "Test search", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    // --------------
    // MENU DRAWER
    // --------------




    // --------------
    // FRAGMENTS
    // --------------

    private void configureAndShowRealEstateFragment() {
        // Get FragmentManager (Support) and Try to find existing instance of fragment in FrameLayout container
        mRealEstateFragment = (RealEstateFragment) getSupportFragmentManager()
                .findFragmentById(R.id.frame_layout_real_estate_activity);

        if (mRealEstateFragment == null) {
            // Create new main fragment
            mRealEstateFragment = new RealEstateFragment();
            // Add it to FrameLayout container
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.frame_layout_real_estate_activity, mRealEstateFragment)
                    .commit();
        }
    }

    private void configureAndShowDetailFragment() {
        mDetailFragment = (DetailFragment) getSupportFragmentManager().findFragmentById(R.id.frame_layout_detail_activity);

        // We only add DetailFragment in Tablet mode (If found frame_layout_detail)
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
        Log.e(TAG, "Button clicked !");
        if (mDetailFragment == null || !mDetailFragment.isVisible()) {
            startActivity(new Intent(this, DetailActivity.class));
        }
    }

    @Override
    public void onItemClickListenerCustom(View view, int position) {
        Log.e(TAG, "onItemClickListenerCustom: ok & position : "+ position );
        if (mDetailFragment != null && mDetailFragment.isVisible()) {
            mDetailFragment.updateTextTest(position);
        } else {
            Intent i = new Intent(this, DetailActivity.class);
            i.putExtra(DetailActivity.EXTRA_POSITION, position);
            startActivity(i);
        }
    }

    // --------------
    // TEST DB
    // --------------

    // Configure ViewModel
    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(this);
        this.mRealEstateViewModel = ViewModelProviders.of(this, viewModelFactory).get(RealEstateViewModel.class);
        this.mRealEstateViewModel.init(USER_ID);
    }

    private void createRealEstateItem() {
        RealEstate realEstate = new RealEstate("Flat", "Brooklyn", 1206532, USER_ID);
        this.mRealEstateViewModel.createRealEstate(realEstate);
    }


}
