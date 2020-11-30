package com.example.mytravellerapp.domain;

import com.example.mytravellerapp.model.entities.request.UpdateReadStatusRequest;
import com.example.mytravellerapp.model.entities.response.NotificationCountResponse;
import com.example.mytravellerapp.model.entities.response.NotificationResponse;
import com.example.mytravellerapp.model.entities.response.UpdateReadStatusResponse;

import io.reactivex.Single;

public interface NotificationService extends Service {
    Single<NotificationCountResponse> getNotificationCountService(String accessToken);
    Single<NotificationResponse> getNotificationService(String token, int count, int page);
    Single<UpdateReadStatusResponse> updateReadStatusService(String accessToken, UpdateReadStatusRequest updateReadStatusRequest);

}
