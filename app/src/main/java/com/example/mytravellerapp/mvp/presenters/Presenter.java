package com.example.mytravellerapp.mvp.presenters;

import com.example.mytravellerapp.mvp.views.View;

public interface Presenter<T> {
    void onCreate();

    void onStart();

    void onStop();

    void onDestroy();

    void attachView(View v);
}
