package com.openclassrooms.realestatemanager.controlers.fragments;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.adapters.RealEstateAdapter;
import com.openclassrooms.realestatemanager.entities.RealEstate;
import com.openclassrooms.realestatemanager.injections.Injection;
import com.openclassrooms.realestatemanager.injections.ViewModelFactory;
import com.openclassrooms.realestatemanager.utils.Utils;
import com.openclassrooms.realestatemanager.viewmodels.RealEstateViewModel;

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

    private static final String TAG = "CustomSearchDialog";
    public static final int USER_ID = 1;
    //Widget
    @BindView(R.id.search_area)
    EditText mArea;
    @BindView(R.id.search_type)
    EditText mType;
    @BindView(R.id.search_date_min)
    EditText mDateMin;
    @BindView(R.id.search_date_max)
    EditText mDateMax;
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
    private List<RealEstate> mRealEstateList = new ArrayList<>();




    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_custom_search_dialog, container, false);
        ButterKnife.bind(this, view);
        //methods
        this.configureViewModel();
        getDialog().setTitle("Search properties");
        mCancel.setOnClickListener(v -> getDialog().cancel());
        mSearch.setOnClickListener(v -> this.startQuery());
        return view;
    }

    // --------------
    // UI
    // --------------


    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(getContext());
        this.mViewModel = ViewModelProviders.of(this, viewModelFactory).get(RealEstateViewModel.class);
    }

    private void startQuery() {
        String type = !mType.getText().toString().equals("") ? mType.getText().toString() : "Apartment";
        String area = !mArea.getText().toString().equals("") ? mArea.getText().toString() : "%";
        Log.d(TAG, "startQuery: show area = "+ area);
//        Date dateMin = !mDateMin.getText().toString().equals("") ? Utils.getDateFromString(mDateMin.getText().toString()) : Utils.getDateFromString("01/01/1990");
//        Date dateMax = !mDateMax.getText().toString().equals("") ? Utils.getDateFromString(mDateMax.getText().toString()) : Calendar.getInstance().getTime();
        String surfaceMin = !mSurfaceMin.getText().toString().equals("") ? mSurfaceMin.getText().toString() : "0";
        String surfaceMax = !mSurfaceMax.getText().toString().equals("") ? mSurfaceMax.getText().toString() : "10000";
        String priceMin = !mPriceMin.getText().toString().equals("") ? mPriceMin.getText().toString() : "0";
        String priceMax = !mPriceMax.getText().toString().equals("") ? mPriceMax.getText().toString() : "9999999999";
        String roomMin = !mRoomMin.getText().toString().equals("") ? mRoomMin.getText().toString() : "0";
        String roomMax = !mRoomMax.getText().toString().equals("") ? mRoomMax.getText().toString() : "50";

        this.searchRealEstate(area, Integer.valueOf(surfaceMin), Integer.valueOf(surfaceMax), Long.valueOf(priceMin), Long.valueOf(priceMax),
                Integer.valueOf(roomMin), Integer.valueOf(roomMax), Calendar.getInstance().getTime(), Calendar.getInstance().getTime(), USER_ID);
        getDialog().dismiss();
    }


    private void searchRealEstate(String area, int minSurface, int maxSurface, long minPrice, long maxPrice,
                                  int minRoom, int maxRoom, Date minDate, Date maxDate, long userId) {
        this.mViewModel.searchRealEstate(area, minSurface, maxSurface, minPrice, maxPrice,
                minRoom, maxRoom, minDate, maxDate, userId).observeForever(realEstateList -> RealEstateFragment.mAdapter.updateData(realEstateList));
    }

}
