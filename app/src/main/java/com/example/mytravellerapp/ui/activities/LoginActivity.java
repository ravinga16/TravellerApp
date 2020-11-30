package com.example.mytravellerapp.ui.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mytravellerapp.R;
import com.example.mytravellerapp.common.CommonUtils;
import com.example.mytravellerapp.common.constants.ApplicationConstants;
import com.example.mytravellerapp.common.constants.IPreferencesKeys;
import com.example.mytravellerapp.domain.UserService;
import com.example.mytravellerapp.domain.UserServiceImpl;
import com.example.mytravellerapp.model.entities.request.LoginRequest;
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
import com.example.mytravellerapp.mvp.presenters.Presenter;
import com.example.mytravellerapp.mvp.presenters.UserPresenter;
import com.example.mytravellerapp.mvp.presenters.UserPresenterImpl;
import com.example.mytravellerapp.mvp.views.UserView;
import com.example.mytravellerapp.utils.AppScheduler;

import lombok.core.Main;

public class LoginActivity extends BaseActivity implements UserView {

    final String TAG = LoginActivity.this.getClass().getSimpleName();

    private BMSService bmsService = new BMSService();
    private TextView textview_repsonse;

    private String send_email;
    private String send_password;
    private boolean success;
    //ui
    private Button btn_login;
    private EditText email;
    private EditText password;
    private TextView btnSignUp;
    private TextView forgotPwd;
    private ProgressDialog progressDialog;
    protected Presenter presenter;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_login);

            btn_login = (Button) findViewById(R.id.btn_login);
            email = (EditText) findViewById(R.id.email);
            password = (EditText) findViewById(R.id.password);
            btnSignUp = (TextView) findViewById(R.id.tv_lbl_signup);
            forgotPwd = (TextView) findViewById(R.id.tv_forgotten_password);

            btn_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    closeKeyboard();
                    doLogin();
                }
            });
//            ButterKnife.bind(this);

            initializePresenter();
            //sign up onClick listener
            btnSignUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(LoginActivity.this, RegisterEmailActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    finish();
                }
            });

            forgotPwd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(LoginActivity.this, ForgotPwdEmailActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    finish();
                }
            });
        } catch (Exception ex) {
            Log.e(TAG, "onCreate: " + ex.toString());
        }

    }

    public void initializePresenter() {
        UserService mUserService = new UserServiceImpl(new BMSService());
        presenter = new UserPresenterImpl(LoginActivity.this, mUserService, new AppScheduler());
        presenter.attachView(LoginActivity.this);
        presenter.onCreate();
    }

    private boolean isValidEmail(String email) {
        return email != null && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (presenter != null) presenter.onStop();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (presenter != null) presenter.onStart();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (presenter != null) presenter.onDestroy();
    }


    private void doLogin() {
        if (!CommonUtils.getInstance().isNetworkConnected()) {
            showAlertDialog(ApplicationConstants.WARNING, ApplicationConstants.ERROR_MSG_CONNECTION_LOST);
        } else if (email.getText().toString().isEmpty()) {
            showTopSnackBar(getString(R.string.email_required), getResources().getColor(R.color.sign_up_password_text_color));
        } else if (!isValidEmail(email.getText().toString())) {
            showTopSnackBar(getString(R.string.email_invalid), getResources().getColor(R.color.sign_up_password_text_color));
        } else if (password.getText().toString().isEmpty()) {
            showTopSnackBar(getString(R.string.password_required), getResources().getColor(R.color.sign_up_password_text_color));
        } else {
            performRequest(email.getText().toString(), password.getText().toString());
        }

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        View view = getCurrentFocus();
        boolean ret = super.dispatchTouchEvent(event);

        if (view instanceof EditText) {
            View w = getCurrentFocus();
            int scrcoords[] = new int[2];
            w.getLocationOnScreen(scrcoords);
            float x = event.getRawX() + w.getLeft() - scrcoords[0];
            float y = event.getRawY() + w.getTop() - scrcoords[1];

            if (event.getAction() == MotionEvent.ACTION_UP &&
                    (x < w.getLeft() || x >= w.getRight() || y < w.getTop() || y > w.getBottom())) {
                CommonUtils.getInstance().hideKeyboard(this);
            }
        }
        return ret;
    }

    private void performRequest(String username, String password) {
        if (CommonUtils.getInstance().isNetworkConnected()) {
            setProgressDialog(true);
            LoginRequest loginRequest = new LoginRequest();
            loginRequest.setEmail(username);
            loginRequest.setPassword(password);
//            loginRequest.setEmail("chamith@gmail.com");
//            loginRequest.setPassword("123456");
            ((UserPresenter) presenter).doLogin(loginRequest);
        } else {
//            showAlertDialog(ApplicationConstants.WARNING, ApplicationConstants.ERROR_MSG_CONNECTION_LOST);
        }
    }

    public void setProgressDialog(boolean isLoading) {
        if (isLoading) {
            if (progressDialog != null) progressDialog.show();
            else progressDialog = ProgressDialog.show(LoginActivity.this,
                    "", "loading", true);
        } else {
            if (progressDialog != null && progressDialog.isShowing()) progressDialog.dismiss();
        }
    }


    public void saveObjectToSharedPreferences(String preferencesKeys, String loginResponse) {
        SharedPreferences prefs = this.getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        prefs.edit().putString(preferencesKeys, loginResponse).apply();
    }

    @Override
    public void showLoginResponse(LoginResponse loginResponse) {
        setProgressDialog(false);
        if (loginResponse.isSuccess()) {
            saveObjectToSharedPreferences(IPreferencesKeys.ACCESS_TOKEN, loginResponse.getToken());
            saveObjectToSharedPreferences(IPreferencesKeys.USER_ID, loginResponse.getUserId());
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        } else {

            if (loginResponse.isAPIError()) {
                showTopSnackBar(loginResponse.getMessage(), getResources().getColor(R.color.sign_up_password_text_color));
            } else {
                showTopSnackBar(loginResponse.getMessage(), getResources().getColor(R.color.sign_up_password_text_color));
            }
        }
    }

    @Override
    public void showProfileResponse(ProfileResponse profileResponse) {

    }

    @Override
    public void showUpdateProfileResponse(ProfileUpdateResponse profileUpdateResponse) {

    }

    @Override
    public void showLogOutResponse(LogOutResponse logOutResponse) {

    }

    @Override
    public void showUploadProfileImageResponse(UploadProfileImageResponse uploadProfileImageResponse) {

    }

    @Override
    public void showRegisterFcmResponse(RegisterFcmResponse registerFcmResponse) {

    }

    @Override
    public void showRegisterResponse(RegisterResponse registerResponse) {

    }

    @Override
    public void showForgotPasswordResponse(BaseServerResponse baseServerResponse) {

    }

    @Override
    public void showResetPasswordResponse(BaseServerResponse baseServerResponse) {

    }

    @Override
    public void showValidateResetCodeResponse(ValidateResetCodeResponse validateResetCodeResponse) {

    }


    @Override
    public void showMessage(String message) {

    }


}
