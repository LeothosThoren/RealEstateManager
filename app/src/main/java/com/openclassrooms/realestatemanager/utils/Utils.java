package com.openclassrooms.realestatemanager.utils;

import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.openclassrooms.realestatemanager.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
            } else if (mobileConnected) {
                Log.i(TAG, "Mobile connection");
            }
        } else {
            Log.i(TAG, "No wifi or mobile connection");
        }
        isNetworkAvailable = (wifiConnected || mobileConnected);

        return isNetworkAvailable;
    }

    //Todo
    public static String convertPriceToString(long price) {
        return String.valueOf(price);
    }

    // To comment
    public static String getDateTime(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return dateFormat.format(date);
    }
}
