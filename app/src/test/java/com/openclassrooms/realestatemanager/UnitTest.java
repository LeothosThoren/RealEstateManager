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
        assertEquals("05/09/2018", Utils.getTodayDate());
    }

    //===================
    // STRING
    //===================

    @Test
    public void stringConverter()throws Exception {
        assertEquals("75+gresham+street+++victoria+park+australia+wa+6100",
                Utils.formatAddress(75, "Gresham Street ","", "Victoria Park", "AUSTRALIA", "WA 6100"));

        assertEquals("251,500,890", Utils.convertPriceToString(251500890) );
    }


}