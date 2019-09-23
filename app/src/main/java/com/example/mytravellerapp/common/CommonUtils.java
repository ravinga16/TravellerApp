package com.example.mytravellerapp.common;

import android.content.Context;
import android.net.ConnectivityManager;

import com.example.mytravellerapp.BaseApplication;

public class CommonUtils {
    private static CommonUtils instance = null;

    private CommonUtils() {}

    public static CommonUtils getInstance() {
        if (instance == null) instance = new CommonUtils();
        return instance;
    }

    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) BaseApplication.getBaseApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
}
