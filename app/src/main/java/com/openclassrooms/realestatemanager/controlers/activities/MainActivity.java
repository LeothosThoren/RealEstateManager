package com.openclassrooms.realestatemanager.controlers.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.entities.User;
import com.openclassrooms.realestatemanager.injections.Injection;
import com.openclassrooms.realestatemanager.injections.ViewModelFactory;
import com.openclassrooms.realestatemanager.utils.Utils;
import com.openclassrooms.realestatemanager.viewmodels.RealEstateViewModel;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private TextView textViewMain;
    private TextView textViewQuantity;
    private Button buttonGoToActivity;
    private TextView headerUserName;
    private ImageView headerPictureProfile;

    private RealEstateViewModel mRealEstateViewModel;
    private static int USER_ID = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.textViewMain = findViewById(R.id.activity_main_activity_text_view_main);
        this.textViewQuantity = findViewById(R.id.activity_main_activity_text_view_quantity);
        this.buttonGoToActivity = findViewById(R.id.simple_button);
        this.headerUserName = findViewById(R.id.main_activity_header_profile_text);
        this.headerPictureProfile = findViewById(R.id.main_activity_header_profile_image);

        this.configureTextViewMain();
        this.configureTextViewQuantity();
        this.clickToGoToActivity();
        this.configureViewModel();
        this.getCurrentUser(USER_ID);

        Utils.isInternetAvailable(this, TAG);
    }

//    // DATA

    // 2 - Configuring ViewModel
    private void configureViewModel(){
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(this);
        this.mRealEstateViewModel = ViewModelProviders.of(this, viewModelFactory).get(RealEstateViewModel.class);
        this.mRealEstateViewModel.init(USER_ID);
    }

    // 3 - Get Current User
    private void getCurrentUser(int userId){
        this.mRealEstateViewModel
                .getUser(userId).observe(this, this::updateHeader);
    }

    // 5 - Update header (username & picture)
    private void updateHeader(User user){
        this.headerUserName.setText(user.getUsername());
        Glide.with(this).load(user.getUrlPicture()).apply(RequestOptions.circleCropTransform()).into(this.headerPictureProfile);
    }

    // UTILITY

    private void clickToGoToActivity() {
        buttonGoToActivity.setOnClickListener(v -> startActivity(RealEstateActivity.class));
    }

    private void startActivity(Class activity) {
        Intent i = new Intent(this, activity);
        startActivity(i);
    }

    // DON'T TOUCH

    private void configureTextViewMain() {
        this.textViewMain.setTextSize(15);
        this.textViewMain.setText("Le premier bien immobilier enregistré vaut ");
    }

    private void configureTextViewQuantity() {
        int quantity = Utils.convertDollarToEuro(100);
        this.textViewQuantity.setTextSize(20);
        this.textViewQuantity.setText(String.format("%d", quantity));
    }
}
