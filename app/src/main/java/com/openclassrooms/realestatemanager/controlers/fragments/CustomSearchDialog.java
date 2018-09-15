package com.openclassrooms.realestatemanager.controlers.fragments;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.entities.RealEstate;
import com.openclassrooms.realestatemanager.injections.Injection;
import com.openclassrooms.realestatemanager.injections.ViewModelFactory;
import com.openclassrooms.realestatemanager.utils.Utils;
import com.openclassrooms.realestatemanager.viewmodels.RealEstateViewModel;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class CustomSearchDialog extends DialogFragment {

    public static final int USER_ID = 1;
    private static final String TAG = "CustomSearchDialog";
    //Widget
    @BindView(R.id.search_area)
    EditText mArea;
    @BindView(R.id.search_type)
    EditText mType;
    @BindView(R.id.search_surface_min)
    EditText mSurfaceMin;
    @BindView(R.id.search_surface_max)
    EditText mSurfaceMax;
    @BindView(R.id.search_price_min)
    EditText mPriceMin;
    @BindView(R.id.search_price_max)
    EditText mPriceMax;
    @BindView(R.id.search_room_min)
    EditText mRoomMin;
    @BindView(R.id.search_room_max)
    EditText mRoomMax;
    @BindView(R.id.search_button_cancel)
    ImageButton mCancel;
    @BindView(R.id.search_button_search)
    ImageButton mSearch;
    //Data
    private RealEstateViewModel mViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_custom_search_dialog, container, false);
        ButterKnife.bind(this, view);

        this.init();
        return view;
    }

    private void init() {
        //methods
        this.configureViewModel();
        this.getRealEstateItems(USER_ID);
        getDialog().setTitle("Search real estates");
        mCancel.setOnClickListener(v -> getDialog().cancel());
        mSearch.setOnClickListener(v -> this.startQuery());
    }


    // --------------
    // UI
    // --------------

    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(getContext());
        this.mViewModel = ViewModelProviders.of(this, viewModelFactory).get(RealEstateViewModel.class);
    }

    // Get all items for a user
    private void getRealEstateItems(int userId) {
        this.mViewModel.getRealEstate(userId).observe(this, this::updateRealEstateItemsList);
    }

    // Update the list of Real Estate item
    private void updateRealEstateItemsList(List<RealEstate> realEstates) {
        RealEstateFragment.mAdapter.updateData(realEstates);
        Log.d(TAG, "search features real estate size: "+ realEstates.size());
    }

    // --------------
    // Action
    // --------------

    private void startQuery() {
        String type = !mType.getText().toString().equals("") ? mType.getText().toString() : "%";
        String area = !mArea.getText().toString().equals("") ? mArea.getText().toString() : "%";
        String surfaceMin = !mSurfaceMin.getText().toString().equals("") ? mSurfaceMin.getText().toString() : "0";
        String surfaceMax = !mSurfaceMax.getText().toString().equals("") ? mSurfaceMax.getText().toString() : "100000";
        String priceMin = !mPriceMin.getText().toString().equals("") ? mPriceMin.getText().toString() : "0";
        String priceMax = !mPriceMax.getText().toString().equals("") ? mPriceMax.getText().toString() : "999999999999";
        String roomMin = !mRoomMin.getText().toString().equals("") ? mRoomMin.getText().toString() : "0";
        String roomMax = !mRoomMax.getText().toString().equals("") ? mRoomMax.getText().toString() : "100";

        this.mViewModel.searchRealEstate(area, Integer.valueOf(surfaceMin), Integer.valueOf(surfaceMax), Long.valueOf(priceMin), Long.valueOf(priceMax),
                Integer.valueOf(roomMin), Integer.valueOf(roomMax), USER_ID)
                .observe(this, this::updateRealEstateItemsList);

        getDialog().dismiss();
    }

}
