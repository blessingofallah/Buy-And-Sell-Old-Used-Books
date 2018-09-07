package com.baniya.pintooaggarwal.bookbuyselling;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Pintoo Aggarwal on 30-08-2017.
 */

public class Utility {
    public static boolean isNetworkAvailable(Context context)
    {
        ConnectivityManager connectivityManager
                =(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo !=null && activeNetworkInfo.isConnected();
    }
}

