package com.openclassrooms.realestatemanager.controlers.fragments;


import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.adapters.RealEstateAdapter;
import com.openclassrooms.realestatemanager.entities.RealEstate;
import com.openclassrooms.realestatemanager.injections.Injection;
import com.openclassrooms.realestatemanager.injections.ViewModelFactory;
import com.openclassrooms.realestatemanager.utils.ItemClickSupport;
import com.openclassrooms.realestatemanager.viewmodels.RealEstateViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class RealEstateFragment extends Fragment implements View.OnClickListener {

    //CONSTANT
    public static int USER_ID = 1; // ASk
    //WIDGET
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    //DATA
    private List<RealEstate> mRealEstateList;
    private RealEstateAdapter mAdapter;
    private RealEstateViewModel mRealEstateViewModel; //ask

    // Declare callback
    private OnButtonClickedListener mCallback;

    // Declare our interface that will be implemented by any container activity
    public interface OnButtonClickedListener {
        public void onButtonClicked(View view);
    }

    public RealEstateFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the recycler_view_item_layout for this fragment
        View view = inflater.inflate(R.layout.fragment_real_estate, container, false);
        ButterKnife.bind(this, view);
        //Handle click
        view.findViewById(R.id.fragment_main_button).setOnClickListener(this);
        // Call for new methods
        this.configureRecyclerView();
        this.configureViewModel();
        this.getRealEstateItems(USER_ID);
        return view;
    }

    // -------------------------------------------------------------------------------------------//
    //                                    CONFIGURATION                                           //
    // -------------------------------------------------------------------------------------------//


    // Create callback to parent activity
    private void createCallbackToParentActivity() {
        try {
            //Parent activity will automatically subscribe to callback
            mCallback = (OnButtonClickedListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(e.toString() + " must implement OnButtonClickedListener");
        }
    }

    // -------------------------------------------------------------------------------------------//
    //                                         DATA                                               //
    // -------------------------------------------------------------------------------------------//


    // Configure ViewModel
    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(getContext());
        this.mRealEstateViewModel = ViewModelProviders.of(this, viewModelFactory).get(RealEstateViewModel.class);
        this.mRealEstateViewModel.init(USER_ID);
    }

//    private void getCurrentUser(int userId){
//        this.mRealEstateViewModel.getUser(userId).observe(this, this::updateHeader);
//    }

    // 3 - Get all items for a user
    private void getRealEstateItems(int userId){
        this.mRealEstateViewModel.getRealEstate(userId).observe(this, this::updateRealEstateItemsList);
    }


    // -------------------------------------------------------------------------------------------//
    //                                       ACTION                                               //
    // -------------------------------------------------------------------------------------------//

    @Override
    public void onClick(View view) {
        mCallback.onButtonClicked(view);
    }

    // -------------------------------------------------------------------------------------------//
    //                                         UI                                                 //
    // -------------------------------------------------------------------------------------------//


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Call the method that creating callback after being attached to parent activity
        this.createCallbackToParentActivity();
    }

    // RecyclerView
    private void configureRecyclerView() {
        this.mAdapter = new RealEstateAdapter(Glide.with(this));
        this.mRecyclerView.setAdapter(this.mAdapter);
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        ItemClickSupport.addTo(mRecyclerView, R.layout.recycler_view_item_layout)
                .setOnItemClickListener((recyclerView1, position, v) -> {
                    // Action to do here
                    Toast.makeText(getContext(), "Click on position :"+ position, Toast.LENGTH_SHORT).show();
                });
    }

    // 6 - Update the list of Real Estate item
    private void updateRealEstateItemsList(List<RealEstate> realEstates){
        this.mAdapter.updateData(realEstates);
    }

}
