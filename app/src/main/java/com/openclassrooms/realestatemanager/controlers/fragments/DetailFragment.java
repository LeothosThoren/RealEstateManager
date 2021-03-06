package com.openclassrooms.realestatemanager.controlers.fragments;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.openclassrooms.realestatemanager.BuildConfig;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.adapters.DetailAdapter;
import com.openclassrooms.realestatemanager.controlers.activities.MapsActivity;
import com.openclassrooms.realestatemanager.controlers.activities.VideoActivity;
import com.openclassrooms.realestatemanager.entities.RealEstate;
import com.openclassrooms.realestatemanager.injections.Injection;
import com.openclassrooms.realestatemanager.injections.ViewModelFactory;
import com.openclassrooms.realestatemanager.utils.Utils;
import com.openclassrooms.realestatemanager.viewmodels.RealEstateViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment {

    private static final String TAG = "DetailFragment";
    public static int USER_ID = 1; // ASk
    public int position;
    //WIDGET
    @BindView(R.id.recycler_view_fragment)
    RecyclerView mDetailRecyclerView;
    @BindView(R.id.detail_surface_quantity)
    TextView mSurfaceQty;
    @BindView(R.id.detail_rooms_quantity)
    TextView mRoomsQty;
    @BindView(R.id.detail_bathrooms_quantity)
    TextView mBathroomsQty;
    @BindView(R.id.detail_bedroom_quantity)
    TextView mBedroomsQty;
    @BindView(R.id.detail_address_line1_num)
    TextView mAddress;
    @BindView(R.id.detail_line2)
    TextView mLine2;
    @BindView(R.id.detail_city)
    TextView mCity;
    @BindView(R.id.detail_state)
    TextView mState;
    @BindView(R.id.detail_zip_code)
    TextView mZipCode;
    @BindView(R.id.detail_description)
    TextView mTextViewDescription;
    @BindView(R.id.detail_map_view)
    ImageView mMapView;
    @BindView(R.id.textView)
    TextView mShowVideo;
    //VAR
    private DetailAdapter mDetailAdapter;
    private RealEstateViewModel mRealEstateViewModel;
    private String mApiKey = "&key=";
    private String mApiUri = "https://maps.googleapis.com/maps/api/staticmap?size=300x300&scale=2&zoom=16&markers=size:mid%7Ccolor:blue%7C";

    public DetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the recycler_view_item_layout for this fragment
        View detailView = inflater.inflate(R.layout.fragment_detail, container, false);
        ButterKnife.bind(this, detailView);
        this.init();

        return detailView;
    }


    // ------------
    // DATA
    // ------------

    private void init() {
        //methods
        this.configureViewModel();
        this.getRealEstateItems(USER_ID);
        this.configureRecyclerView();
        this.mMapView.setOnClickListener(v -> this.launchMapActivity());
        this.mShowVideo.setOnClickListener(v -> this.launchVideoActivity());

    }

    // Configure ViewModel
    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(getContext());
        this.mRealEstateViewModel = ViewModelProviders.of(this, viewModelFactory).get(RealEstateViewModel.class);
    }

    // ------------
    // UI
    // ------------

    // Get all items for a user
    private void getRealEstateItems(int userId) {
        this.mRealEstateViewModel.getRealEstate(userId).observe(this, this::updateViewOnMobile);
        Log.d(TAG, "getRealEstateItems: ");
    }

    // RecyclerView
    private void configureRecyclerView() {
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        this.mDetailRecyclerView.setLayoutManager(layoutManager);
        this.mDetailAdapter = new DetailAdapter(Glide.with(this));
        this.mDetailRecyclerView.setAdapter(this.mDetailAdapter);
    }

    private void updateViewOnMobile(List<RealEstate> realEstateList) {
        if (realEstateList.size() > 0) {
            mTextViewDescription.setText(realEstateList.get(position).getDescription());
            mSurfaceQty.setText(getString(R.string.surface_size, realEstateList.get(position).getSurface()));
            mRoomsQty.setText(getString(R.string.room_quantity, realEstateList.get(position).getRoom()));
            mBathroomsQty.setText(getString(R.string.bathroom_quantity, realEstateList.get(position).getBathroom()));
            mBedroomsQty.setText(getString(R.string.bedroom_quantity, realEstateList.get(position).getBedroom()));
            mAddress.setText(getString(R.string.address, realEstateList.get(position).getAddress().line1));

            if (realEstateList.get(position).getAddress().line2 != null
                    && (!realEstateList.get(position).getAddress().line2.equals(""))) {
                mLine2.setText(realEstateList.get(position).getAddress().line2);
                mLine2.setVisibility(View.VISIBLE);
            }
            mCity.setText(realEstateList.get(position).getAddress().city);
            mState.setText(realEstateList.get(position).getAddress().state);
            mZipCode.setText(realEstateList.get(position).getAddress().zip);
            Glide.with(this)
                    .load(mApiUri + Utils.formatAddress(
                            realEstateList.get(position).getAddress().line1,
                            realEstateList.get(position).getAddress().city,
                            realEstateList.get(position).getAddress().state,
                            realEstateList.get(position).getAddress().zip) + mApiKey + getString(R.string.google_maps_key))
                    .apply(RequestOptions.centerCropTransform())
                    .into(mMapView);
            mDetailAdapter.updateData(realEstateList.get(position).getPictureUrl(), realEstateList.get(position).getTitle());
        }

    }

    // ------------
    // ACTION
    // ------------

    public void updateViewOnTablet(RealEstate realEstate) {
        if (realEstate != null) {
            mTextViewDescription.setText(realEstate.getDescription());
            mSurfaceQty.setText(getString(R.string.surface_size, realEstate.getSurface()));
            mRoomsQty.setText(getString(R.string.room_quantity, realEstate.getRoom()));
            mBathroomsQty.setText(getString(R.string.bathroom_quantity, realEstate.getBathroom()));
            mBedroomsQty.setText(getString(R.string.bedroom_quantity, realEstate.getBedroom()));
            mAddress.setText(getString(R.string.address, realEstate.getAddress().line1));
            if (realEstate.getAddress().line2 != null && (!realEstate.getAddress().line2.equals(""))) {
                mLine2.setText(realEstate.getAddress().line2);
                mLine2.setVisibility(View.VISIBLE);
            }
            mCity.setText(realEstate.getAddress().city);
            mState.setText(realEstate.getAddress().state);
            mZipCode.setText(realEstate.getAddress().zip);

            Glide.with(this)
                    .load(mApiUri + Utils.formatAddress(
                            realEstate.getAddress().line1,
                            realEstate.getAddress().city,
                            realEstate.getAddress().state,
                            realEstate.getAddress().zip) + mApiKey + getString(R.string.google_maps_key))
                    .apply(RequestOptions.centerCropTransform())
                    .into(mMapView);
            mDetailAdapter.updateData(realEstate.getPictureUrl(), realEstate.getTitle());
        }

    }

    private void launchMapActivity() {
        Intent i = new Intent(getContext(), MapsActivity.class);
        startActivity(i);
    }

    private void launchVideoActivity() {
        Intent i = new Intent(getContext(), VideoActivity.class);
        startActivity(i);
    }

}
