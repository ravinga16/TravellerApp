package com.example.mytravellerapp.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.example.mytravellerapp.R;
import com.example.mytravellerapp.common.constants.IPreferencesKeys;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {
    private static final String TAG = "SplashActivity";
    private static int SPLASH_TIME_OUT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        final ImageView imageView = findViewById(R.id.image_splash);

        final SharedPreferences preferences = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);

//
//        new Handler().postDelayed(new Runnable() {
//
//            /*
//             * Showing splash screen with a timer. This will be useful when you
//             * want to show case your app logo / company
//             */
//
//            @Override
//            public void run() {
//                // This method will be executed once the timer is over
//                // Start your app main activity
//                Class classObj = (preferences.contains(IPreferencesKeys.ACCESS_TOKEN)) ? MainActivity.class : LoginActivity.class;
////                startActivity(new Intent(SplashActivity.this, classObj));
////                finish();
//                startActivity(new Intent(SplashActivity.this, classObj));
//                // close this activity
//                finish();
//            }
//        }, SPLASH_TIME_OUT);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Class classObj = (preferences.contains(IPreferencesKeys.ACCESS_TOKEN)) ? MainActivity.class : LoginActivity.class;
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        Intent intent = new Intent(SplashActivity.this, classObj);
                        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                                SplashActivity.this, imageView, ViewCompat.getTransitionName(imageView));
                        startActivity(intent, options.toBundle());
//                        startActivity(new Intent(SplashActivity.this, classObj));
                        finish();
                    }
                });
            }
        }, SPLASH_TIME_OUT);
    }
}
