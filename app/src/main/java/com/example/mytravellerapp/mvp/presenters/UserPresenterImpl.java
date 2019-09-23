package com.example.mytravellerapp.mvp.presenters;

import android.app.Activity;
import android.util.Log;

import com.example.mytravellerapp.common.constants.ApplicationConstants;
import com.example.mytravellerapp.domain.Service;
import com.example.mytravellerapp.domain.UserService;
import com.example.mytravellerapp.model.entities.request.LoginRequest;
import com.example.mytravellerapp.model.entities.response.LoginResponse;
import com.example.mytravellerapp.model.rest.exceptions.RetrofitException;
import com.example.mytravellerapp.mvp.views.UserView;
import com.example.mytravellerapp.mvp.views.View;
import com.example.mytravellerapp.utils.IScheduler;

import java.io.IOException;

import io.reactivex.Single;
import io.reactivex.observers.DisposableSingleObserver;

public class UserPresenterImpl extends BasePresenter implements UserPresenter {
    private final static String TAG = "UserPresenterImpl";

    private UserView mUserView;

    public UserPresenterImpl(Activity activityContext, Service userAccountService, IScheduler scheduler) {
        super(activityContext, userAccountService, scheduler);
    }

    @Override
    public void doLogin(LoginRequest loginRequest) {
        disposable.add(doLoginObservable(loginRequest).subscribeWith(doLoginSubscriber()));
    }


    @Override
    public void attachView(View v) {
        if (v instanceof UserView) {
            mUserView = (UserView) v;
            mView = mUserView;
        }
    }

    public DisposableSingleObserver<LoginResponse> doLoginSubscriber() {
        return new DefaultSubscriber<LoginResponse>(this.mView) {

            @Override
            public void onError(Throwable e) {
                try {
                    RetrofitException error = (RetrofitException) e;
                    if (error.getKind() == RetrofitException.Kind.EXPIRED){
                        LoginResponse exceptionResponse = new LoginResponse();
                        exceptionResponse.setSuccess(false);
                        exceptionResponse.setTokenExpired(true);
                        exceptionResponse.setMessage(ApplicationConstants.ERROR_MSG_TOKEN_EXPIRED);
                        mUserView.showLoginResponse(exceptionResponse);
                    } else {
                        LoginResponse response = error.getErrorBodyAs(LoginResponse.class);
                        if (response == null) {
                            response = new LoginResponse();
                            response.setMessage(getExceptionMessage(e));
                            response.setAPIError(false);
                        } else {
                            response.setAPIError(true);
                        }
                        response.setSuccess(false);
                        mUserView.showLoginResponse(response);
                    }
                } catch (IOException ex) {
                    LoginResponse exceptionResponse = new LoginResponse();
                    exceptionResponse.setSuccess(false);
                    exceptionResponse.setAPIError(false);
                    exceptionResponse.setMessage(ApplicationConstants.ERROR_MSG_REST_UNEXPECTED);
                    mUserView.showLoginResponse(exceptionResponse);

                    ex.printStackTrace();
                }
            }

            @Override
            public void onSuccess(LoginResponse response) {
                if (response != null) {
                    response.setSuccess(true);
                    mUserView.showLoginResponse(response);
                }
            }
        };
    }

    public Single<LoginResponse> doLoginObservable(LoginRequest loginRequest) {
        try {
            System.out.println("");
            return getService().doLoginService(loginRequest)
                    .subscribeOn(scheduler.backgroundThread())
                    .observeOn(scheduler.mainThread());


        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }



    private UserService getService() {
        return (UserService) mService;
    }
}

