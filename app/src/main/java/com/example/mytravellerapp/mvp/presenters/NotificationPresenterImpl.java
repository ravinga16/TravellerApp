package com.example.mytravellerapp.mvp.presenters;

import android.app.Activity;
import android.util.Log;

import com.example.mytravellerapp.common.constants.ApplicationConstants;
import com.example.mytravellerapp.domain.NotificationService;
import com.example.mytravellerapp.domain.Service;
import com.example.mytravellerapp.model.entities.request.UpdateReadStatusRequest;
import com.example.mytravellerapp.model.entities.response.NotificationCountResponse;
import com.example.mytravellerapp.model.entities.response.NotificationResponse;
import com.example.mytravellerapp.model.entities.response.UpdateReadStatusResponse;
import com.example.mytravellerapp.model.rest.exceptions.RetrofitException;
import com.example.mytravellerapp.mvp.views.NotificationView;
import com.example.mytravellerapp.mvp.views.View;
import com.example.mytravellerapp.utils.IScheduler;

import java.io.IOException;

import io.reactivex.Single;
import io.reactivex.observers.DisposableSingleObserver;


public class NotificationPresenterImpl extends BasePresenter implements NotificationPresenter {

    private final static String TAG = "ToursPresenterImpl";

    private NotificationView mNotificationView;

    public NotificationPresenterImpl(Activity activityContext, Service userAccountService, IScheduler scheduler) {
        super(activityContext, userAccountService, scheduler);
    }

    @Override
    public void getNotificationCount() {
        disposable.add(getNotificationCountObservable().subscribeWith(getNotificationCountSubscriber()));
    }

    @Override
    public void getNotification(int count, int page) {
        disposable.add(getNotificationObservable(count, page).subscribeWith(getNotificationSubscriber()));
    }
    @Override
    public void updateNotificationReadStatus(UpdateReadStatusRequest updateReadStatusRequest) {
        disposable.add(updateNotificationReadStatusObservable(updateReadStatusRequest).subscribeWith(updateNotificationReadStatusSubscriber()));
    }

    @Override
    public void attachView(View v) {
        if (v instanceof NotificationView) {
            mNotificationView = (NotificationView) v;
            mView = mNotificationView;
        }
    }


