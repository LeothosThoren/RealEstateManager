package com.openclassrooms.realestatemanager.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.Nullable;

import com.openclassrooms.realestatemanager.entities.RealEstate;
import com.openclassrooms.realestatemanager.entities.User;
import com.openclassrooms.realestatemanager.repositories.RealEstateDataRepository;
import com.openclassrooms.realestatemanager.repositories.UserDataRepository;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;

public class RealEstateViewModel extends ViewModel {

    private final RealEstateDataRepository realEstateDataSource;
    private final UserDataRepository userDataSource;
    private final Executor executor;

    // DATA
    @Nullable
    private LiveData<User> currentUser;

    public RealEstateViewModel(RealEstateDataRepository realEstateDataSource,
                               UserDataRepository userDataSource, Executor executor) {
        this.realEstateDataSource = realEstateDataSource;
        this.userDataSource = userDataSource;
        this.executor = executor;
    }

    public void init(long userId) {
        if (this.currentUser != null) {
            return;
        }
        currentUser = userDataSource.getUser(userId);
    }

    // -------------
    // FOR USER
    // -------------

    public LiveData<User> getUser(long userId) {
        return this.currentUser;
    }

    // -----------------
    // FOR REAL ESTATE
    // -----------------

    public LiveData<List<RealEstate>> getRealEstate(long userId) {
        return realEstateDataSource.getRealEstate(userId);
    }

    public LiveData<List<RealEstate>> searchRealEstate(String type, String area, Integer minSurface, Integer maxSurface, Long minPrice, Long maxPrice,
                                                       Integer minRoom, Integer maxRoom, long userId) {
        return realEstateDataSource.searchRealEstate(type, area, minSurface, maxSurface, minPrice, maxPrice,
                minRoom, maxRoom, userId);
    }

    public void createRealEstate(RealEstate realEstate) {
        executor.execute(() -> realEstateDataSource.createRealEstate(realEstate));
    }

    public void deleteRealEstate(long itemId) {
        executor.execute(() -> realEstateDataSource.deleteRealEstate(itemId));
    }

    public void updateRealEstate(RealEstate realEstate) {
        executor.execute(() -> realEstateDataSource.updateRealEstate(realEstate));
    }

}
