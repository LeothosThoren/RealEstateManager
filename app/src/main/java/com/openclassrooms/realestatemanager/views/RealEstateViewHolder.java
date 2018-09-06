package com.openclassrooms.realestatemanager.views;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.entities.RealEstate;
import com.openclassrooms.realestatemanager.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RealEstateViewHolder extends RecyclerView.ViewHolder {


    @BindView(R.id.item_picture)
    ImageView mImageViewPicture;
    @BindView(R.id.item_name)
    TextView mTextViewName;
    @BindView(R.id.item_location)
    TextView mTextViewLocation;
    @BindView(R.id.item_price)
    TextView mTextViewPrice;
    @BindView(R.id.real_estate_item_background)
    RelativeLayout mLayout;

    public RealEstateViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void updateWithRealEstate(RealEstate realEstate, RequestManager glide) {

        if (realEstate != null) {

            this.mTextViewName.setText(realEstate.getType());
            this.mTextViewLocation.setText(realEstate.getArea());
            this.mTextViewPrice.setText(Utils.convertPriceToString(realEstate.getPrice()));
            if (realEstate.getPictureUrl() != null) {
                glide.load(realEstate.getPictureUrl())
                        .apply(RequestOptions.centerCropTransform())
                        .into(this.mImageViewPicture);

            }
        }
    }
}
