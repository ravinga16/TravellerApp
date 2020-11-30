package com.example.mytravellerapp.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.mytravellerapp.R;
import com.example.mytravellerapp.common.constants.IPreferencesKeys;
import com.example.mytravellerapp.dto.NotificationBody;
import com.example.mytravellerapp.ui.activities.BaseActivity;
import com.example.mytravellerapp.ui.activities.MainActivity;
import com.example.mytravellerapp.ui.fragments.HomeFragment;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import org.json.JSONObject;

import java.util.Map;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMessagingServ";
    public static final String TOKEN_BROADCAST = "myfcmtokenbroadcast";

    public static  final String TOKEN_NOTIFICATION = "tokennotification";
    public MyFirebaseMessagingService() {
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        getApplicationContext().sendBroadcast(new Intent(TOKEN_NOTIFICATION));
        if (remoteMessage.getData().size() > 0) {
            try {
//                JSONObject json = new JSONObject(remoteMessage.getData());
                sendPushNotification(remoteMessage);
            } catch (Exception e) {
                Log.e(TAG, "Exception 01: " + e.getMessage());
            }
        }
    }

    private void sendPushNotification(RemoteMessage remoteMessage) {
        try {
            //getting the json data
            Map<String, String> data = remoteMessage.getData();
            Gson gson = new Gson();
            JsonElement jsonElement = gson.toJsonTree(data);
            NotificationBody notificationBody = gson.fromJson(jsonElement, NotificationBody.class);
            //parsing json data
            String title = data.get("andoridContent");
            String message = data.get("date");
            String imageUrl = data.get("image");
//            creating MyNotificationManager object
            MyNotificationManager mNotificationManager = new MyNotificationManager(getApplicationContext());

//            creating an intent for the notification

//            if there is no image
            if (true) {
                //displaying small notification
                if (BaseActivity.isAppWentToBg) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra(IPreferencesKeys.PUSH_DATA, notificationBody);
                    mNotificationManager.showSmallNotification(title, message, intent);
                } else {
                    Intent intent = new Intent("com.example.mytravellerapp_FCM-MESSAGE");
                    intent.putExtra(IPreferencesKeys.PUSH_DATA, notificationBody);
                    LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
                    localBroadcastManager.sendBroadcast(intent);
                }
            } else {
                //if there is an image
                //displaying a big notification
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra(IPreferencesKeys.PUSH_DATA, notificationBody);
                mNotificationManager.showBigNotification(title, message, imageUrl, intent);
            }
        } catch (Exception e) {
            Log.e(TAG, "Exception 02: " + e.getMessage());
        }

    }

    @Override
    public void onNewToken(String refreshedToken) {
        super.onNewToken(refreshedToken);
        //Notify the main activity
        getApplicationContext().sendBroadcast(new Intent(TOKEN_BROADCAST));
        //calling the method store token and passing token
        storeToken(refreshedToken);
    }

    private void storeToken(String token) {
        //we will save the token in shared preferences later
        SharedPrefManager.getInstance(getApplicationContext()).saveDeviceToken(token);
    }


}

