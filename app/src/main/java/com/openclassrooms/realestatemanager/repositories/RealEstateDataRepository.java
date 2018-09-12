package com.openclassrooms.realestatemanager.repositories;

import android.arch.lifecycle.LiveData;

import com.openclassrooms.realestatemanager.database.dao.RealEstateDao;
import com.openclassrooms.realestatemanager.entities.RealEstate;

import java.util.Date;
import java.util.List;

public class RealEstateDataRepository {

    private final RealEstateDao realEstateDao;

    public RealEstateDataRepository(RealEstateDao realEstateDao) {
        this.realEstateDao = realEstateDao;
    }

    // --- GET ---

    public LiveData<List<RealEstate>> getRealEstate(long userId) {
        return this.realEstateDao.getRealEstate(userId);
    }

    // --- SEARCH ---

    public LiveData<List<RealEstate>> searchRealEstate(String area, int minSurface, int maxSurface, long minPrice, long maxPrice,
                                                       int minRoom, int maxRoom, Date minDate, Date maxDate, long userId) {
        return this.realEstateDao.searchRealEstate(area, minSurface, maxSurface, minPrice, maxPrice,
                minRoom, maxRoom, minDate, maxDate, userId);
    }

    // --- CREATE ---

    public void createRealEstate(RealEstate realEstate) {
        realEstateDao.insertRealEstate(realEstate);
    }

    // --- DELETE ---
    public void deleteRealEstate(long realEstateId) {
        realEstateDao.deleteRealEstate(realEstateId);
    }

    // --- UPDATE ---
    public void updateRealEstate(RealEstate realEstate) {
        realEstateDao.updateRealEstate(realEstate);
    }
}
