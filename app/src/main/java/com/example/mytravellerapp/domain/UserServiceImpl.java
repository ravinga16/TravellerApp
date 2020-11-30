package com.example.mytravellerapp.domain;

import com.example.mytravellerapp.model.entities.request.ForgotPasswordRequest;
import com.example.mytravellerapp.model.entities.request.LoginRequest;
import com.example.mytravellerapp.model.entities.request.RegisterFcmTokenRequest;
import com.example.mytravellerapp.model.entities.request.RegistrationRequest;
import com.example.mytravellerapp.model.entities.request.ResetPasswordRequest;
import com.example.mytravellerapp.model.entities.request.UpdateMyDetailRequest;
import com.example.mytravellerapp.model.entities.response.BaseServerResponse;
import com.example.mytravellerapp.model.entities.response.LogOutResponse;
import com.example.mytravellerapp.model.entities.response.LoginResponse;
import com.example.mytravellerapp.model.entities.response.ProfileResponse;
import com.example.mytravellerapp.model.entities.response.ProfileUpdateResponse;
import com.example.mytravellerapp.model.entities.response.RegisterFcmResponse;
import com.example.mytravellerapp.model.entities.response.RegisterResponse;
import com.example.mytravellerapp.model.entities.response.UploadProfileImageResponse;
import com.example.mytravellerapp.model.entities.response.ValidateResetCodeResponse;
import com.example.mytravellerapp.model.rest.BMSService;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import okhttp3.MultipartBody;
import retrofit2.Call;

public class UserServiceImpl implements UserService{

    private BMSService bmsService;


    public UserServiceImpl(BMSService bmsService) {
        super();
        this.bmsService = bmsService;
    }

    @Override
    public Single<RegisterResponse> doRegisterService(RegistrationRequest registrationRequest) {
        return bmsService.getApi()
                .doRegisterAPI(registrationRequest)
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Single<LoginResponse> doLoginService(LoginRequest loginRequest) {

        return bmsService.getApi()
                .doLoginAPI(loginRequest)
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Single<ProfileResponse> getProfileDetailsService(String accessToken, String userId) {
        return bmsService.getApi()
                .getProfileDetailsAPI(accessToken, userId)
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Single<ProfileUpdateResponse> doUpdateProfileService(String accessToken, UpdateMyDetailRequest updateMyDetailRequest, String userId) {
        return bmsService.getApi()
                .doUpdateProfileAPI(accessToken, updateMyDetailRequest, userId)
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Single<LogOutResponse> doLogOutService(String accessToken) {
        return bmsService.getApi()
                .doLogOutAPI(accessToken)
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Single<UploadProfileImageResponse> doUploadProfileImageService(String accessToken, MultipartBody.Part[] profileImage, String userId) {
        return bmsService.getApi()
                .doUploadProfileImageAPI(accessToken, profileImage, userId)
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Single<RegisterFcmResponse> doRegisterFcmService(String accessToken, RegisterFcmTokenRequest registerFcmTokenRequest) {
        return bmsService.getApi()
                .doRegisterFcmAPI(accessToken, registerFcmTokenRequest)
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Single<BaseServerResponse> doForgotPasswordService(ForgotPasswordRequest forgotPasswordRequest) {
        return bmsService.getApi()
                .doForgotPasswordAPI(forgotPasswordRequest)
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Single<BaseServerResponse> doResetPasswordService(ResetPasswordRequest resetPasswordRequest) {
        return bmsService.getApi()
                .doResetPasswordAPI(resetPasswordRequest)
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Single<ValidateResetCodeResponse> doValidateResetCodeService(String resetToken) {
        return bmsService.getApi()
                .doValidateResetCodeAPI(resetToken)
                .observeOn(AndroidSchedulers.mainThread());
    }

}
