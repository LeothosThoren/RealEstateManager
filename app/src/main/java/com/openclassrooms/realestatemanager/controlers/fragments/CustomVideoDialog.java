package com.openclassrooms.realestatemanager.controlers.fragments;


import android.arch.lifecycle.ViewModelProviders;
import android.arch.persistence.room.Update;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.entities.RealEstate;
import com.openclassrooms.realestatemanager.injections.Injection;
import com.openclassrooms.realestatemanager.injections.ViewModelFactory;
import com.openclassrooms.realestatemanager.utils.HelperSingleton;
import com.openclassrooms.realestatemanager.viewmodels.RealEstateViewModel;

import java.util.List;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class CustomVideoDialog extends DialogFragment {

    private static final String TAG = "CustomVideoDialog";
    public static final int USER_ID = 1;
    //Widget
    @BindView(R.id.video_view)
    VideoView mVideoView;
    String videoPath;
    //Var
    private RealEstateViewModel mViewModel;
    private int dataPosition = HelperSingleton.getInstance().getPosition();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_custom_video_dialog, container, false);

        this.init();
        return view;
    }


    private void init() {
        getDialog().setTitle("Video");
        this.configureViewModel();
        this.getRealEstateItems(USER_ID);
//        this.displayVideo();
    }

    private void displayVideo() {
//        Uri uri = Uri.parse(videoPath);
        mVideoView.setVideoPath(videoPath);
        //Add media controller on video view
        MediaController mediaController = new MediaController(getContext());
        mVideoView.setMediaController(mediaController);
        mediaController.setAnchorView(mVideoView);

    }

    //---------------------
    // Data
    //---------------------

    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(getContext());
        this.mViewModel = ViewModelProviders.of(this, viewModelFactory).get(RealEstateViewModel.class);
    }

    // Get all items for a user
    private void getRealEstateItems(int userId) {
        this.mViewModel.getRealEstate(userId).observe(this, this::getVideoUri);
        Log.d(TAG, "getRealEstateItems: ");
    }

    private void getVideoUri(List<RealEstate> rel) {
        videoPath = rel.get(dataPosition).getUrlVideo();
        Log.d(TAG, "getVideoUri: "+ videoPath);
    }

}
