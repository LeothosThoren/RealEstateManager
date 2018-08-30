package com.openclassrooms.realestatemanager.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.RequestManager;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.views.DetailViewHolder;

import java.util.ArrayList;

public class DetailAdapter extends RecyclerView.Adapter<DetailViewHolder> {

    // TODO see how we can do to use entities
    private ArrayList<String> pictureUrl = new ArrayList<>();
    private ArrayList<String> descriptionText = new ArrayList<>();
    private RequestManager glide;

    public DetailAdapter(ArrayList<String> pictureUrl, ArrayList<String> descriptionText, RequestManager glide) {
        this.pictureUrl = pictureUrl;
        this.descriptionText = descriptionText;
        this.glide = glide;
    }

    @NonNull
    @Override
    public DetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view_item_detail_layout, parent, false);
        return new DetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailViewHolder holder, int position) {
        holder.updateWithDetail(this.pictureUrl.get(position), this.descriptionText.get(position), this.glide);
    }

    @Override
    public int getItemCount() {
        return pictureUrl.size();
    }
}
