package com.openclassrooms.realestatemanager.views;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.adapters.RealEstateAdapter;
import com.openclassrooms.realestatemanager.entities.RealEstate;
import com.openclassrooms.realestatemanager.utils.App;
import com.openclassrooms.realestatemanager.utils.HelperSingleton;
import com.openclassrooms.realestatemanager.utils.Utils;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RealEstateViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


    @BindView(R.id.item_picture)
    public ImageView mImageViewPicture;
    @BindView(R.id.item_name)
    public TextView mTextViewName;
    @BindView(R.id.item_location)
    public TextView mTextViewLocation;
    @BindView(R.id.item_price)
    public TextView mTextViewPrice;
    @BindView(R.id.real_estate_item_background)
    public RelativeLayout mLayout;
    @BindView(R.id.item_banner)
    public ImageView mBanner;
    @BindView(R.id.item_radio_button)
    public RadioButton mRadioButtonToCheck;
    private WeakReference<RealEstateAdapter.Listener> callbackWeakRef;

    public RealEstateViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);

    }

    public void updateWithRealEstate(RealEstate realEstate, RequestManager glide, RealEstateAdapter.Listener callback, int lastSelectedPosition) {

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
            mRadioButtonToCheck.setVisibility(HelperSingleton.getInstance().getViewVisibility());
            //Click on checkBox
            this.mRadioButtonToCheck.setOnClickListener(this);
            this.callbackWeakRef = new WeakReference<>(callback);
            this.mRadioButtonToCheck.setChecked(lastSelectedPosition == getAdapterPosition());

        }
    }

    @Override
    public void onClick(View v) {
        RealEstateAdapter.Listener callback = callbackWeakRef.get();
        if (callback != null) callback.onClickCheckButton(getAdapterPosition());
    }
}
