package com.example.mytravellerapp.mvp.presenters;

import com.example.mytravellerapp.model.entities.request.ForgotPasswordRequest;
import com.example.mytravellerapp.model.entities.request.LoginRequest;
import com.example.mytravellerapp.model.entities.request.RegisterFcmTokenRequest;
import com.example.mytravellerapp.model.entities.request.RegistrationRequest;
import com.example.mytravellerapp.model.entities.request.ResetPasswordRequest;
import com.example.mytravellerapp.model.entities.request.UpdateMyDetailRequest;

import okhttp3.MultipartBody;

public interface UserPresenter extends Presenter{
    void doLogin(LoginRequest loginRequest);
    void doRegister(RegistrationRequest registrationRequest);
    void getProfileDetails (String userId);
    void doUpdateProfile(UpdateMyDetailRequest updateMyDetailRequest, String userId);
    void doLogOut();
    void doUploadProfileImage(MultipartBody.Part[] profileImage, String userId);
    void doRegisterFcmToken(RegisterFcmTokenRequest registerFcmTokenRequest);
    void doForgotPassword(ForgotPasswordRequest forgotPasswordRequest);
    void doResetPassword(ResetPasswordRequest resetPasswordRequest);
    void doValidateResetCode(String resetToken);


}
