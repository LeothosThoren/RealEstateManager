package com.openclassrooms.realestatemanager.controlers.fragments;


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
import com.openclassrooms.realestatemanager.adapters.DetailAdapter;
import com.openclassrooms.realestatemanager.adapters.RealEstateAdapter;
import com.openclassrooms.realestatemanager.base.BaseFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment {

    //WIDGET
    @BindView(R.id.recycler_view_fragment)
    RecyclerView mDetailRecyclerView;
    //VAR
    private DetailAdapter mDetailAdapter;
    private ArrayList<String> pictureUrl = new ArrayList<>();
    private ArrayList<String> descriptionText = new ArrayList<>();

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
        pictureUrl.add("http://google.fr/");
        descriptionText.add("No description");
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


    //Test
    public void updateTextTest(int position) {
        // find a way to get datas (see in the data base ?)
    }
}
