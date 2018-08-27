package com.openclassrooms.realestatemanager.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.RequestManager;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.entities.RealEstate;
import com.openclassrooms.realestatemanager.views.RealEstateViewHolder;

import java.util.ArrayList;
import java.util.List;

public class RealEstateAdapter extends RecyclerView.Adapter<RealEstateViewHolder> {

    // VAR
    private List<RealEstate> realEstateList;
    private RequestManager glide;

    public RealEstateAdapter(RequestManager glide) {
        this.realEstateList = new ArrayList<>();
        this.glide = glide;
    }

    @NonNull
    @Override
    public RealEstateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.fragment_real_estate, parent, false);
        return new RealEstateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RealEstateViewHolder viewHolder, int position) {
        viewHolder.updateWithRealEstate(this.realEstateList.get(position), this.glide);
    }

    @Override
    public int getItemCount() {
        return realEstateList.size();
    }

    public RealEstate getRealEstate(int position) {
        return this.realEstateList.get(position);
    }

    public void updateData(List<RealEstate> realEstates) {
        this.realEstateList = realEstates;
        this.notifyDataSetChanged();
    }
}
