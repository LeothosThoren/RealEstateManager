package com.openclassrooms.realestatemanager.injections;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.openclassrooms.realestatemanager.repositories.RealEstateDataRepository;
import com.openclassrooms.realestatemanager.repositories.UserDataRepository;
import com.openclassrooms.realestatemanager.viewmodels.RealEstateViewModel;

import java.util.concurrent.Executor;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private final RealEstateDataRepository realEstateDataSource;
    private final UserDataRepository userDataSource;
    private final Executor executor;

    public ViewModelFactory(RealEstateDataRepository realEstateDataSource,
                            UserDataRepository userDataSource, Executor executor) {
        this.realEstateDataSource = realEstateDataSource;
        this.userDataSource = userDataSource;
        this.executor = executor;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(RealEstateViewModel.class)) {
            return (T) new RealEstateViewModel(realEstateDataSource, userDataSource, executor);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
