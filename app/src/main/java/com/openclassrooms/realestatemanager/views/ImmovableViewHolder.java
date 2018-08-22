package com.openclassrooms.realestatemanager.views;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.models.Immovable;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImmovableViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.item_picture)
    ImageView mImageViewPicture;
    @BindView(R.id.item_name)
    TextView mTextViewName;
    @BindView(R.id.item_location)
    TextView mTextViewLocation;
    @BindView(R.id.item_price)
    TextView mTextViewPrice;

    public ImmovableViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void updateWithImmovable(Immovable immovable, RequestManager glide) {

        if (immovable != null) {
            this.mTextViewName.setText(immovable.getType());
            this.mTextViewLocation.setText(immovable.getArea());
            this.mTextViewPrice.setText(immovable.getPrice());
            if (immovable.getPictureUrl() != null) {
                glide.load(immovable.getPictureUrl())
                        .apply(RequestOptions.centerCropTransform())
                        .into(this.mImageViewPicture);
            }
        }

    }
}
