package com.example.mytravellerapp.mvp.presenters;

import android.util.Log;


import com.example.mytravellerapp.mvp.views.View;

import io.reactivex.observers.DisposableSingleObserver;

public class DefaultSubscriber<T>  extends DisposableSingleObserver<T> {
    private String TAG="DefaultSubscriber";
    private View mView;

    public DefaultSubscriber(View pView){
        mView = pView;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onSuccess(T t) {

    }

    @Override
    public void onError(Throwable throwable) {
        if(mView != null) {
            mView.showMessage(throwable.getLocalizedMessage());
        }
        throwable.printStackTrace();
        Log.e(TAG, "Error Occurred while retrieving List: " + throwable.getStackTrace());
    }
}
