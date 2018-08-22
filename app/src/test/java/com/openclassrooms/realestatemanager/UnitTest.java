package com.openclassrooms.realestatemanager;

import com.openclassrooms.realestatemanager.utils.Utils;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class UnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    //===================
    // CONVERSIONS
    //===================

    @Test
    public void currencyConversion()throws Exception {
        assertEquals(88, Utils.convertDollarToEuro(100));
        assertEquals(143, Utils.convertEurosToDollars(125));
    }

    //===================
    // DATE
    //===================

    @Test
    public void dateConverter()throws Exception {
        //Don't forget to switch the current day date in the test
        assertEquals("15/08/2018", Utils.getTodayDate());
    }
}