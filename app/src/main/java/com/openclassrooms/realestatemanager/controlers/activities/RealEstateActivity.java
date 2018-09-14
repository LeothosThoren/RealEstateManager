package com.openclassrooms.realestatemanager.controlers.activities;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.base.BaseActivity;
import com.openclassrooms.realestatemanager.controlers.fragments.CustomDialogForm;
import com.openclassrooms.realestatemanager.controlers.fragments.CustomSearchDialog;
import com.openclassrooms.realestatemanager.controlers.fragments.DetailFragment;
import com.openclassrooms.realestatemanager.controlers.fragments.RealEstateFragment;
import com.openclassrooms.realestatemanager.entities.RealEstate;
import com.openclassrooms.realestatemanager.utils.HelperSingleton;

import butterknife.BindView;

public class RealEstateActivity extends BaseActivity implements RealEstateFragment.OnItemClickListenerCustom {


    public static final String FRAGMENT_FORM_TAG = "CustomDialogForm";
    public static final String FRAGMENT_SEARCH_TAG = "CustomSearchDialog";
    private static final String TAG = RealEstateActivity.class.getSimpleName();
    // WIDGET
    @BindView(R.id.toolbar)
    android.support.v7.widget.Toolbar mToolbar;
    // VAR
    private RealEstateFragment mRealEstateFragment;
    private DetailFragment mDetailFragment;
    //DATA

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Configure and show home fragment
        this.configureToolbar();
        this.configureAndShowRealEstateFragment();
        this.configureAndShowDetailFragment();
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

    /*...*/

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
        customDialogForm.setStyle(DialogFragment.STYLE_NORMAL, R.style.Dialog_FullScreen);
        customDialogForm.show(getSupportFragmentManager(), FRAGMENT_FORM_TAG);
    }


    private void openCustomSearchDialog() {
        CustomSearchDialog customSearchDialog = new CustomSearchDialog();
//        customSearchDialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.Dialog_FullScreen);
        customSearchDialog.show(getSupportFragmentManager(), FRAGMENT_SEARCH_TAG);
    }
}
