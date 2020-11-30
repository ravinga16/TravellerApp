package com.example.mytravellerapp.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.style.UnderlineSpan;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.mytravellerapp.R;
import com.example.mytravellerapp.common.CommonUtils;
import com.example.mytravellerapp.common.constants.ApplicationConstants;
import com.example.mytravellerapp.domain.UserService;
import com.example.mytravellerapp.domain.UserServiceImpl;
import com.example.mytravellerapp.model.entities.request.ResetPasswordRequest;
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
import com.google.android.material.textfield.TextInputEditText;

public class ForgotPwdAddNewActivity extends BaseActivity implements UserView {
    protected Presenter presenter;
    private ProgressDialog progressDialog;
    private String resetToken;
    //ui
    private TextInputEditText newPassword;
    private TextInputEditText confirmPassword;
    private Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pwd_add_new);

        initializePresenter();
        newPassword = findViewById(R.id.new_password);
        confirmPassword = findViewById(R.id.confirm_password);
        nextButton = findViewById(R.id.btn_next);

        if (getIntent() != null) {
            resetToken = getIntent().getStringExtra("EXTRA_RESET_TOKEN");
        }

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onNextClick();
            }
        });
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

    public void initializePresenter() {
        UserService mUserService = new UserServiceImpl(new BMSService());
        presenter = new UserPresenterImpl(ForgotPwdAddNewActivity.this, mUserService, new AppScheduler());
        presenter.attachView(ForgotPwdAddNewActivity.this);
        presenter.onCreate();
    }

    /**
     * Used to hide soft keyboard when touch out side of the edit text
     */
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

    public void onNextClick() {
        if (newPassword.getText().toString().trim().isEmpty()) {
            showTopSnackBar(getString(R.string.new_password_required), getResources().getColor(R.color.sign_up_password_text_color));
        } else if (!isValidPassword(newPassword.getText().toString())) {
            showTopSnackBar(getString(R.string.password_invalid), getResources().getColor(R.color.sign_up_password_text_color));
        } else if (confirmPassword.getText().toString().trim().isEmpty()) {
            showTopSnackBar(getString(R.string.confirm_required), getResources().getColor(R.color.sign_up_password_text_color));
        } else if (!isValidConfirmPassword(confirmPassword.getText().toString().trim())) {
            showTopSnackBar(getString(R.string.confirm_password_invalid), getResources().getColor(R.color.sign_up_password_text_color));
        } else {
            performRequest(resetToken, newPassword.getText().toString().trim(), confirmPassword.getText().toString().trim());
        }
    }

    private void performRequest(String resetToken, String newPassword, String passwordConfirm) {
        if (CommonUtils.getInstance().isNetworkConnected()) {
            setProgressDialog(true);
            ResetPasswordRequest request = new ResetPasswordRequest();
            request.setResetToken(resetToken);
            request.setPassword(newPassword);
            request.setConfirmPassword(passwordConfirm);
            ((UserPresenter) presenter).doResetPassword(request);
        } else {
            showAlertDialog(ApplicationConstants.WARNING, ApplicationConstants.ERROR_MSG_CONNECTION_LOST);
        }
    }

    private boolean isValidPassword(String password) {
        return password != null && password.length() >= 6;
    }

    private boolean isValidConfirmPassword(String password) {
        return password != null && (newPassword.getText().toString()).equals(password);
    }

    public void setProgressDialog(boolean isLoading) {
        if (isLoading) {
            if (progressDialog != null) progressDialog.show();
            else progressDialog = ProgressDialog.show(ForgotPwdAddNewActivity.this,
                    ApplicationConstants.EMPTY_STRING, "loading", true);
        } else {
            if (progressDialog != null && progressDialog.isShowing()) progressDialog.dismiss();
        }
    }

    @Override
    public void showResetPasswordResponse(BaseServerResponse baseServerResponse) {
        setProgressDialog(false);
        if (baseServerResponse.isSuccess()) {
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle(Html.fromHtml("<font color='#2c8e19'> Success ! </font>"));
            alertDialog.setMessage(baseServerResponse.getMessage());
            alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(ForgotPwdAddNewActivity.this, LoginActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    finish();
                }
            });
            alertDialog.show();

        } else {
            if (baseServerResponse.isAPIError()) {
                showTopSnackBar(baseServerResponse.getMessage(), getResources().getColor(R.color.sign_up_password_text_color));
            } else {
                showTopSnackBar(baseServerResponse.getMessage(), getResources().getColor(R.color.sign_up_password_text_color));
            }
        }
    }

    @Override
    public void showLoginResponse(LoginResponse loginResponse) {

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
    public void showValidateResetCodeResponse(ValidateResetCodeResponse validateResetCodeResponse) {

    }

    @Override
    public void showMessage(String message) {

    }
}
