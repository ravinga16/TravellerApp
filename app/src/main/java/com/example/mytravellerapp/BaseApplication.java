package com.example.mytravellerapp;

import android.app.Application;
import android.content.SharedPreferences;

import lombok.Getter;
import lombok.Setter;

public class BaseApplication extends Application {

    //singleton class
    private static BaseApplication baseApplication;
    private final String TAG = BaseApplication.this.getClass().getSimpleName();
    @Getter
    @Setter
    private String user_mail = null;
    private SharedPreferences preferences;

    @Override
    public void onCreate() {
        super.onCreate();

        baseApplication = (BaseApplication) getApplicationContext();
        preferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);

    }

    public BaseApplication() {
        super();
    }

    public static BaseApplication getBaseApplication() {
        return baseApplication;
    }
}
