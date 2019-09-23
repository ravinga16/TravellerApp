package com.example.mytravellerapp.mvp.presenters;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;


import com.example.mytravellerapp.BaseApplication;
import com.example.mytravellerapp.common.constants.ApplicationConstants;
import com.example.mytravellerapp.common.constants.IPreferencesKeys;
import com.example.mytravellerapp.domain.Service;
import com.example.mytravellerapp.model.rest.exceptions.RetrofitException;
import com.example.mytravellerapp.mvp.views.View;
import com.example.mytravellerapp.utils.IScheduler;

import io.reactivex.disposables.CompositeDisposable;

import static com.example.mytravellerapp.common.constants.IPreferencesKeys.USER_ID;


public abstract class BasePresenter implements Presenter {
    protected Activity activity;
    protected Service mService;
    protected CompositeDisposable disposable = new CompositeDisposable();

    protected IScheduler scheduler;
    protected View mView;

    protected SharedPreferences preferences;
    private String access_token;
    private String user_id;

    protected BasePresenter(Activity activityContext, Service pService, IScheduler scheduler){
        this.activity = activityContext;
        this.mService = pService;
        this.scheduler = scheduler;

        this.preferences = activityContext.getSharedPreferences(activityContext.getPackageName(), Context.MODE_PRIVATE);
        this.access_token = preferences.getString(IPreferencesKeys.ACCESS_TOKEN, "");
    }


    public String getAccessToken() {
        if (access_token == null || access_token.equals("")) {
            Context mContext = BaseApplication.getBaseApplication();
            SharedPreferences preferences = mContext.getSharedPreferences(mContext.getPackageName(), Context.MODE_PRIVATE);
            String token = "Bearer " + preferences.getString(IPreferencesKeys.ACCESS_TOKEN, "");
            return token;
        } else {
            return "Bearer " + access_token;
        }
    }

    public String getExceptionMessage(Throwable e) {
        if (((RetrofitException) e).getKind() == RetrofitException.Kind.NETWORK) {
            return ApplicationConstants.ERROR_MSG_REST_NETWORK;
        } else if (((RetrofitException) e).getKind() == RetrofitException.Kind.HTTP) {
            return ApplicationConstants.ERROR_MSG_REST_HTTP;
        } else if (((RetrofitException) e).getKind() == RetrofitException.Kind.UNEXPECTED) {
            return ApplicationConstants.ERROR_MSG_REST_UNEXPECTED;
        }
        return null;
    }

    public String getUserId() {
        SharedPreferences prefs = BaseApplication.getBaseApplication().getSharedPreferences("com.eyepax.traveller", Context.MODE_PRIVATE);
        String pref = prefs.getString(USER_ID,"default");
        return pref;
    }

    @Override
    public void onCreate() {}

    @Override
    public void onStart() {}

    @Override
    public void onStop() {}

    @Override
    public void attachView(View v) {
        mView =  v;
    }

    @Override
    public void onDestroy() {
        disposable.dispose();
    }
}
