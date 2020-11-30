package com.example.mytravellerapp.mvp.presenters;

import android.app.Activity;
import android.util.Log;

import com.example.mytravellerapp.common.constants.ApplicationConstants;
import com.example.mytravellerapp.domain.Service;
import com.example.mytravellerapp.domain.UserService;
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
import com.example.mytravellerapp.model.rest.exceptions.RetrofitException;
import com.example.mytravellerapp.mvp.views.UserView;
import com.example.mytravellerapp.mvp.views.View;
import com.example.mytravellerapp.utils.IScheduler;

import java.io.IOException;

import io.reactivex.Single;
import io.reactivex.observers.DisposableSingleObserver;
import okhttp3.MultipartBody;

public class UserPresenterImpl extends BasePresenter implements UserPresenter {
    private final static String TAG = "UserPresenterImpl";

    private UserView mUserView;

    public UserPresenterImpl(Activity activityContext, Service userAccountService, IScheduler scheduler) {
        super(activityContext, userAccountService, scheduler);
    }

    @Override
    public void doRegister(RegistrationRequest registrationRequest) {
        disposable.add(doRegisterObservable(registrationRequest).subscribeWith(doRegisterSubscriber()));
    }

    @Override
    public void doLogin(LoginRequest loginRequest) {
        disposable.add(doLoginObservable(loginRequest).subscribeWith(doLoginSubscriber()));
    }

    @Override
    public void getProfileDetails(String userId) {
        disposable.add(getProfileDetailsObservable().subscribeWith(getProfileDetailsSubscriber()));
    }

    @Override
    public void doUpdateProfile(UpdateMyDetailRequest updateMyDetailRequest, String userId) {
        disposable.add(doUpdateProfileObservable(updateMyDetailRequest).subscribeWith(doUpdateProfileSubscriber()));

    }

    @Override
    public void doLogOut() {
        disposable.add(doLogOutObservable().subscribeWith(doLogOutSubscriber()));
    }

    @Override
    public void doUploadProfileImage(MultipartBody.Part[] profileImage, String userId) {
        disposable.add(doUploadProfileImageObservable(profileImage).subscribeWith(doUploadProfileImageSubscriber()));
    }

    @Override
    public void doRegisterFcmToken(RegisterFcmTokenRequest registerFcmTokenRequest) {
        disposable.add(doRegisterFcmTokenObservable(registerFcmTokenRequest).subscribeWith(doRegisterFcmTokenSubscriber()));
    }

    @Override
    public void doForgotPassword(ForgotPasswordRequest forgotPasswordRequest) {
        disposable.add(doForgotPasswordObservable(forgotPasswordRequest).subscribeWith(doForgotPasswordSubscriber()));
    }

    @Override
    public void doResetPassword(ResetPasswordRequest resetPasswordRequest) {
        disposable.add(doResetPasswordObservable(resetPasswordRequest).subscribeWith(doResetPasswordSubscriber()));
    }

    @Override
    public void doValidateResetCode(String resetToken) {
        disposable.add(doValidateResetCodeObservable(resetToken).subscribeWith(doValidateResetCodeSubscriber()));
    }

    @Override
    public void attachView(View v) {
        if (v instanceof UserView) {
            mUserView = (UserView) v;
            mView = mUserView;
        }
    }
    /*Observables*/

