package com.openclassrooms.realestatemanager.controlers.fragments;


import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.adapters.DetailAdapter;
import com.openclassrooms.realestatemanager.adapters.RealEstateAdapter;
import com.openclassrooms.realestatemanager.base.BaseFragment;
import com.openclassrooms.realestatemanager.entities.RealEstate;
import com.openclassrooms.realestatemanager.injections.Injection;
import com.openclassrooms.realestatemanager.injections.ViewModelFactory;
import com.openclassrooms.realestatemanager.viewmodels.RealEstateViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.support.constraint.Constraints.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment {

    private static final String TAG = "DetailFragment";
    //WIDGET
    @BindView(R.id.recycler_view_fragment)
    RecyclerView mDetailRecyclerView;
    @BindView(R.id.description_content)TextView mTextViewDescription;
    //VAR
    private DetailAdapter mDetailAdapter;
    private ArrayList<String> pictureUrl = new ArrayList<>();
    private ArrayList<String> descriptionText = new ArrayList<>();
    private RealEstateViewModel mRealEstateViewModel;
    public static int USER_ID = 1; // ASk
    public int position;

    public DetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the recycler_view_item_layout for this fragment
        View detailView = inflater.inflate(R.layout.fragment_detail, container, false);
        ButterKnife.bind(this, detailView);

        //methods
        pictureUrl.add("https://frama.link/jMuusLXd");
        descriptionText.add("No description");
        configureViewModel();
        getRealEstateItems(USER_ID);
        configureRecyclerView();


        return detailView;
    }

    // RecyclerView
    private void configureRecyclerView() {
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        this.mDetailRecyclerView.setLayoutManager(layoutManager);
        this.mDetailAdapter = new DetailAdapter(pictureUrl, descriptionText, Glide.with(this));
        this.mDetailRecyclerView.setAdapter(this.mDetailAdapter);
    }

    // -------------------------------------------------------------------------------------------//
    //                                         DATA                                               //
    // -------------------------------------------------------------------------------------------//

    // Configure ViewModel
    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(getContext());
        this.mRealEstateViewModel = ViewModelProviders.of(this, viewModelFactory).get(RealEstateViewModel.class);
    }

    // 3 - Get all items for a user
    private void getRealEstateItems(int userId) {
        this.mRealEstateViewModel.getRealEstate(userId).observe(this, this::updateDataFromAList);
        Log.d(TAG, "getRealEstateItems: ");
    }


    // -------------------------------------------------------------------------------------------//
    //                                          UI                                                //
    // -------------------------------------------------------------------------------------------//

    private void updateDataFromAList(List<RealEstate> realEstateList) {
        mTextViewDescription.setText(realEstateList.get(position).getArea());
    }

    public void updateViewOnTablet(RealEstate realEstate) {
        mTextViewDescription.setText(realEstate.getArea());
    }
}
