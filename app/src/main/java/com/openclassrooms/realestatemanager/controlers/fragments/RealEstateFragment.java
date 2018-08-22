package com.openclassrooms.realestatemanager.controlers.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.adapters.RealEstateAdapter;
import com.openclassrooms.realestatemanager.models.RealEstate;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class RealEstateFragment extends Fragment implements View.OnClickListener {

    // WIDGET
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    //DATA
    private List<RealEstate> mRealEstateList;
    //VAR
    private RealEstateAdapter mAdapter;

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
        return view;
    }

    // -------------------------------------------------------------------------------------------//
    //                                    CONFIGURATION                                           //
    // -------------------------------------------------------------------------------------------//

    // RecyclerView
    private void configureRecyclerView() {
        this.mRealEstateList = new ArrayList<>();
        this.mAdapter = new RealEstateAdapter(this.mRealEstateList, Glide.with(this));
        this.mRecyclerView.setAdapter(this.mAdapter);
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

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




}
