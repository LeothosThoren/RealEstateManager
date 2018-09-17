package com.openclassrooms.realestatemanager.controlers.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.base.BaseActivity;
import com.openclassrooms.realestatemanager.entities.RealEstate;
import com.openclassrooms.realestatemanager.injections.Injection;
import com.openclassrooms.realestatemanager.injections.ViewModelFactory;
import com.openclassrooms.realestatemanager.utils.HelperSingleton;
import com.openclassrooms.realestatemanager.viewmodels.RealEstateViewModel;

import java.util.List;

import butterknife.BindView;

public class VideoActivity extends BaseActivity {

    public static final int USER_ID = 1;
    //Widget
    @BindView(R.id.video_view)
    VideoView mVideoView;
    //Var
    private RealEstateViewModel mViewModel;
    private int dataPosition = HelperSingleton.getInstance().getPosition();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.init();
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.activity_video;
    }

    private void init() {
        this.configureViewModel();
        this.getRealEstateItems(USER_ID);
    }

    private void displayVideo(String path) {
        Uri uri = Uri.parse(path);
        mVideoView.setVideoURI(uri);
        //Add media controller on video view
        mVideoView.start();
        MediaController mediaController = new MediaController(this);
        mVideoView.setMediaController(mediaController);
        mediaController.setAnchorView(mVideoView);
    }

    //---------------------
    // Data
    //---------------------

    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(this);
        this.mViewModel = ViewModelProviders.of(this, viewModelFactory).get(RealEstateViewModel.class);
    }

    // Get all items for a user
    private void getRealEstateItems(int userId) {
        this.mViewModel.getRealEstate(userId).observe(this, this::getVideoUri);
    }

    private void getVideoUri(List<RealEstate> rel) {
        String videoPath = rel.get(dataPosition).getUrlVideo();
        this.displayVideo(videoPath);
    }
}
