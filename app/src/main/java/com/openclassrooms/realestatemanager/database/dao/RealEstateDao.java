package com.openclassrooms.realestatemanager.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.database.Cursor;

import com.openclassrooms.realestatemanager.entities.RealEstate;

import java.util.Date;
import java.util.List;

@Dao
public interface RealEstateDao {

    @Query("SELECT * FROM RealEstate WHERE userId = :userId")
    LiveData<List<RealEstate>> getRealEstate(long userId);

    @Query("SELECT * FROM RealEstate WHERE userId = :userId AND area LIKE :area AND surface BETWEEN :minSurface AND :maxSurface" +
            " AND price BETWEEN :minPrice AND :maxPrice AND room BETWEEN :minRoom AND :maxRoom AND entryDate BETWEEN :minDate AND :maxDate")
    LiveData<List<RealEstate>> searchRealEstate(String area, Integer minSurface, Integer maxSurface, Long minPrice, Long maxPrice,
                                                Integer minRoom, Integer maxRoom, Date minDate, Date maxDate, long userId);

    @Query("SELECT * FROM RealEstate WHERE userId = :userId")
    Cursor getRealEstateWithCursor(long userId);

    @Insert
    long insertRealEstate(RealEstate realEstate);

    @Update
    int updateRealEstate(RealEstate realEstate);

    @Query("DELETE FROM RealEstate WHERE id = :realestateId")
    int deleteRealEstate(long realestateId);
}
