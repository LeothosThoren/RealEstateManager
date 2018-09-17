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
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.adapters.DetailAdapter;
import com.openclassrooms.realestatemanager.entities.RealEstate;
import com.openclassrooms.realestatemanager.injections.Injection;
import com.openclassrooms.realestatemanager.injections.ViewModelFactory;
import com.openclassrooms.realestatemanager.utils.HelperSingleton;
import com.openclassrooms.realestatemanager.viewmodels.RealEstateViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import pub.devrel.easypermissions.EasyPermissions;

import static android.app.Activity.RESULT_OK;


public class CustomCarouselDialog extends DialogFragment implements View.OnClickListener {

    public static final int USER_ID = 1;
    private static final String TAG = "CustomCarouselDialog";
    private static final String PERMS = Manifest.permission.READ_EXTERNAL_STORAGE;
    private static final int RC_IMAGE_PERMS = 100;
    private static final int RC_CHOOSE_PHOTO = 200;
    //Interface
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
    private List<String> mTitleList = new ArrayList<>();
    private List<String> mPictureList = new ArrayList<>();
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
        getDialog().setTitle("Add pictures");

        if (isUpdateMode) {
            this.configureViewModel();
            this.getRealEstateItems(USER_ID);
            this.mActionSave.setText(R.string.button_ok);
        }

        this.configureRecyclerView();
        mActionCancel.setOnClickListener(this);
        mActionSave.setOnClickListener(this);
        mCarouselButtonAdd.setOnClickListener(this);
        mCarouselPictureSelection.setOnClickListener(this);
    }

    // --------------------
    // Permission picture
    // --------------------

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
                Glide.with(this) //SHOWING PREVIEW OF IMAGE
                        .load(this.uriImageSelected)
                        .apply(RequestOptions.circleCropTransform())
                        .into(this.mCarouselPictureSelection);
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
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        this.mAdapter = new DetailAdapter(Glide.with(this));
        this.mRecyclerView.setAdapter(this.mAdapter);
        this.mRecyclerView.setLayoutManager(layoutManager);

    }

    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(getContext());
        this.mViewModel = ViewModelProviders.of(this, viewModelFactory).get(RealEstateViewModel.class);
    }

    // Get all items for a user
    private void getRealEstateItems(int userId) {
        this.mViewModel.getRealEstate(userId).observe(this, this::updateRealEstateUI);
        Log.d(TAG, "getRealEstateItems: ");
    }

    // --------------
    // Ui
    // --------------


    private void updateUI() {
        this.mEditText.setText("");
        this.mCarouselPictureSelection.setImageDrawable(getResources().getDrawable(R.drawable.ic_add_image));
    }


    //Allow to retrieve data from room  and display it
    private void updateRealEstateUI(List<RealEstate> realEstateList) {
        this.mAdapter.updateData(realEstateList.get(dataPosition).getPictureUrl(), realEstateList.get(dataPosition).getTitle());
        this.mRealEstateList.addAll(realEstateList);
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
                if (isCreateMode)
                    this.addPictureAndTitleInList();
                else if (isUpdateMode)
                    this.updatePictureAndTitleInDatabase();
                break;
            case R.id.action_cancel:
                getDialog().cancel();
                break;
            case R.id.action_save:
                if (isCreateMode)
                    this.saveData(mPictureList, mTitleList);
                else if (isUpdateMode)
                    this.saveData(Objects.requireNonNull(mRealEstateList.get(dataPosition).getPictureUrl()), mRealEstateList.get(dataPosition).getTitle());
                break;
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

    private void addPictureAndTitleInList() {
        if (mEditText.getText() != null && uriImageSelected != null) {
            String title = mEditText.getText().toString();
            String url = uriImageSelected.toString();

            this.mPictureList.add(url);
            this.mTitleList.add(title);
            this.mAdapter.updateData(mPictureList, mTitleList);
            this.updateUI();
        } else {
            Toast.makeText(getContext(), "The title or the picture is missing!", Toast.LENGTH_SHORT).show();
        }

    }

    private void updatePictureAndTitleInDatabase() {
        if (mEditText.getText() != null && uriImageSelected != null) {
            String title = mEditText.getText().toString();
            String url = uriImageSelected.toString();

            this.mRealEstateList.get(dataPosition).getPictureUrl().add(url);
            this.mRealEstateList.get(dataPosition).getTitle().add(title);
            mViewModel.updateRealEstate(mRealEstateList.get(dataPosition));//Update here
            this.updateUI();
        } else {
            Toast.makeText(getContext(), "The title or the picture is missing!", Toast.LENGTH_SHORT).show();
        }
    }


    private void saveData(List<String> pictureList, List<String> titleList) {
        if (pictureList.size() > 0 && titleList.size() > 0) {
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
