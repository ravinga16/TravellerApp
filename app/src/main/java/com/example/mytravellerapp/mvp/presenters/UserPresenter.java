package com.example.mytravellerapp.mvp.presenters;

import com.example.mytravellerapp.model.entities.request.LoginRequest;

public interface UserPresenter extends Presenter{
    void doLogin(LoginRequest loginRequest);
}
