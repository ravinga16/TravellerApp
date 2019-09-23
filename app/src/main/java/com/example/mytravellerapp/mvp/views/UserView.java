package com.example.mytravellerapp.mvp.views;


import com.example.mytravellerapp.model.entities.response.LoginResponse;

public interface UserView extends View {
    void showLoginResponse(LoginResponse loginResponse);
}
