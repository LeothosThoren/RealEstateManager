package com.openclassrooms.realestatemanager.repositories;

import android.arch.lifecycle.LiveData;

import com.openclassrooms.realestatemanager.database.dao.RealEstateDao;
import com.openclassrooms.realestatemanager.models.RealEstate;

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
