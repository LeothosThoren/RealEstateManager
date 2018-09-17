package com.openclassrooms.realestatemanager.controlers.activities;

import android.app.DialogFragment;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.base.BaseActivity;
import com.openclassrooms.realestatemanager.controlers.fragments.CustomDialogForm;
import com.openclassrooms.realestatemanager.controlers.fragments.CustomSearchDialog;
import com.openclassrooms.realestatemanager.controlers.fragments.DetailFragment;
import com.openclassrooms.realestatemanager.controlers.fragments.RealEstateFragment;
import com.openclassrooms.realestatemanager.entities.RealEstate;
import com.openclassrooms.realestatemanager.entities.User;
import com.openclassrooms.realestatemanager.injections.Injection;
import com.openclassrooms.realestatemanager.injections.ViewModelFactory;
import com.openclassrooms.realestatemanager.utils.HelperSingleton;
import com.openclassrooms.realestatemanager.viewmodels.RealEstateViewModel;

import butterknife.BindView;

public class RealEstateActivity extends BaseActivity implements RealEstateFragment.OnItemClickListenerCustom,
        NavigationView.OnNavigationItemSelectedListener{

    public static final String FRAGMENT_FORM_TAG = "CustomDialogForm";
    public static final String FRAGMENT_SEARCH_TAG = "CustomSearchDialog";
    public static final int USER_ID = 1;
    private static final String TAG = RealEstateActivity.class.getSimpleName();
    // WIDGET
    @BindView(R.id.toolbar)
    android.support.v7.widget.Toolbar mToolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.nav_view)
    NavigationView mNavigationView;
    private TextView mTextViewUser;
    private ImageView mImageViewProfile;
    // VAR
    private RealEstateFragment mRealEstateFragment;
    private DetailFragment mDetailFragment;
    //DATA
    private RealEstateViewModel mRealEstateViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Configure and show home fragment
        this.configureToolbar();
        this.configureDrawerLayout();
        this.configureNavigationView();
        this.configureNavHeader();
        this.configureAndShowRealEstateFragment();
        this.configureAndShowDetailFragment();
        this.configureViewModel();
        this.getCurrentUser(USER_ID);
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
                HelperSingleton.getInstance().setMode(id);
                this.openCustomDialog();
                //indicate we want to create
                Log.d(TAG, "onOptionsItemSelected: test create id = " + id + " vs  R.id.menu_add " + R.id.menu_add);
                break;
            case R.id.menu_update:
                // indicate the custom fragment we want to update
                HelperSingleton.getInstance().setIsVisible(true);
                HelperSingleton.getInstance().setMode(id);
                RealEstateFragment.mAdapter.notifyDataSetChanged();
                Log.d(TAG, "onOptionsItemSelected: Test update id = " + id + " vs  R.id.menu_update " + R.id.menu_update);
                break;
            case R.id.menu_search:
                this.openCustomSearchDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    // --------------
    // MENU DRAWER
    // --------------

    //Handle the clickHandler on MENU DRAWER
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle Navigation Item Click
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_drawer_map:
                this.launchMapActivity();
                break;
            case R.id.nav_drawer_settings:
                Toast.makeText(this, "No settings for now...", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_drawer_logout:
                Toast.makeText(this, "Can't logout for now...", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        this.mDrawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    //Handle menu drawer on back press button
    @Override
    public void onBackPressed() {
        // Handle back clickHandler to close menu
        if (this.mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    // Configure Drawer Layout
    private void configureDrawerLayout() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    // Configure NavigationView
    private void configureNavigationView() {
        this.mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);

    }

    private void configureNavHeader() {
        // Handle navigation header items
        View headView = mNavigationView.getHeaderView(0);
        mTextViewUser = (TextView) headView.findViewById(R.id.menu_drawer_username);
        mImageViewProfile = (ImageView) headView.findViewById(R.id.menu_drawer_picture);
    }

    // --------------
    // UI
    // --------------

    // Configuring ViewModel
    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(this);
        this.mRealEstateViewModel = ViewModelProviders.of(this, viewModelFactory).get(RealEstateViewModel.class);
        this.mRealEstateViewModel.init(USER_ID);
    }

    // Get Current User
    private void getCurrentUser(int userId) {
        this.mRealEstateViewModel
                .getUser(userId).observe(this, this::updateHeader);
    }

    // Update header (username & picture)
    private void updateHeader(User user) {
        this.mTextViewUser.setText(user.getUsername());
        Glide.with(this).load(user.getUrlPicture())
                .apply(RequestOptions.circleCropTransform()).into(this.mImageViewProfile);
    }

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
    public void onItemClickListenerCustom(View view, int position, RealEstate realEstate) {
        Log.d(TAG, "onItemClickListenerCustom: ok & position : " + position);
        if (mDetailFragment != null && mDetailFragment.isVisible()) {
            mDetailFragment.updateViewOnTablet(realEstate);
        } else {
            Intent i = new Intent(this, DetailActivity.class);
            i.putExtra(DetailActivity.EXTRA_POSITION, position);
            startActivity(i);
        }
    }


    // --------------
    // Action
    // --------------

    /**
     * @method openCustomDialog
     * Create and open custom dialog by applying full screen style
     */
    private void openCustomDialog() {
        CustomDialogForm customDialogForm = new CustomDialogForm();
        customDialogForm.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.Dialog_FullScreen);
        customDialogForm.show(getSupportFragmentManager(), FRAGMENT_FORM_TAG);
    }


    private void openCustomSearchDialog() {
        CustomSearchDialog customSearchDialog = new CustomSearchDialog();
        customSearchDialog.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.Dialog_FullScreen);
        customSearchDialog.show(getSupportFragmentManager(), FRAGMENT_SEARCH_TAG);
    }

    private void launchMapActivity() {
        Intent i = new Intent(this, MapsActivity.class);
        startActivity(i);
    }


}
