package com.openclassrooms.realestatemanager.controlers.fragments;


import android.app.DialogFragment;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.adapters.RealEstateAdapter;
import com.openclassrooms.realestatemanager.entities.RealEstate;
import com.openclassrooms.realestatemanager.injections.Injection;
import com.openclassrooms.realestatemanager.injections.ViewModelFactory;
import com.openclassrooms.realestatemanager.utils.HelperSingleton;
import com.openclassrooms.realestatemanager.utils.ItemClickSupport;
import com.openclassrooms.realestatemanager.viewmodels.RealEstateViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.openclassrooms.realestatemanager.controlers.activities.RealEstateActivity.FRAGMENT_FORM_TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class RealEstateFragment extends Fragment implements RealEstateAdapter.Listener{

    private static final String TAG = "RealEstateFragment";
    //CONSTANT
    public static int USER_ID = 1;
    //WIDGET
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.frag_swipe_layout)
    SwipeRefreshLayout mRefreshLayout;
    //DATA
    public static RealEstateAdapter mAdapter;
    private RealEstateViewModel mRealEstateViewModel;
    // Declare callback
    private OnItemClickListenerCustom mCallback;

    public RealEstateFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the recycler_view_item_real_estate_layout for this fragment
        View view = inflater.inflate(R.layout.fragment_real_estate, container, false);
        ButterKnife.bind(this, view);

        this.init();
        return view;
    }
    private void init() {
        // Call for new methods
        this.configureRecyclerView();
        this.configureViewModel();
        this.getRealEstateItems(USER_ID);
        this.configureSwipeRefreshLayout();
    }


    // --------------
    // Configure
    // --------------

    // Configure ViewModel
    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(getContext());
        this.mRealEstateViewModel = ViewModelProviders.of(this, viewModelFactory).get(RealEstateViewModel.class);
    }

    private void configureSwipeRefreshLayout() {
        this.mRefreshLayout.setOnRefreshListener(() -> {
            getRealEstateItems(USER_ID);
        });
    }

    // RecyclerView
    private void configureRecyclerView() {
        mAdapter = new RealEstateAdapter(Glide.with(this), this);
        this.mRecyclerView.setAdapter(mAdapter);
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.configureClickWithRecyclerView();

    }

    // --------------
    // Action
    // --------------

    //Click on list handle with utility class
    private void configureClickWithRecyclerView() {
        ItemClickSupport.addTo(mRecyclerView, R.layout.recycler_view_item_real_estate_layout)
                .setOnItemClickListener((recyclerView1, position, v) -> {
                    // Action to do here
                    Toast.makeText(getContext(), "Click on position : " + position, Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "configureClickWithRecyclerView: "+ mAdapter.getRealEstate(position).getArea());

                    RealEstate realEstate = mAdapter.getRealEstate(position);
                    mCallback.onItemClickListenerCustom(v, position, realEstate);
                });
    }

    //Interface that handle click from specific view
    @Override
    public void onClickCheckButton(int position) {
        Log.d(TAG, "onClickCheckButton: case checked on position: "+ position);
        HelperSingleton.getInstance().setPosition(position);
        mAdapter.notifyItemChanged(position);
        this.openCustomDialog();
    }

    //Try to open fragment from item check box
    private void openCustomDialog() {
        CustomDialogForm customDialogForm = new CustomDialogForm();
        customDialogForm.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.Dialog_FullScreen);
        if (getFragmentManager() != null)
        customDialogForm.show(getFragmentManager(), FRAGMENT_FORM_TAG);
    }

    // --------------
    // Ui
    // --------------

    // Get all items for a user
    private void getRealEstateItems(int userId) {
        this.mRealEstateViewModel.getRealEstate(userId).observe(this, this::updateRealEstateItemsList);
    }

    // Update the list of Real Estate item
    private void updateRealEstateItemsList(List<RealEstate> realEstates) {
        mAdapter.updateData(realEstates);
        mRefreshLayout.setRefreshing(false);
    }

    // -------------
    // Callback
    // -------------

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Call the method that creating callback after being attached to parent activity
        this.createCallbackToParentActivity();
    }

    // Create callback to parent activity
    private void createCallbackToParentActivity() {
        try {
            //Parent activity will automatically subscribe to callback
            mCallback = (OnItemClickListenerCustom) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(e.toString() + " must implement OnItemClickListenerCustom");
        }
    }

    // Declare our interface that will be implemented by any container activity
    public interface OnItemClickListenerCustom {
        void onItemClickListenerCustom(View view, int position, RealEstate realEstate);
    }


}
