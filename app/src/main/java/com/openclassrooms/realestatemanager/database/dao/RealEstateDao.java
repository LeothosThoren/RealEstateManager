package com.openclassrooms.realestatemanager.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.openclassrooms.realestatemanager.models.RealEstate;

import java.util.List;

@Dao
public interface RealEstateDao {

    @Query("SELECT * FROM RealEstate WHERE userId = :userId")
    LiveData<List<RealEstate>> getRealEstate(long userId);

    @Insert
    long insertRealEstate(RealEstate realEstate);

    @Update
    int updateRealEstate(RealEstate realEstate);

    @Query("DELETE FROM RealEstate WHERE id = :realestateId")
    int deleteRealEstate(long realestateId);
}
