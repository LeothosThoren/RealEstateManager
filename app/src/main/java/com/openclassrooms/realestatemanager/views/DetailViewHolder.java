package com.openclassrooms.realestatemanager.views;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.openclassrooms.realestatemanager.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Optional;

public class DetailViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.item_detail_image)
    ImageView mImageView;
    @BindView(R.id.item_detail_description_text)
    TextView mTextView;

    public DetailViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @Optional
    public void updateWithDetail(String picture, String description, RequestManager glide) {
        if (picture != null) {
            Uri uri = Uri.parse(picture);
            glide.load(uri).apply(RequestOptions.centerCropTransform()).into(this.mImageView);
        }
        if (description != null) {
            mTextView.setText(description);
        }
    }
}
