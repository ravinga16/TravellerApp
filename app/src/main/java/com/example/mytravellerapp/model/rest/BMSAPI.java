package com.example.mytravellerapp.model.rest;

import com.example.mytravellerapp.model.entities.request.LoginRequest;
import com.example.mytravellerapp.model.entities.response.LoginResponse;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface BMSAPI {
    @POST("/user/login")
    Single<LoginResponse> doLoginAPI(
            @Body LoginRequest loginRequest);
}