    public Single<UpdateReadStatusResponse> updateNotificationReadStatusObservable(UpdateReadStatusRequest updateReadStatusRequest) {
        try {
            return getService().updateReadStatusService(getAccessToken(), updateReadStatusRequest)
                    .subscribeOn(scheduler.backgroundThread())
                    .observeOn(scheduler.mainThread());

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }


    public DisposableSingleObserver<UpdateReadStatusResponse> updateNotificationReadStatusSubscriber() {
        return new DefaultSubscriber<UpdateReadStatusResponse>(this.mView) {

            @Override
            public void onError(Throwable e) {
                try {
                    RetrofitException error = (RetrofitException) e;
                    if (error.getKind() == RetrofitException.Kind.EXPIRED){
                        UpdateReadStatusResponse exceptionResponse = new UpdateReadStatusResponse();
                        exceptionResponse.setSuccess(false);
                        exceptionResponse.setTokenExpired(true);
                        exceptionResponse.setMessage(ApplicationConstants.ERROR_MSG_TOKEN_EXPIRED);
                        mNotificationView.showUpdateNotificationReadStatusResponse((exceptionResponse));
                    } else {
                        UpdateReadStatusResponse response = error.getErrorBodyAs(UpdateReadStatusResponse.class);
                        if (response == null) {
                            response = new UpdateReadStatusResponse();
                            response.setMessage(getExceptionMessage(e));
                            response.setAPIError(false);
                        } else {
                            response.setAPIError(true);
                        }
                        response.setSuccess(false);
                        mNotificationView.showUpdateNotificationReadStatusResponse(response);
                    }
                } catch (IOException ex) {
                    UpdateReadStatusResponse exceptionResponse = new UpdateReadStatusResponse();
                    exceptionResponse.setSuccess(false);
                    exceptionResponse.setAPIError(false);
                    exceptionResponse.setMessage(ApplicationConstants.ERROR_MSG_REST_UNEXPECTED);
                    mNotificationView.showUpdateNotificationReadStatusResponse(exceptionResponse);

                    ex.printStackTrace();
                }
            }

            @Override
            public void onSuccess(UpdateReadStatusResponse response) {
                if (response != null) {
                    response.setSuccess(true);
                    mNotificationView.showUpdateNotificationReadStatusResponse(response);
                }
            }
        };
    }
    /*--------------------------------------------------------------*/

    public Single<NotificationResponse> getNotificationObservable(int count, int page) {
        try {
            return getService(). getNotificationService(getAccessToken(), count, page)
                    .subscribeOn(scheduler.backgroundThread())
                    .observeOn(scheduler.mainThread());

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }
    public DisposableSingleObserver<NotificationResponse> getNotificationSubscriber() {
        return new DefaultSubscriber<NotificationResponse>(this.mView) {

            @Override
            public void onError(Throwable e) {
                try {
                    RetrofitException error = (RetrofitException) e;
                    if (error.getKind() == RetrofitException.Kind.EXPIRED){
                        NotificationResponse exceptionResponse = new NotificationResponse();
                        exceptionResponse.setSuccess(false);
                        exceptionResponse.setTokenExpired(true);
                        exceptionResponse.setMessage(ApplicationConstants.ERROR_MSG_TOKEN_EXPIRED);
                        mNotificationView.showNotificationResponse((exceptionResponse));
                    } else {
                        NotificationResponse response = error.getErrorBodyAs(NotificationResponse.class);
                        if (response == null) {
                            response = new NotificationResponse();
                            response.setMessage(getExceptionMessage(e));
                            response.setAPIError(false);
                        } else {
                            response.setAPIError(true);
                        }
                        response.setSuccess(false);
                        mNotificationView.showNotificationResponse(response);
                    }
                } catch (IOException ex) {
                    NotificationResponse exceptionResponse = new NotificationResponse();
                    exceptionResponse.setSuccess(false);
                    exceptionResponse.setAPIError(false);
                    exceptionResponse.setMessage(ApplicationConstants.ERROR_MSG_REST_UNEXPECTED);
                    mNotificationView.showNotificationResponse(exceptionResponse);

                    ex.printStackTrace();
                }
            }

            @Override
            public void onSuccess(NotificationResponse response) {
                if (response != null) {
                    response.setSuccess(true);
                    mNotificationView.showNotificationResponse(response);
                }
            }
        };
    }
 /*-------------------------------------*/

    public DisposableSingleObserver<NotificationCountResponse> getNotificationCountSubscriber() {
        return new DefaultSubscriber<NotificationCountResponse>(this.mView) {

            @Override
            public void onError(Throwable e) {
                try {
                    RetrofitException error = (RetrofitException) e;
                    if (error.getKind() == RetrofitException.Kind.EXPIRED){
                        NotificationCountResponse exceptionResponse = new NotificationCountResponse();
                        exceptionResponse.setSuccess(false);
                        exceptionResponse.setTokenExpired(true);
                        exceptionResponse.setMessage(ApplicationConstants.ERROR_MSG_TOKEN_EXPIRED);
                        mNotificationView.showNotificationCountResponse((exceptionResponse));
                    } else {
                        NotificationCountResponse response = error.getErrorBodyAs(NotificationCountResponse.class);
                        if (response == null) {
                            response = new NotificationCountResponse();
                            response.setMessage(getExceptionMessage(e));
                            response.setAPIError(false);
                        } else {
                            response.setAPIError(true);
                        }
                        response.setSuccess(false);
                        mNotificationView.showNotificationCountResponse(response);
                    }
                } catch (IOException ex) {
                    NotificationCountResponse exceptionResponse = new NotificationCountResponse();
                    exceptionResponse.setSuccess(false);
                    exceptionResponse.setAPIError(false);
                    exceptionResponse.setMessage(ApplicationConstants.ERROR_MSG_REST_UNEXPECTED);
                    mNotificationView.showNotificationCountResponse(exceptionResponse);

                    ex.printStackTrace();
                }
            }

            @Override
            public void onSuccess(NotificationCountResponse response) {
                if (response != null) {
                    response.setSuccess(true);
                    mNotificationView.showNotificationCountResponse(response);
                }
            }
        };
    }

    public Single<NotificationCountResponse> getNotificationCountObservable() {
        try {
            return getService(). getNotificationCountService(getAccessToken())
                    .subscribeOn(scheduler.backgroundThread())
                    .observeOn(scheduler.mainThread());

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    private NotificationService getService() {
        return (NotificationService) mService;
    }


}