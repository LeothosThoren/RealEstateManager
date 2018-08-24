package com.openclassrooms.realestatemanager;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.persistence.room.Room;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.openclassrooms.realestatemanager.database.RealEstateDatabase;
import com.openclassrooms.realestatemanager.models.RealEstate;
import com.openclassrooms.realestatemanager.models.User;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.TestCase.assertTrue;

@RunWith(AndroidJUnit4.class)
public class RealEstateDaoTest {

    //--- Test ---
    // DATA SET FOR TEST
    private static long USER_ID = 1;
    private static User USER_DEMO = new User(USER_ID, "Titou", "https://www.google.fr, ");
    private static RealEstate NEW_REAL_ESTATE_FROM_MANHATTAN = new RealEstate("Flat", "Manhattan", "17,870,000", USER_ID);
    private static RealEstate NEW_REAL_ESTATE_FROM_BROOKLYN = new RealEstate("Duplex", "Brooklyn", "13,990,000", USER_ID);
    private static RealEstate NEW_REAL_ESTATE_FROM_SOUTHAMPTON = new RealEstate("House", "Southampton", "41,480,000", USER_ID);

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();
    private RealEstateDatabase database;

    @Before
    public void initDb() throws Exception {
        this.database = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),
                RealEstateDatabase.class)
                .allowMainThreadQueries()
                .build();
    }

    @After
    public void closeDb() throws Exception {
        database.close();
    }

    @Test
    public void insertAndGetUser() throws InterruptedException {
        // BEFORE : Adding a new user
        this.database.userDao().createUser(USER_DEMO);
        // TEST
        User user = LiveDataTestUtil.getValue(this.database.userDao().getUser(USER_ID));
        assertTrue(user.getUsername().equals(USER_DEMO.getUsername()) && user.getId() == USER_ID);
    }

    @Test
    public void getRelaEstateWhenNoItemInserted() throws InterruptedException {
        // TEST
        List<RealEstate> realEstates = LiveDataTestUtil.getValue(this.database.realEstateDao().getRealEstate(USER_ID));
        assertTrue(realEstates.isEmpty());
    }

    @Test
    public void insertAndGetRealEstates() throws InterruptedException {
        // BEFORE : Adding demo user & demo realEstate

        this.database.userDao().createUser(USER_DEMO);
        this.database.realEstateDao().insertRealEstate(NEW_REAL_ESTATE_FROM_MANHATTAN);
        this.database.realEstateDao().insertRealEstate(NEW_REAL_ESTATE_FROM_BROOKLYN);
        this.database.realEstateDao().insertRealEstate(NEW_REAL_ESTATE_FROM_SOUTHAMPTON);

        // TEST
        List<RealEstate> realEstates = LiveDataTestUtil.getValue(this.database.realEstateDao().getRealEstate(USER_ID));
        assertTrue(realEstates.size() == 3);
    }

    @Test
    public void insertAndUpdateRealEstate() throws InterruptedException {
        // BEFORE : Adding demo user & demo RealEstate. Next, update item added & re-save it
        this.database.userDao().createUser(USER_DEMO);
        this.database.realEstateDao().insertRealEstate(NEW_REAL_ESTATE_FROM_MANHATTAN);
        RealEstate realEstateAdded = LiveDataTestUtil.getValue(this.database.realEstateDao().getRealEstate(USER_ID)).get(0);
        realEstateAdded.setType("Penthouse");
        this.database.realEstateDao().updateRealEstate(realEstateAdded);

        //TEST
        List<RealEstate> realEstates = LiveDataTestUtil.getValue(this.database.realEstateDao().getRealEstate(USER_ID));
        assertEquals("Penthouse", realEstates.get(0).getType());
    }

    @Test
    public void insertAndDeleteRealEstate() throws InterruptedException {
        // BEFORE : Adding demo user & demo RealEstate. Next, get the RealEstate added & delete it.
        this.database.userDao().createUser(USER_DEMO);
        this.database.realEstateDao().insertRealEstate(NEW_REAL_ESTATE_FROM_MANHATTAN);
        RealEstate realEstateAdded = LiveDataTestUtil.getValue(this.database.realEstateDao().getRealEstate(USER_ID)).get(0);
        this.database.realEstateDao().deleteRealEstate(realEstateAdded.getId());

        //TEST
        List<RealEstate> realEstates = LiveDataTestUtil.getValue(this.database.realEstateDao().getRealEstate(USER_ID));
        assertTrue(realEstates.isEmpty());
    }

}
