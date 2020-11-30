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

import io.reactivex.Single;
import okhttp3.MultipartBody;
import retrofit2.Call;

public interface UserService extends Service{
    Single<LoginResponse> doLoginService(LoginRequest loginRequest);
    Single<ProfileResponse> getProfileDetailsService(String accessToken, String userId);
    Single<ProfileUpdateResponse> doUpdateProfileService(String accessToken, UpdateMyDetailRequest updateMyDetailRequest, String userId);
    Single<LogOutResponse> doLogOutService(String accessToken);
    Single<UploadProfileImageResponse> doUploadProfileImageService(String accessToken, MultipartBody.Part[] profileImage, String userId);
    Single<RegisterFcmResponse> doRegisterFcmService(String accessToken, RegisterFcmTokenRequest registerFcmTokenRequest);
    Single<RegisterResponse> doRegisterService(RegistrationRequest registrationRequest);
    Single<BaseServerResponse> doForgotPasswordService(ForgotPasswordRequest forgotPasswordRequest);
    Single<BaseServerResponse> doResetPasswordService(ResetPasswordRequest resetPasswordRequest);
    Single<ValidateResetCodeResponse> doValidateResetCodeService(String resetToken);

}
