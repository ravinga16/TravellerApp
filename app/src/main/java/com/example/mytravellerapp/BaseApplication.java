package com.example.mytravellerapp;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.example.mytravellerapp.common.constants.IPreferencesKeys;
import com.example.mytravellerapp.ui.activities.LoginActivity;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

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
        initImageLoader(getApplicationContext());
    }

    public static void initImageLoader(Context context) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(false)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .build();
        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by
        //  ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.defaultDisplayImageOptions(options);

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());
    }

    public BaseApplication() {
        super();
    }

    public static BaseApplication getBaseApplication() {
        return baseApplication;
    }

    public void logOut(Context context) {
        try {
            preferences.edit().remove(IPreferencesKeys.ACCESS_TOKEN).apply();
            preferences.edit().remove(IPreferencesKeys.USER_ID).apply();
            preferences.edit().remove(IPreferencesKeys.IS_REGISTER_FCM).apply();
            Intent intent = new Intent(context, LoginActivity.class);
            context.startActivity(intent);
            ((Activity) context).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            ((Activity) context).finish();
        } catch (Exception e) {
            Log.e(TAG, "exTokenClearData: " + e.toString());
        }
    }

    public void exTokenClearData(Context context) {
        try {
            preferences.edit().remove(IPreferencesKeys.ACCESS_TOKEN).apply();
            Intent intent = new Intent(context, LoginActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("TOKEN_EX_MSG", "TOKEN_EX_MSG");
            intent.putExtras(bundle);
            context.startActivity(intent);
            ((Activity) context).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            ((Activity) context).finish();
        } catch (Exception e) {
            Log.e(TAG, "exTokenClearData: " + e.toString());
        }
    }
}
