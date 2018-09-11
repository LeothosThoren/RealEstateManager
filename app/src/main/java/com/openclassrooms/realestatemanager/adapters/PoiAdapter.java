package com.openclassrooms.realestatemanager.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.views.PoiViewHolder;

import java.util.ArrayList;
import java.util.List;

public class PoiAdapter extends RecyclerView.Adapter<PoiViewHolder> {

    //Var
    private List<String> poiList;

    public PoiAdapter() {
        this.poiList = new ArrayList<>();
    }

    @NonNull
    @Override
    public PoiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view_item_poi, parent, false);
        return new PoiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PoiViewHolder holder, int position) {
        holder.poiTextView.setText(poiList.get(position));
    }

    @Override
    public int getItemCount() {
        return poiList.size() > 0 ? poiList.size() : 0;
    }

    public String getPoiValue(int position) {
        return this.poiList.get(position);
    }

    public void updateData(List<String> poiList) {
        this.poiList = poiList;
        this.notifyDataSetChanged();
    }
}
