package com.example.mytravellerapp.domain;

import com.example.mytravellerapp.model.entities.request.LoginRequest;
import com.example.mytravellerapp.model.entities.response.LoginResponse;

import io.reactivex.Single;
import retrofit2.Call;

public interface UserService extends Service{
    Single<LoginResponse> doLoginService(LoginRequest loginRequest);
}
