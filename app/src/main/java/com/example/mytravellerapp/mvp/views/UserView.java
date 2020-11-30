package com.example.mytravellerapp.mvp.views;


import com.example.mytravellerapp.model.entities.response.BaseServerResponse;
import com.example.mytravellerapp.model.entities.response.LogOutResponse;
import com.example.mytravellerapp.model.entities.response.LoginResponse;
import com.example.mytravellerapp.model.entities.response.ProfileResponse;
import com.example.mytravellerapp.model.entities.response.ProfileUpdateResponse;
import com.example.mytravellerapp.model.entities.response.RegisterFcmResponse;
import com.example.mytravellerapp.model.entities.response.RegisterResponse;
import com.example.mytravellerapp.model.entities.response.UploadProfileImageResponse;
import com.example.mytravellerapp.model.entities.response.ValidateResetCodeResponse;

public interface UserView extends View {
    void showLoginResponse(LoginResponse loginResponse);
    void showProfileResponse(ProfileResponse profileResponse);
    void showUpdateProfileResponse(ProfileUpdateResponse profileUpdateResponse);
    void showLogOutResponse(LogOutResponse logOutResponse);
    void showUploadProfileImageResponse(UploadProfileImageResponse uploadProfileImageResponse);
    void showRegisterFcmResponse(RegisterFcmResponse registerFcmResponse);
    void showRegisterResponse(RegisterResponse registerResponse);
    void showForgotPasswordResponse(BaseServerResponse baseServerResponse);
    void showResetPasswordResponse(BaseServerResponse baseServerResponse);
    void showValidateResetCodeResponse(ValidateResetCodeResponse validateResetCodeResponse);

}
