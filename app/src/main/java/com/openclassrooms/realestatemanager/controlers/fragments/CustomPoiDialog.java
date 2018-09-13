package com.openclassrooms.realestatemanager.controlers.fragments;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.adapters.PoiAdapter;
import com.openclassrooms.realestatemanager.entities.RealEstate;
import com.openclassrooms.realestatemanager.injections.Injection;
import com.openclassrooms.realestatemanager.injections.ViewModelFactory;
import com.openclassrooms.realestatemanager.utils.HelperSingleton;
import com.openclassrooms.realestatemanager.viewmodels.RealEstateViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.openclassrooms.realestatemanager.controlers.fragments.CustomDialogForm.USER_ID;

/**
 * A simple {@link Fragment} subclass.
 */
public class CustomPoiDialog extends DialogFragment implements View.OnClickListener {

    private static final String TAG = "CustomPoiDialog";
    public OnInputSelected mOnInputSelected;
    //Widget
    @BindView(R.id.poi_spinner)
    Spinner mSpinner;
    @BindView(R.id.action_cancel)
    TextView mActionCancel;
    @BindView(R.id.action_save)
    TextView mActionOk;
    @BindView(R.id.recycler_view_object)
    RecyclerView mRecyclerView;
    @BindView(R.id.poi_button_add)
    ImageView mPoiButtonAdd;
    //Var
    private ArrayAdapter<CharSequence> spinnerAdapter;
    private PoiAdapter mPoiAdapter;
    private int dataPosition = HelperSingleton.getInstance().getPosition();
    private boolean isCreateMode = HelperSingleton.getInstance().getMode() == R.id.menu_add;
    private boolean isUpdateMode = HelperSingleton.getInstance().getMode() == R.id.menu_update;
    private boolean mIsLargeLayout;
    //Data
    private List<String> poiList = new ArrayList<>();
    private List<RealEstate> mRealEstateList = new ArrayList<>();
    private RealEstateViewModel mViewModel;

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_custom_poi_dialog, container, false);
        ButterKnife.bind(this, view);
        mIsLargeLayout = getResources().getBoolean(R.bool.large_layout);

        this.init();
        return view;
    }

    @Override
    public void onResume() {
        // Get existing layout params for the window
        if (getDialog().getWindow() != null) {
            ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
            // Assign window properties to fill the parent
            if (mIsLargeLayout) {
                params.width = WindowManager.LayoutParams.WRAP_CONTENT;
                params.height = WindowManager.LayoutParams.MATCH_PARENT;
                getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
            } else {
                params.width = WindowManager.LayoutParams.MATCH_PARENT;
                params.height = WindowManager.LayoutParams.MATCH_PARENT;
                getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
            }

        }
        // Call super onResume after resizing
        super.onResume();
    }

    // ----------------
    // init
    // ----------------

    private void init() {
        getDialog().setTitle("Select points of interest");

        if (isUpdateMode) {
            this.configureViewModel();
            this.getRealEstateItems(USER_ID);
            this.mActionOk.setText(R.string.button_ok);
        }

        this.configureRecyclerView();
        this.configureSpinner();
        mActionCancel.setOnClickListener(this);
        mActionOk.setOnClickListener(this);
        mPoiButtonAdd.setOnClickListener(this);
    }


    // ----------------
    // Config
    // ----------------

    // RecyclerView
    private void configureRecyclerView() {
        this.mPoiAdapter = new PoiAdapter();
        this.mRecyclerView.setAdapter(this.mPoiAdapter);
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    }

    private void configureSpinner() {
        if (getContext() != null) {
            spinnerAdapter = ArrayAdapter.createFromResource(getContext(), R.array.point_of_interest, android.R.layout.simple_spinner_item);
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
            mSpinner.setAdapter(spinnerAdapter);
        }

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

    //Allow to retrieve data from room  and display it
    private void updateRealEstateUI(List<RealEstate> realEstateList) {
        this.mPoiAdapter.updateData(realEstateList.get(dataPosition).getPoi());
        this.mRealEstateList.addAll(realEstateList);
    }

    private void updateUI() {
        mPoiAdapter.updateData(poiList);
    }

    // --------------
    // Action
    // --------------

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.action_cancel:
                getDialog().cancel();
                break;
            case R.id.action_save:
                if (isCreateMode)
                    this.saveData();
                else if (isUpdateMode)
                    getDialog().dismiss();
                break;
            case R.id.poi_button_add:
                if (isCreateMode) {
                    this.addPoiInList();
                } else if (isUpdateMode) {
                    updatePoiInDatabase();
                }
                break;
        }
    }


    private void addPoiInList() {
        String input = mSpinner.getSelectedItem().toString();
        this.poiList.add(input);
        this.updateUI();
        Log.d(TAG, "configureClickAction: find item = " + input);
    }

    private void updatePoiInDatabase() {
        String input = mSpinner.getSelectedItem().toString();
        this.mRealEstateList.get(dataPosition).getPoi().add(input);
        mViewModel.updateRealEstate(mRealEstateList.get(dataPosition));
    }

    private void saveData() {
        if (poiList.size() > 0) {
            mOnInputSelected.sendInput(poiList);
            Log.d(TAG, "Show size of the array : " + poiList.size());
            Toast.makeText(getContext(), "Poi added!", Toast.LENGTH_SHORT).show();
            getDialog().dismiss();
        } else {
            Toast.makeText(getContext(), "Please add at least one item", Toast.LENGTH_SHORT).show();
        }
    }


    //--------------
    // Interface
    // -------------

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mOnInputSelected = (OnInputSelected) getTargetFragment();
        } catch (ClassCastException e) {
            Log.e(TAG, "onAttach: ClassCastException : " + e.getMessage());
        }
    }

    //Interface
    public interface OnInputSelected {
        void sendInput(List<String> input);
    }
}


