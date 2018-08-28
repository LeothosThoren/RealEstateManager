package com.openclassrooms.realestatemanager;

import android.arch.persistence.room.Room;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.openclassrooms.realestatemanager.database.RealEstateDatabase;
import com.openclassrooms.realestatemanager.provider.RealEstateContentProvider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;

@RunWith(AndroidJUnit4.class)
public class RealEstateContentProviderTest {

    // DATA SET FOR TEST
    private static long USER_ID = 1;
    // FOR DATA
    private ContentResolver mContentResolver;

    @Before
    public void setUp() {
        Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),
                RealEstateDatabase.class)
                .allowMainThreadQueries()
                .build();
        mContentResolver = InstrumentationRegistry.getContext().getContentResolver();
    }

    @Test
    public void getItemsWhenNoItemInserted() {
        final Cursor cursor = mContentResolver.query(ContentUris.withAppendedId(RealEstateContentProvider.URI_REAL_ESTATE, USER_ID), null, null, null, null);
        assertThat(cursor, notNullValue());
        if (cursor != null) {
            assertThat(cursor.getCount(), is(0));
            cursor.close();
        }
    }

    @Test
    public void insertAndGetItem() {
        // BEFORE : Adding demo item
        final Uri userUri = mContentResolver.insert(RealEstateContentProvider.URI_REAL_ESTATE, generateItem());
        // TEST
        final Cursor cursor = mContentResolver.query(ContentUris.withAppendedId(RealEstateContentProvider.URI_REAL_ESTATE, USER_ID), null, null, null, null);
        assertThat(cursor, notNullValue());
        if (cursor != null) {
            assertThat(cursor.getCount(), is(1));
            assertThat(cursor.moveToFirst(), is(true));
            assertThat(cursor.getString(cursor.getColumnIndexOrThrow("type")), is("Flat"));
        }
    }

    // ---

    private ContentValues generateItem() {
        final ContentValues values = new ContentValues();
        values.put("type", "Flat");
        values.put("area", "Southampton");
        values.put("price", "12000000");
        values.put("userId", "1");
        return values;
    }
}
