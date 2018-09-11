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
public class CustomPoiDialog extends DialogFragment {

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
    //Data
    private List<String> poiList = new ArrayList<>();
    private RealEstateViewModel mViewModel;

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_custom_list_object, container, false);
        ButterKnife.bind(this, view);
        //Methods
        this.configureRecyclerView();
        this.configureSpinner();
        this.configureClickAction();
        this.configureViewModel();
        this.getRealEstateItems(USER_ID);

        return view;
    }


    // ----------------
    // Config
    // ----------------

    // RecyclerView
    private void configureRecyclerView() {
        this.mPoiAdapter = new PoiAdapter();
        this.mRecyclerView.setAdapter(this.mPoiAdapter);
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        this.configureClickWithRecyclerView();

    }

    private void configureSpinner() {
        if (getContext() != null) {
            spinnerAdapter = ArrayAdapter.createFromResource(getContext(), R.array.point_of_interest, android.R.layout.simple_spinner_item);
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_list_item_checked);
            mSpinner.setAdapter(spinnerAdapter);
        }

    }

    // --------------
    // Action
    // --------------

    private void configureClickAction() {
        mActionOk.setOnClickListener(v -> {
            Log.d(TAG, "onClick: capturing spinner input.");
            if (poiList != null) {
                mOnInputSelected.sendInput(poiList);
                Log.d(TAG, "Show size of the array : " + poiList.size());
                Toast.makeText(getContext(), "Poi added!", Toast.LENGTH_SHORT).show();
                getDialog().dismiss();
            } else {
                Toast.makeText(getContext(), "Please add at least one item", Toast.LENGTH_SHORT).show();
            }

        });

        mActionCancel.setOnClickListener(v -> {
            Log.d(TAG, "onClick: closing dialog");
            getDialog().cancel();
        });


        //If we want to update it need to be done directly in here, add or delete
        mPoiButtonAdd.setOnClickListener(v -> {
            String input = mSpinner.getSelectedItem().toString();
            if (!input.equals("")) {
                poiList.add(input);
                if (poiList.size() > 0)
                    mPoiAdapter.notifyItemChanged(poiList.size());
                Log.d(TAG, "configureClickAction: find item = " + input);
            }

        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mOnInputSelected = (OnInputSelected) getTargetFragment();
        } catch (ClassCastException e) {
            Log.e(TAG, "onAttach: ClassCastException : " + e.getMessage());
        }
    }

    // ------------
    // Data
    // ------------

    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(getContext());
        this.mViewModel = ViewModelProviders.of(this, viewModelFactory).get(RealEstateViewModel.class);
    }

    // Get all items for a user
    private void getRealEstateItems(int userId) {
        this.mViewModel.getRealEstate(userId).observe(this, this::updatePoiUI);
        Log.d(TAG, "getRealEstateItems: ");
    }

    private void updatePoiUI(List<RealEstate> realEstateList) {
        mPoiAdapter.updateData(realEstateList.get(dataPosition).getPoi());
    }

    //Interface
    public interface OnInputSelected {
        void sendInput(List<String> input);
    }
}


