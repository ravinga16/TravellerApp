package com.example.mytravellerapp.domain;

import com.example.mytravellerapp.model.entities.request.UpdateReadStatusRequest;
import com.example.mytravellerapp.model.entities.response.NotificationCountResponse;
import com.example.mytravellerapp.model.entities.response.NotificationResponse;
import com.example.mytravellerapp.model.entities.response.UpdateReadStatusResponse;
import com.example.mytravellerapp.model.rest.BMSService;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class NotificationServiceImpl implements NotificationService {
    private BMSService bmsService;

    public NotificationServiceImpl(BMSService bmsService) {
        super();
        this.bmsService = bmsService;
    }

    @Override
    public Single<NotificationCountResponse> getNotificationCountService(String accessToken) {
        return bmsService.getApi()
                .getNotificationCountAPI(accessToken)
                .observeOn(AndroidSchedulers.mainThread());
    }
    @Override
    public Single<NotificationResponse> getNotificationService(String token, int count, int page) {
        return bmsService.getApi()
                .getNotificationAPI(token)
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Single<UpdateReadStatusResponse> updateReadStatusService(String accessToken, UpdateReadStatusRequest updateReadStatusRequest) {
        return bmsService.getApi()
                .updateReadStatusAPI(accessToken, updateReadStatusRequest)
                .observeOn(AndroidSchedulers.mainThread());
    }


}
