package com.example.mytravellerapp.mvp.presenters;

import com.example.mytravellerapp.model.entities.request.UpdateReadStatusRequest;

public interface NotificationPresenter extends Presenter {
    void getNotification(int count, int page);
    void getNotificationCount();
    void updateNotificationReadStatus(UpdateReadStatusRequest updateReadStatusRequest);

}
