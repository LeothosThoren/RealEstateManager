package com.openclassrooms.realestatemanager.controlers.fragments;


import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.adapters.DetailAdapter;
import com.openclassrooms.realestatemanager.entities.RealEstate;
import com.openclassrooms.realestatemanager.injections.Injection;
import com.openclassrooms.realestatemanager.injections.ViewModelFactory;
import com.openclassrooms.realestatemanager.utils.HelperSingleton;
import com.openclassrooms.realestatemanager.viewmodels.RealEstateViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pub.devrel.easypermissions.EasyPermissions;

import static android.app.Activity.RESULT_OK;


public class CustomCarouselDialog extends DialogFragment implements View.OnClickListener {

    private static final String TAG = "CustomCarouselDialog";
    private static final String PERMS = Manifest.permission.READ_EXTERNAL_STORAGE;
    private static final int RC_IMAGE_PERMS = 100;
    private static final int RC_CHOOSE_PHOTO = 200;
    OnInputsSelected mOnInputsSelected;
    //Widget
    @BindView(R.id.action_cancel)
    TextView mActionCancel;
    @BindView(R.id.action_save)
    TextView mActionSave;
    @BindView(R.id.recycler_view_carousel)
    RecyclerView mRecyclerView;
    @BindView(R.id.carousel_button_add)
    ImageView mCarouselButtonAdd;
    @BindView(R.id.carousel_select_picture)
    ImageView mCarouselPictureSelection;
    @BindView(R.id.carousel_edit_text)
    EditText mEditText;
    //Var
    private int dataPosition = HelperSingleton.getInstance().getPosition();
    private boolean isCreateMode = HelperSingleton.getInstance().getMode() == R.id.menu_add;
    private boolean isUpdateMode = HelperSingleton.getInstance().getMode() == R.id.menu_update;
    private DetailAdapter mAdapter;
    //Data
    private List<String> titleList = new ArrayList<>();
    private List<String> pictureList = new ArrayList<>();
    private List<RealEstate> mRealEstateList = new ArrayList<>();
    private RealEstateViewModel mViewModel;
    private Uri uriImageSelected;


    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_custom_carousel_dialog, container, false);
        ButterKnife.bind(this, view);

        this.init();
        return view;
    }

    // ----------------
    // init
    // ----------------

    private void init() {
        this.configureViewModel();
        this.configureRecyclerView();
        mActionCancel.setOnClickListener(this);
        mActionSave.setOnClickListener(this);
        mCarouselButtonAdd.setOnClickListener(this);
        mCarouselPictureSelection.setOnClickListener(this);
    }

    // ----------------
    // Permission
    // ----------------

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Calling the appropriate method after activity result
        this.handleResponse(requestCode, resultCode, data);
    }


    private void handleResponse(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_CHOOSE_PHOTO) {
            if (resultCode == RESULT_OK) { //SUCCESS
                this.uriImageSelected = data.getData();
            } else {
                Toast.makeText(getContext(), getString(R.string.toast_title_no_image_chosen), Toast.LENGTH_SHORT).show();
            }
        }
    }

    // ----------------
    // Config
    // ----------------

    // RecyclerView
    private void configureRecyclerView() {
        this.mAdapter = new DetailAdapter(Glide.with(this));
        this.mRecyclerView.setAdapter(this.mAdapter);
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        this.configureClickWithRecyclerView();

    }

    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(getContext());
        this.mViewModel = ViewModelProviders.of(this, viewModelFactory).get(RealEstateViewModel.class);
    }

    // --------------
    // Action
    // --------------

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.carousel_select_picture:
                this.selectPictureOnDevice();
                break;
            case R.id.carousel_button_add:
                this.addPictureAndTitleInList();
                break;
            case R.id.action_cancel:
                getDialog().cancel();
                break;
            case R.id.action_save:
                this.saveData();
                break;
        }
    }

    private void addPictureAndTitleInList() {
        if (mEditText.getText() != null || uriImageSelected != null) {
            String title = mEditText.getText().toString();
            String url = uriImageSelected.toString();
            pictureList.add(url);
            titleList.add(title);
            mAdapter.notifyDataSetChanged();
            Log.d(TAG, "configureClickAction: find title = " + title + " url = " + url);
        } else {
            Toast.makeText(getContext(), "The title or the picture is missing!", Toast.LENGTH_SHORT).show();
        }

    }

    private void selectPictureOnDevice() {
        if (getContext() != null)
            if (!EasyPermissions.hasPermissions(getContext(), PERMS)) {
                EasyPermissions.requestPermissions(this, getString(R.string.popup_title_permission_files_access), RC_IMAGE_PERMS, PERMS);
                return;
            }
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RC_CHOOSE_PHOTO);
    }


    private void saveData() {
        if (pictureList != null && titleList != null) {
            mOnInputsSelected.sendBothInputs(pictureList, titleList);
            Toast.makeText(getContext(), "Elements added!", Toast.LENGTH_SHORT).show();
            getDialog().dismiss();
        } else {
            Toast.makeText(getContext(), "Please add at least one item row ", Toast.LENGTH_SHORT).show();
        }
    }

    //--------------
    // Interface
    // -------------

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mOnInputsSelected = (CustomCarouselDialog.OnInputsSelected) getTargetFragment();
        } catch (ClassCastException e) {
            Log.e(TAG, "onAttach: ClassCastException : " + e.getMessage());
        }
    }

    //Interface
    public interface OnInputsSelected {
        void sendBothInputs(List<String> picture, List<String> title);
    }
}
