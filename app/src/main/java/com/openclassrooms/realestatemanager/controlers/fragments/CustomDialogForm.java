package com.openclassrooms.realestatemanager.controlers.fragments;


import android.app.DatePickerDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.entities.Address;
import com.openclassrooms.realestatemanager.entities.RealEstate;
import com.openclassrooms.realestatemanager.injections.Injection;
import com.openclassrooms.realestatemanager.injections.ViewModelFactory;
import com.openclassrooms.realestatemanager.utils.Utils;
import com.openclassrooms.realestatemanager.viewmodels.RealEstateViewModel;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CustomDialogForm extends DialogFragment implements View.OnClickListener {

    public static final int USER_ID = 1;
    private static final String TAG = "CustomDialogForm";
    // Widget
    @BindView(R.id.action_cancel)
    TextView mActionCancel;
    @BindView(R.id.action_save)
    TextView mActionSave;
    @BindView(R.id.edit_text_type)
    EditText mType;
    @BindView(R.id.edit_text_area)
    EditText mArea;
    @BindView(R.id.edit_text_description)
    EditText mDescription;
    @BindView(R.id.edit_text_surface)
    EditText mSurface;
    @BindView(R.id.edit_text_price)
    EditText mPrice;
    @BindView(R.id.edit_text_nb_rooms)
    EditText mRoomNb;
    @BindView(R.id.edit_text_nb_bathrooms)
    EditText mBathroomNb;
    @BindView(R.id.edit_text_nb_bedrooms)
    EditText mBedroomNb;
    @BindView(R.id.edit_text_entry_date)
    EditText mEntryDate;
    @BindView(R.id.edit_text_address_number)
    EditText mAddressNum;
    @BindView(R.id.edit_text_address_line1)
    EditText mAddressLine1;
    @BindView(R.id.edit_text_address_line2)
    EditText mAddressLine2;
    @BindView(R.id.edit_text_address_city)
    EditText mAddressCity;
    @BindView(R.id.edit_text_address_state)
    EditText mAddressState;
    @BindView(R.id.edit_text_address_zip)
    EditText mAddressZip;
    // Data
    private RealEstateViewModel mViewModel;
    // Var
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private Date entryDate;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_custom_dialog_form, container, false);
        ButterKnife.bind(this, view);
        // Methods
        this.configureViewModel();
        mActionCancel.setOnClickListener(this);
        mActionSave.setOnClickListener(this);
        mEntryDate.setOnClickListener(this);
        return view;

    }

    // --------------
    // UI
    // --------------

    @Override
    public void onResume() {
        // Get existing layout params for the window
        ViewGroup.LayoutParams params = Objects.requireNonNull(getDialog().getWindow()).getAttributes();
        // Assign window properties to fill the parent
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        // Call super onResume after resizing
        super.onResume();
    }

    // --------------
    // Action
    // --------------

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.edit_text_entry_date:
                this.configDatePickerDialog(getContext());
                break;
            case R.id.action_cancel:
                Log.d(TAG, "onClick: closing dialog");
                getDialog().cancel();
                break;
            case R.id.action_save:
                Log.d(TAG, "onClick: saving data and closing dialog");
                //Save operation...
                saveOperation(mType.getText().toString(), mArea.getText().toString(), mDescription.getText().toString(), Long.valueOf(mPrice.getText().toString()),
                        Integer.valueOf(mSurface.getText().toString()), Integer.valueOf(mRoomNb.getText().toString()), Integer.valueOf(mBathroomNb.getText().toString()),
                        Integer.valueOf(mBedroomNb.getText().toString()), "https://unsplash.com/photos/mcL2f-J74GY", populateAddressObject(), entryDate, USER_ID);

        }

    }

    // --------------
    // Data
    // --------------

    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(getContext());
        this.mViewModel = ViewModelProviders.of(this, viewModelFactory).get(RealEstateViewModel.class);
    }

    private void saveOperation(String type, String area, String description, long price, int surface, int rooms,
                               int bathrooms, int bedrooms, String url, Address address, Date entryDate, long userId) {

        RealEstate realEstate = new RealEstate(type, area, description, price, surface, rooms, bathrooms, bedrooms, url, address, entryDate, userId);
        mViewModel.createRealEstate(realEstate);

        // Take little moment before dismiss the dialog and display a toast message
        final Handler handler = new Handler();
        handler.postDelayed(() -> {
            Toast.makeText(getContext(), "Data saved!", Toast.LENGTH_SHORT).show();
            getDialog().dismiss();
        }, 2000);
    }

    private Address populateAddressObject() {
        Address address = new Address();
        address.number = Integer.valueOf(mAddressNum.getText().toString());
        address.line1 = mAddressLine1.getText().toString();
        address.line2 = mAddressLine2.getText().toString();
        address.city = mAddressCity.getText().toString();
        address.country = mAddressState.getText().toString();
        address.zip = mAddressZip.getText().toString();

        return address;
    }

    // --------------
    // Config
    // --------------

    private void configDatePickerDialog(Context context) {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(
                context,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                mDateSetListener,
                year, month, day);
        dialog.show();
        //Handle the click
        mDateSetListener = (view, year1, month1, dayOfMonth) -> {
            month1 = month1 + 1;
            String date = Utils.checkDigit(month1) + "/" + Utils.checkDigit(dayOfMonth) + "/" + year1;
            //For display in the view
            mEntryDate.setText(date);
            // Get to database
            try {
                entryDate = Utils.getDateFromDatePicker(dayOfMonth, month1, year1);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        };

    }


}
