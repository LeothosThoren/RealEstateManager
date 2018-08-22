package com.openclassrooms.realestatemanager;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.persistence.room.Room;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.openclassrooms.realestatemanager.database.RealEstateDatabase;
import com.openclassrooms.realestatemanager.models.User;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.TestCase.assertTrue;

@RunWith(AndroidJUnit4.class)
public class RealEstateDaoTest {

    private RealEstateDatabase database;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

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

    //--- Test ---
    // DATA SET FOR TEST
    private static long USER_ID = 1;
    private static User USER_DEMO = new User(USER_ID, "Titou", "https://www.google.fr, ");


    @Test
    public void insertAndGetUser() throws InterruptedException {
        // BEFORE : Adding a new user
        this.database.userDao().createUser(USER_DEMO);
        // TEST
        User user = LiveDataTestUtil.getValue(this.database.userDao().getUser(USER_ID));
        assertTrue(user.getUsername().equals(USER_DEMO.getUsername()) && user.getId() == USER_ID);
    }

}
