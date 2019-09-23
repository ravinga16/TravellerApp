package com.example.mytravellerapp.domain;

import com.example.mytravellerapp.model.entities.request.LoginRequest;
import com.example.mytravellerapp.model.entities.response.LoginResponse;
import com.example.mytravellerapp.model.rest.BMSService;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import retrofit2.Call;

public class UserServiceImpl implements UserService{

    private BMSService bmsService;


    public UserServiceImpl(BMSService bmsService) {
        super();
        this.bmsService = bmsService;
    }

    @Override
    public Single<LoginResponse> doLoginService(LoginRequest loginRequest) {
        System.out.println("");
        return bmsService.getApi()
                .doLoginAPI(loginRequest)
                .observeOn(AndroidSchedulers.mainThread());
    }
}
