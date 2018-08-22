package com.openclassrooms.realestatemanager.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.RequestManager;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.models.Immovable;
import com.openclassrooms.realestatemanager.views.ImmovableViewHolder;

import java.util.List;

public class ImmovableAdapter extends RecyclerView.Adapter<ImmovableViewHolder> {

    // VAR
    private List<Immovable> immovableList;
    private RequestManager glide;

    public ImmovableAdapter(List<Immovable> immovableList, RequestManager glide) {
        this.immovableList = immovableList;
        this.glide = glide;
    }

    @NonNull
    @Override
    public ImmovableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.fragment_real_estate, parent, false);
        return new ImmovableViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImmovableViewHolder viewHolder, int position) {
        viewHolder.updateWithImmovable(this.immovableList.get(position), this.glide);
    }

    @Override
    public int getItemCount() {
        return immovableList.size();
    }

    public Immovable getImmovable(int position) {
        return this.immovableList.get(position);
    }

    public void updateData(List<Immovable> immovables) {
        this.immovableList = immovables;
        this.notifyDataSetChanged();
    }
}
