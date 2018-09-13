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
import java.util.List;

public class DetailAdapter extends RecyclerView.Adapter<DetailViewHolder> {

    private List<String> pictureUrl;
    private List<String> titleDescripton;
    private RequestManager glide;

    public DetailAdapter(RequestManager glide) {
        this.pictureUrl = new ArrayList<>();;
        this.titleDescripton = new ArrayList<>();;
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
        holder.updateWithDetail(this.pictureUrl.get(position), this.titleDescripton.get(position), this.glide);
    }

    @Override
    public int getItemCount() {
        return pictureUrl.size();
    }

    public void updateData(List<String> pictures, List<String> title) {
        this.pictureUrl = pictures;
        this.titleDescripton = title;
        this.notifyDataSetChanged();
    }
}
