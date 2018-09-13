package com.openclassrooms.realestatemanager;

import com.openclassrooms.realestatemanager.utils.Utils;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


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
        //Pattern like 13/09/2018 -> dd/MM/yyyy
        Pattern datePattern = Pattern.compile("^\\d{2}/\\d{2}/\\d{4}$");
        Matcher matcher = datePattern.matcher(Utils.getTodayDate());
        assertTrue(matcher.matches());

        //Pattern like 03/29/2018 -> MM/dd/yyyy
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        Date date = sdf.parse("03/29/2018");

        Pattern usDatePattern = Pattern.compile("^(0[1-9]|1[012])/(0[1-9]|[12][0-9]|3[01])/\\d{4}$");
        Matcher scdMatcher = usDatePattern.matcher(Utils.getFormattedDate(date, "MM/dd/yyyy"));
        assertTrue(scdMatcher.matches());

    }


    //===================
    // STRING
    //===================

    @Test
    public void stringConverter()throws Exception {
        assertEquals("75+gresham+street+victoria+park+australia+wa+6100",
                Utils.formatAddress("75 Gresham Street ", "Victoria Park", "AUSTRALIA", "WA 6100"));

        assertEquals("251,500,890", Utils.convertPriceToString(251500890) );
        assertEquals("7500", Utils.formatToString(7500));
    }


}