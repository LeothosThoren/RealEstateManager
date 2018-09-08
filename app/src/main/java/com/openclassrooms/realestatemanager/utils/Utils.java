package com.openclassrooms.realestatemanager.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Philippe on 21/02/2018.
 */

public class Utils {

    /**
     * Conversion d'un prix d'un bien immobilier (Dollars vers Euros)
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     *
     * @param dollars
     * @return
     */
    public static int convertDollarToEuro(int dollars) {
        return (int) Math.round(dollars * 0.88);
    }

    /**
     * Conversion d'un prix d'un bien immobilier (Euros vers Dollars)
     *
     * @param euros
     * @return
     */
    public static int convertEurosToDollars(int euros) {
        return (int) Math.round(euros * 1.14);
    }

    /**
     * Conversion de la date d'aujourd'hui en un format plus approprié
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     *
     * @return
     */
    public static String getTodayDate() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return dateFormat.format(new Date());
    }

    /**
     * Vérification de la connexion réseau
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     *
     * @param context
     * @param TAG
     * @return
     */
    public static Boolean isInternetAvailable(Context context, String TAG) {
        // Instead of WifiManager only we check both wifi and mobile connectivity with connectivity manager
        ConnectivityManager connMgr = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeInfo = null;
        boolean wifiConnected = false;
        boolean mobileConnected = false;
        boolean isNetworkAvailable = false;
        if (connMgr != null) {
            activeInfo = connMgr.getActiveNetworkInfo();
        }

        if (activeInfo != null && activeInfo.isConnected()) {
            wifiConnected = activeInfo.getType() == ConnectivityManager.TYPE_WIFI;
            mobileConnected = activeInfo.getType() == ConnectivityManager.TYPE_MOBILE;
            if (wifiConnected) {
                Log.i(TAG, "Wifi connection");
                Toast.makeText(context, "Wifi connection is available", Toast.LENGTH_SHORT).show();
            } else if (mobileConnected) {
                Log.i(TAG, "Mobile connection");
                Toast.makeText(context, "Mobile connection is available", Toast.LENGTH_SHORT).show();
            }
        } else {
            Log.i(TAG, "No wifi or mobile connection");
        }
        isNetworkAvailable = (wifiConnected || mobileConnected);

        return isNetworkAvailable;
    }


    public static String convertPriceToString(long price) {
        DecimalFormat formatter = new DecimalFormat("#,###",new DecimalFormatSymbols(Locale.US));
        return formatter.format(price);
    }

    // To comment
    public static Date getDateFromDatePicker(int day, int month, int year) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String formatedDate = sdf.format(calendar.getTime());
        return sdf.parse(formatedDate);
    }
//
    public static String checkDigit(int number) {
        return number <= 9 ? "0" + number : String.valueOf(number);
    }
//
    public static String formatAddress(int nb, String line1, String line2, String city,
                                       String state, String zip) {
        String address = (String.valueOf(nb) + " " + line1 + " " + line2 + " " + city + " " + state
                + " " + zip).toLowerCase();
        return address.replace(" ", "+");
    }

    public static String formatToString(int i) {
        return String.valueOf(i);
    }

}
