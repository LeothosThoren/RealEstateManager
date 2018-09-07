package com.openclassrooms.realestatemanager.controlers.fragments;


import android.app.DatePickerDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CustomDialogForm extends DialogFragment implements View.OnClickListener {

    public static final int USER_ID = 1;
    private static final String TAG = CustomDialogForm.class.getSimpleName();
    // Widget
    @BindView(R.id.action_cancel)
    TextView mActionCancel;
    @BindView(R.id.action_save)
    TextView mActionSave;
    @BindView(R.id.edit_text_type)
    Spinner mType;
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
    TextView mEntryDateText;
    @BindView(R.id.edit_text_sold_date)
    TextView mSoldDateText;
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
    @BindView(R.id.button_add_point_of_interest)
    Button mAddPoi;
    // Data
    private RealEstateViewModel mViewModel;
    private Date entryDate, soldDate;
    // Var
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private boolean mIsLargeLayout;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_custom_dialog_form, container, false);
        ButterKnife.bind(this, view);
        this.init();
        return view;

    }

    private void init() {
        // Methods
        this.configureViewModel();
        this.configureSpinner();
        this.getRealEstateItems(USER_ID);
        mIsLargeLayout = getResources().getBoolean(R.bool.large_layout);
        mActionCancel.setOnClickListener(this);
        mActionSave.setOnClickListener(this);
        mEntryDateText.setOnClickListener(this);
        mAddPoi.setOnClickListener(this);

        //Handle the click of the date picker dialog
        mDateSetListener = (view, year1, month1, dayOfMonth) -> {
            month1 = month1 + 1;
            String date = Utils.checkDigit(month1) + "/" + Utils.checkDigit(dayOfMonth) + "/" + year1;
            //For displaying in the view
            mEntryDateText.setText(date);
            // Get to database
            try {
                entryDate = Utils.getDateFromDatePicker(dayOfMonth, month1, year1);
            } catch (ParseException e) {
                e.printStackTrace();
            }

        };
    }

    // --------------
    // UI
    // --------------

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

    // --------------
    // Action
    // --------------

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.edit_text_entry_date:
                Log.d(TAG, "onClick: change entry date");
                this.configDatePickerDialog(getContext());
                break;
            case R.id.edit_text_sold_date:
                Log.d(TAG, "onClick: change sold date");
                this.configDatePickerDialog(getContext());
                break;
            case R.id.button_add_point_of_interest:
                Log.d(TAG, "onClick: open poi list");
                //Todo
                break;
            case R.id.button_add_picture:
                Log.d(TAG, "onClick: open picture list");
                //Todo
                break;
            case R.id.action_cancel:
                Log.d(TAG, "onClick: cancel and closing dialog");
                getDialog().cancel();
                break;
            case R.id.action_save:
                Log.d(TAG, "onClick: saving data and closing dialog");
                saveOperation();
        }

    }

    // --------------
    // Config
    // --------------

    private void configDatePickerDialog(Context context) {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(context, android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                mDateSetListener, year, month, day);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        dialog.show();

    }

    private void configureSpinner() {
        if (getContext() != null) {
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.property_type, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_list_item_checked);
            mType.setAdapter(adapter);
        }

    }

    // --------------
    // Data
    // --------------

    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(getContext());
        this.mViewModel = ViewModelProviders.of(this, viewModelFactory).get(RealEstateViewModel.class);
    }

    // Get all items for a user
    private void getRealEstateItems(int userId) {
        this.mViewModel.getRealEstate(userId).observe(this, this::retrieveDateFromViewModel);
        Log.d(TAG, "getRealEstateItems: ");
    }

    private void retrieveDateFromViewModel(List<RealEstate> realEstateList) {
        Log.d(TAG, "retrieveDateFromViewModel: at position 1:\n"+
        realEstateList.get(1).getEntryDate()+"\n"+
        realEstateList.get(1).getPrice()+"\n"+
        realEstateList.get(1).getSoldDate());

    }

    private void saveOperation() {

        if (mType.getSelectedItem() != null && mArea.getText() != null && mDescription.getText() != null
                && mPrice.getText() != null && mSurface.getText() != null && mRoomNb.getText() != null
                && mBathroomNb.getText() != null && mBedroomNb.getText() != null && entryDate != null) {
            RealEstate realEstate = new RealEstate(mType.getSelectedItem().toString(), mArea.getText().toString(),
                    mDescription.getText().toString(), Long.valueOf(mPrice.getText().toString()),
                    Integer.valueOf(mSurface.getText().toString()), Integer.valueOf(mRoomNb.getText().toString()),
                    Integer.valueOf(mBathroomNb.getText().toString()),
                    Integer.valueOf(mBedroomNb.getText().toString()), "https://images.pexels.com/photos/534151/pexels-photo-534151.jpeg",
                    populateAddressObject(), entryDate, USER_ID);
            //Creation on DB
            mViewModel.createRealEstate(realEstate);
            //Confirmation
            Toast.makeText(getContext(), "Data saved!", Toast.LENGTH_SHORT).show();
            getDialog().dismiss();
        } else {
            Toast.makeText(getContext(), "Please, fulfil all the fields", Toast.LENGTH_SHORT).show();
        }

    }

    private Address populateAddressObject() {
        Address address = new Address();
        address.number = Integer.valueOf(mAddressNum.getText().toString());
        address.line1 = mAddressLine1.getText().toString();
        address.line2 = mAddressLine2.getText().toString();
        address.city = mAddressCity.getText().toString();
        address.state = mAddressState.getText().toString();
        address.zip = mAddressZip.getText().toString();

        return address;
    }


}