    public Single<ValidateResetCodeResponse> doValidateResetCodeObservable(String resetToken) {
        try {
            return getService().doValidateResetCodeService(resetToken)
                    .subscribeOn(scheduler.backgroundThread())
                    .observeOn(scheduler.mainThread());

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    public Single<BaseServerResponse> doResetPasswordObservable(ResetPasswordRequest resetPasswordRequest) {
        try {
            return getService().doResetPasswordService(resetPasswordRequest)
                    .subscribeOn(scheduler.backgroundThread())
                    .observeOn(scheduler.mainThread());

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    public Single<BaseServerResponse> doForgotPasswordObservable(ForgotPasswordRequest forgotPasswordRequest) {
        try {
            return getService().doForgotPasswordService(forgotPasswordRequest)
                    .subscribeOn(scheduler.backgroundThread())
                    .observeOn(scheduler.mainThread());

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }
    public Single<RegisterResponse> doRegisterObservable(RegistrationRequest registrationRequest) {
        try {
            return getService().doRegisterService(registrationRequest)
                    .subscribeOn(scheduler.backgroundThread())
                    .observeOn(scheduler.mainThread());

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    public Single<RegisterFcmResponse> doRegisterFcmTokenObservable(RegisterFcmTokenRequest registerFcmTokenRequest) {
        try {
            return getService().doRegisterFcmService(getAccessToken(), registerFcmTokenRequest)
                    .subscribeOn(scheduler.backgroundThread())
                    .observeOn(scheduler.mainThread());

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }
    public Single<ProfileResponse> getProfileDetailsObservable() {
        try {
            return getService().getProfileDetailsService(getAccessToken(), getUserId())
                    .subscribeOn(scheduler.backgroundThread())
                    .observeOn(scheduler.mainThread());

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    public Single<ProfileUpdateResponse> doUpdateProfileObservable(UpdateMyDetailRequest updateMyDetailRequest) {
        try {
            return getService().doUpdateProfileService(getAccessToken(), updateMyDetailRequest, getUserId())
                    .subscribeOn(scheduler.backgroundThread())
                    .observeOn(scheduler.mainThread());

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    public Single<LogOutResponse> doLogOutObservable() {
        try {
            return getService().doLogOutService(getAccessToken())
                    .subscribeOn(scheduler.backgroundThread())
                    .observeOn(scheduler.mainThread());

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    public Single<UploadProfileImageResponse> doUploadProfileImageObservable(MultipartBody.Part[] profileImage) {
        try {
            return getService(). doUploadProfileImageService(getAccessToken(), profileImage, getUserId())
                    .subscribeOn(scheduler.backgroundThread())
                    .observeOn(scheduler.mainThread());

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    /*subscribes*/

    public DisposableSingleObserver<ValidateResetCodeResponse> doValidateResetCodeSubscriber() {
        return new DefaultSubscriber<ValidateResetCodeResponse>(this.mView) {

            @Override
            public void onError(Throwable e) {
                try {
                    RetrofitException error = (RetrofitException) e;
                    if (error.getKind() == RetrofitException.Kind.EXPIRED){
                        ValidateResetCodeResponse exceptionResponse = new ValidateResetCodeResponse();
                        exceptionResponse.setSuccess(false);
                        exceptionResponse.setTokenExpired(true);
                        exceptionResponse.setMessage(ApplicationConstants.ERROR_MSG_TOKEN_EXPIRED);
                        mUserView.showValidateResetCodeResponse(exceptionResponse);
                    } else {
                        ValidateResetCodeResponse response = error.getErrorBodyAs(ValidateResetCodeResponse.class);
                        if (response == null) {
                            response = new ValidateResetCodeResponse();
                            response.setMessage(getExceptionMessage(e));
                            response.setAPIError(false);
                        } else {
                            response.setAPIError(true);
                        }
                        response.setSuccess(false);
                        mUserView.showValidateResetCodeResponse(response);
                    }
                } catch (IOException ex) {
                    ValidateResetCodeResponse exceptionResponse = new ValidateResetCodeResponse();
                    exceptionResponse.setSuccess(false);
                    exceptionResponse.setAPIError(false);
                    exceptionResponse.setMessage(ApplicationConstants.ERROR_MSG_REST_UNEXPECTED);
                    mUserView.showValidateResetCodeResponse(exceptionResponse);

                    ex.printStackTrace();
                }
            }

            @Override
            public void onSuccess(ValidateResetCodeResponse response) {
                if (response != null) {
                    response.setSuccess(true);
                    mUserView.showValidateResetCodeResponse(response);
                }
            }
        };
    }

    public DisposableSingleObserver<BaseServerResponse> doForgotPasswordSubscriber() {
        return new DefaultSubscriber<BaseServerResponse>(this.mView) {

            @Override
            public void onError(Throwable e) {
                try {
                    RetrofitException error = (RetrofitException) e;
                    if (error.getKind() == RetrofitException.Kind.EXPIRED){
                        BaseServerResponse exceptionResponse = new BaseServerResponse();
                        exceptionResponse.setSuccess(false);
                        exceptionResponse.setTokenExpired(true);
                        exceptionResponse.setMessage(ApplicationConstants.ERROR_MSG_TOKEN_EXPIRED);
                        mUserView.showForgotPasswordResponse(exceptionResponse);
                    } else {
                        BaseServerResponse response = error.getErrorBodyAs(BaseServerResponse.class);
                        if (response == null) {
                            response = new BaseServerResponse();
                            response.setMessage(getExceptionMessage(e));
                            response.setAPIError(false);
                        } else {
                            response.setAPIError(true);
                        }
                        response.setSuccess(false);
                        mUserView.showForgotPasswordResponse(response);
                    }
                } catch (IOException ex) {
                    BaseServerResponse exceptionResponse = new BaseServerResponse();
                    exceptionResponse.setSuccess(false);
                    exceptionResponse.setAPIError(false);
                    exceptionResponse.setMessage(ApplicationConstants.ERROR_MSG_REST_UNEXPECTED);
                    mUserView.showForgotPasswordResponse(exceptionResponse);

                    ex.printStackTrace();
                }
            }

            @Override
            public void onSuccess(BaseServerResponse response) {
                if (response != null) {
                    response.setSuccess(true);
                    mUserView.showForgotPasswordResponse(response);
                }
            }
        };
    }


    public DisposableSingleObserver<BaseServerResponse> doResetPasswordSubscriber() {
        return new DefaultSubscriber<BaseServerResponse>(this.mView) {

            @Override
            public void onError(Throwable e) {
                try {
                    RetrofitException error = (RetrofitException) e;
                    if (error.getKind() == RetrofitException.Kind.EXPIRED){
                        BaseServerResponse exceptionResponse = new BaseServerResponse();
                        exceptionResponse.setSuccess(false);
                        exceptionResponse.setTokenExpired(true);
                        exceptionResponse.setMessage(ApplicationConstants.ERROR_MSG_TOKEN_EXPIRED);
                        mUserView.showResetPasswordResponse(exceptionResponse);
                    } else {
                        BaseServerResponse response = error.getErrorBodyAs(BaseServerResponse.class);
                        if (response == null) {
                            response = new BaseServerResponse();
                            response.setMessage(getExceptionMessage(e));
                            response.setAPIError(false);
                        } else {
                            response.setAPIError(true);
                        }
                        response.setSuccess(false);
                        mUserView.showResetPasswordResponse(response);
                    }
                } catch (IOException ex) {
                    BaseServerResponse exceptionResponse = new BaseServerResponse();
                    exceptionResponse.setSuccess(false);
                    exceptionResponse.setAPIError(false);
                    exceptionResponse.setMessage(ApplicationConstants.ERROR_MSG_REST_UNEXPECTED);
                    mUserView.showResetPasswordResponse(exceptionResponse);

                    ex.printStackTrace();
                }
            }

            @Override
            public void onSuccess(BaseServerResponse response) {
                if (response != null) {
                    response.setSuccess(true);
                    mUserView.showResetPasswordResponse(response);
                }
            }
        };
    }

    public DisposableSingleObserver<RegisterResponse> doRegisterSubscriber() {
        return new DefaultSubscriber<RegisterResponse>(this.mView) {

            @Override
            public void onError(Throwable e) {
                try {
                    RetrofitException error = (RetrofitException) e;
                    RegisterResponse response = error.getErrorBodyAs(RegisterResponse.class);
                    if (response == null) {
                        response = new RegisterResponse();
                        response.setMessage(getExceptionMessage(e));
                        response.setAPIError(false);
                    } else {
                        response.setAPIError(true);
                    }
                    response.setSuccess(false);
                    mUserView.showRegisterResponse(response);
                } catch (IOException ex) {
                    RegisterResponse exceptionResponse = new RegisterResponse();
                    exceptionResponse.setSuccess(false);
                    exceptionResponse.setAPIError(false);
                    exceptionResponse.setMessage(ApplicationConstants.ERROR_MSG_REST_UNEXPECTED);
                    mUserView.showRegisterResponse(exceptionResponse);

                    ex.printStackTrace();
                }
            }

            @Override
            public void onSuccess(RegisterResponse response) {
                if (response != null) {
                    response.setSuccess(true);
                    mUserView.showRegisterResponse(response);
                }
            }
        };
    }


    public DisposableSingleObserver<ProfileUpdateResponse> doUpdateProfileSubscriber() {
        return new DefaultSubscriber<ProfileUpdateResponse>(this.mView) {

            @Override
            public void onError(Throwable e) {
                try {
                    RetrofitException error = (RetrofitException) e;
                    if (error.getKind() == RetrofitException.Kind.EXPIRED){
                        ProfileUpdateResponse exceptionResponse = new ProfileUpdateResponse();
                        exceptionResponse.setSuccess(false);
                        exceptionResponse.setTokenExpired(true);
                        exceptionResponse.setMessage(ApplicationConstants.ERROR_MSG_TOKEN_EXPIRED);
                        mUserView.showUpdateProfileResponse(exceptionResponse);
                    } else {
                        ProfileUpdateResponse response = error.getErrorBodyAs(ProfileUpdateResponse.class);
                        if (response == null) {
                            response = new ProfileUpdateResponse();
                            response.setMessage(getExceptionMessage(e));
                            response.setAPIError(false);
                        } else {
                            response.setAPIError(true);
                        }
                        response.setSuccess(false);
                        mUserView.showUpdateProfileResponse(response);
                    }
                } catch (IOException ex) {
                    ProfileUpdateResponse exceptionResponse = new ProfileUpdateResponse();
                    exceptionResponse.setSuccess(false);
                    exceptionResponse.setAPIError(false);
                    exceptionResponse.setMessage(ApplicationConstants.ERROR_MSG_REST_UNEXPECTED);
                    mUserView.showUpdateProfileResponse(exceptionResponse);

                    ex.printStackTrace();
                }
            }

            @Override
            public void onSuccess(ProfileUpdateResponse response) {
                if (response != null) {
                    response.setSuccess(true);
                    mUserView.showUpdateProfileResponse(response);
                }
            }
        };
    }

    public DisposableSingleObserver<LoginResponse> doLoginSubscriber() {
        return new DefaultSubscriber<LoginResponse>((View) this.mView) {

            @Override
            public void onError(Throwable e) {
                try {
                    RetrofitException error = (RetrofitException) e;
                    if (error.getKind() == RetrofitException.Kind.EXPIRED){
                        LoginResponse exceptionResponse = new LoginResponse();
                        exceptionResponse.setSuccess(false);
                        exceptionResponse.setTokenExpired(true);
                        exceptionResponse.setMessage(ApplicationConstants.ERROR_MSG_TOKEN_EXPIRED);
                        mUserView.showLoginResponse(exceptionResponse);
                    } else {
                        LoginResponse response = error.getErrorBodyAs(LoginResponse.class);
                        if (response == null) {
                            response = new LoginResponse();
                            response.setMessage(getExceptionMessage(e));
                            response.setAPIError(false);
                        } else {
                            response.setAPIError(true);
                        }
                        response.setSuccess(false);
                        mUserView.showLoginResponse(response);
                    }
                } catch (IOException ex) {
                    LoginResponse exceptionResponse = new LoginResponse();
                    exceptionResponse.setSuccess(false);
                    exceptionResponse.setAPIError(false);
                    exceptionResponse.setMessage(ApplicationConstants.ERROR_MSG_REST_UNEXPECTED);
                    mUserView.showLoginResponse(exceptionResponse);

                    ex.printStackTrace();
                }
            }

            @Override
            public void onSuccess(LoginResponse response) {
                if (response != null) {
                    response.setSuccess(true);
                    mUserView.showLoginResponse(response);
                }
            }
        };
    }

    public Single<LoginResponse> doLoginObservable(LoginRequest loginRequest) {
        try {

            return getService().doLoginService(loginRequest)
                    .subscribeOn(scheduler.backgroundThread())
                    .observeOn(scheduler.mainThread());


        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    public DisposableSingleObserver<ProfileResponse> getProfileDetailsSubscriber() {
        return new DefaultSubscriber<ProfileResponse>(this.mView) {

            @Override
            public void onError(Throwable e) {
                try {
                    RetrofitException error = (RetrofitException) e;
                    if (error.getKind() == RetrofitException.Kind.EXPIRED){
                        ProfileResponse exceptionResponse = new ProfileResponse();
                        exceptionResponse.setSuccess(false);
                        exceptionResponse.setTokenExpired(true);
                        exceptionResponse.setMessage(ApplicationConstants.ERROR_MSG_TOKEN_EXPIRED);
                        mUserView.showProfileResponse(exceptionResponse);
                    } else {
                        ProfileResponse response = error.getErrorBodyAs(ProfileResponse.class);
                        if (response == null) {
                            response = new ProfileResponse();
                            response.setMessage(getExceptionMessage(e));
                            response.setAPIError(false);
                        } else {
                            response.setAPIError(true);
                        }
                        response.setSuccess(false);
                        mUserView.showProfileResponse(response);
                    }
                } catch (IOException ex) {
                    ProfileResponse exceptionResponse = new ProfileResponse();
                    exceptionResponse.setSuccess(false);
                    exceptionResponse.setAPIError(false);
                    exceptionResponse.setMessage(ApplicationConstants.ERROR_MSG_REST_UNEXPECTED);
                    mUserView.showProfileResponse(exceptionResponse);

                    ex.printStackTrace();
                }
            }

            @Override
            public void onSuccess(ProfileResponse response) {
                if (response != null) {
                    response.setSuccess(true);
                    mUserView.showProfileResponse(response);
                }
            }
        };
    }

    public DisposableSingleObserver<LogOutResponse> doLogOutSubscriber() {
        return new DefaultSubscriber<LogOutResponse>(this.mView) {

            @Override
            public void onError(Throwable e) {
                try {
                    RetrofitException error = (RetrofitException) e;
                    if (error.getKind() == RetrofitException.Kind.EXPIRED){
                        LogOutResponse exceptionResponse = new LogOutResponse();
                        exceptionResponse.setSuccess(false);
                        exceptionResponse.setTokenExpired(true);
                        exceptionResponse.setMessage(ApplicationConstants.ERROR_MSG_TOKEN_EXPIRED);
                        mUserView.showLogOutResponse(exceptionResponse);
                    } else {
                        LogOutResponse response = error.getErrorBodyAs(LogOutResponse.class);
                        if (response == null) {
                            response = new LogOutResponse();
                            response.setMessage(getExceptionMessage(e));
                            response.setAPIError(false);
                        } else {
                            response.setAPIError(true);
                        }
                        response.setSuccess(false);
                        mUserView.showLogOutResponse(response);
                    }
                } catch (IOException ex) {
                    LogOutResponse exceptionResponse = new LogOutResponse();
                    exceptionResponse.setSuccess(false);
                    exceptionResponse.setAPIError(false);
                    exceptionResponse.setMessage(ApplicationConstants.ERROR_MSG_REST_UNEXPECTED);
                    mUserView.showLogOutResponse(exceptionResponse);

                    ex.printStackTrace();
                }
            }

            @Override
            public void onSuccess(LogOutResponse response) {
                if (response != null) {
                    response.setSuccess(true);
                    mUserView.showLogOutResponse(response);
                }
            }
        };
    }

    public DisposableSingleObserver<UploadProfileImageResponse> doUploadProfileImageSubscriber() {
        return new DefaultSubscriber<UploadProfileImageResponse>(this.mView) {

            @Override
            public void onError(Throwable e) {
                try {
                    RetrofitException error = (RetrofitException) e;
                    if (error.getKind() == RetrofitException.Kind.EXPIRED){
                        UploadProfileImageResponse exceptionResponse = new UploadProfileImageResponse();
                        exceptionResponse.setSuccess(false);
                        exceptionResponse.setTokenExpired(true);
                        exceptionResponse.setMessage(ApplicationConstants.ERROR_MSG_TOKEN_EXPIRED);
                        mUserView.showUploadProfileImageResponse((exceptionResponse));
                    } else {
                        UploadProfileImageResponse response = error.getErrorBodyAs(UploadProfileImageResponse.class);
                        if (response == null) {
                            response = new UploadProfileImageResponse();
                            response.setMessage(getExceptionMessage(e));
                            response.setAPIError(false);
                        } else {
                            response.setAPIError(true);
                        }
                        response.setSuccess(false);
                        mUserView.showUploadProfileImageResponse(response);
                    }
                } catch (IOException ex) {
                    UploadProfileImageResponse exceptionResponse = new UploadProfileImageResponse();
                    exceptionResponse.setSuccess(false);
                    exceptionResponse.setAPIError(false);
                    exceptionResponse.setMessage(ApplicationConstants.ERROR_MSG_REST_UNEXPECTED);
                    mUserView.showUploadProfileImageResponse(exceptionResponse);

                    ex.printStackTrace();
                }
            }

            @Override
            public void onSuccess(UploadProfileImageResponse response) {
                if (response != null) {
                    response.setSuccess(true);
                    mUserView.showUploadProfileImageResponse(response);
                }
            }
        };
    }

    public DisposableSingleObserver<RegisterFcmResponse> doRegisterFcmTokenSubscriber() {
        return new DefaultSubscriber<RegisterFcmResponse>(this.mView) {

            @Override
            public void onError(Throwable e) {
                try {
                    RetrofitException error = (RetrofitException) e;
                    if (error.getKind() == RetrofitException.Kind.EXPIRED){
                        RegisterFcmResponse exceptionResponse = new RegisterFcmResponse();
                        exceptionResponse.setSuccess(false);
                        exceptionResponse.setTokenExpired(true);
                        exceptionResponse.setMessage(ApplicationConstants.ERROR_MSG_TOKEN_EXPIRED);
                        mUserView.showRegisterFcmResponse(exceptionResponse);
                    } else {
                        RegisterFcmResponse response = error.getErrorBodyAs(RegisterFcmResponse.class);
                        if (response == null) {
                            response = new RegisterFcmResponse();
                            response.setMessage(getExceptionMessage(e));
                            response.setAPIError(false);
                        } else {
                            response.setAPIError(true);
                        }
                        response.setSuccess(false);
                        mUserView.showRegisterFcmResponse(response);
                    }
                } catch (IOException ex) {
                    RegisterFcmResponse exceptionResponse = new RegisterFcmResponse();
                    exceptionResponse.setSuccess(false);
                    exceptionResponse.setAPIError(false);
                    exceptionResponse.setMessage(ApplicationConstants.ERROR_MSG_REST_UNEXPECTED);
                    mUserView.showRegisterFcmResponse(exceptionResponse);

                    ex.printStackTrace();
                }
            }

            @Override
            public void onSuccess(RegisterFcmResponse response) {
                if (response != null) {
                    response.setSuccess(true);
                    mUserView.showRegisterFcmResponse(response);
                }
            }
        };
    }

    private UserService getService() {
        return (UserService) mService;
    }
}

