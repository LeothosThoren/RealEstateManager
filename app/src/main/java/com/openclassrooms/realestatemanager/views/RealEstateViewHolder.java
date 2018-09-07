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
import com.openclassrooms.realestatemanager.utils.App;
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
    @BindView(R.id.item_banner)
    ImageView mBanner;

    public RealEstateViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void updateWithRealEstate(RealEstate realEstate, RequestManager glide) {

        if (realEstate != null) {
            // Banner
            if (realEstate.getSoldDate() != null) {
                this.mBanner.setVisibility(View.VISIBLE);
            }
            // Type
            this.mTextViewName.setText(realEstate.getType());
            // Area
            this.mTextViewLocation.setText(realEstate.getArea());
            // Price
            this.mTextViewPrice.setText(App.getContext().getResources().getString(R.string.item_price,
                    Utils.convertPriceToString(realEstate.getPrice())));
            // Picture
            if (realEstate.getPictureUrl() != null) {
                glide.load(realEstate.getPictureUrl())
                        .apply(RequestOptions.centerCropTransform())
                        .into(this.mImageViewPicture);

            }
        }
    }
}
