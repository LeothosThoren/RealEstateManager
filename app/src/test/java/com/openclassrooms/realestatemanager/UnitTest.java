package com.openclassrooms.realestatemanager;

import com.openclassrooms.realestatemanager.utils.Utils;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class UnitTest {

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
        assertEquals("10/09/2018", Utils.getTodayDate());
        assertEquals("09/10/2018", Utils.getFormattedDate(new Date(), "MM/dd/yyyy"));
    }


    //===================
    // STRING
    //===================

    @Test
    public void stringConverter()throws Exception {
        assertEquals("75+gresham+street+++victoria+park+australia+wa+6100",
                Utils.formatAddress("Gresham Street ","", "Victoria Park", "AUSTRALIA", "WA 6100"));

        assertEquals("251,500,890", Utils.convertPriceToString(251500890) );

        assertEquals("7500", Utils.formatToString(7500));
    }


}