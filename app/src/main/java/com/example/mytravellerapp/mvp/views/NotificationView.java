package com.example.mytravellerapp.mvp.views;

import com.example.mytravellerapp.model.entities.response.NotificationCountResponse;
import com.example.mytravellerapp.model.entities.response.NotificationResponse;
import com.example.mytravellerapp.model.entities.response.UpdateReadStatusResponse;

public interface NotificationView extends View {
    void showNotificationCountResponse(NotificationCountResponse notificationCountResponse);
    void showNotificationResponse(NotificationResponse notificationResponse);
    void showUpdateNotificationReadStatusResponse(UpdateReadStatusResponse updateReadStatusResponse);

}
