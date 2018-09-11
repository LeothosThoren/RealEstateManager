package com.openclassrooms.realestatemanager.views;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.openclassrooms.realestatemanager.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PoiViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.item_poi_text)
    public TextView poiTextView;

    public PoiViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);

    }
}
