package com.example.mytravellerapp.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.mytravellerapp.R;
import com.example.mytravellerapp.common.CommonUtils;
import com.example.mytravellerapp.common.constants.ApplicationConstants;
import com.example.mytravellerapp.domain.UserService;
import com.example.mytravellerapp.domain.UserServiceImpl;
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

public class ForgotPwdConfirmationActivity extends BaseActivity implements UserView {
    private static final String TAG = "ForgotPwdConfirmationAc";
    String resetToken = null;
    private ProgressDialog progressDialog;
    protected Presenter presenter;
    //ui
    private TextInputEditText one;
    private TextInputEditText two;
    private TextInputEditText three;
    private TextInputEditText four;
    private Button btnNext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pwd_confirmation);
        //bind ui
        one = findViewById(R.id.one);
        two = findViewById(R.id.two);
        three = findViewById(R.id.three);
        four = findViewById(R.id.four);
        btnNext = findViewById(R.id.btn_next);

        initializePresenter();
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetToken = one.getText().toString()+two.getText().toString()+three.getText().toString()+four.getText().toString();
                if(!resetToken.equals("") && resetToken.length()==4){
                    performRequest(resetToken);
                }else {
                    showTopSnackBar(getString(R.string.password_reset_validation_code_required), getResources().getColor(R.color.sign_up_password_text_color));
                }
            }
        });
    }

    public void initializePresenter() {
        UserService mUserService = new UserServiceImpl(new BMSService());
        presenter = new UserPresenterImpl(ForgotPwdConfirmationActivity.this, mUserService, new AppScheduler());
        presenter.attachView(ForgotPwdConfirmationActivity.this);
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

    private void performRequest(String resetToken) {
        if (CommonUtils.getInstance().isNetworkConnected()) {
            setProgressDialog(true);

            ((UserPresenter) presenter).doValidateResetCode(resetToken);
        } else {
            showAlertDialog(ApplicationConstants.WARNING, ApplicationConstants.ERROR_MSG_CONNECTION_LOST);
        }
    }

    public void setProgressDialog(boolean isLoading) {
        if (isLoading) {
            if (progressDialog != null) progressDialog.show();
            else progressDialog = ProgressDialog.show(ForgotPwdConfirmationActivity.this,
                    ApplicationConstants.EMPTY_STRING, "loading", true);
        } else {
            if (progressDialog != null && progressDialog.isShowing()) progressDialog.dismiss();
        }
    }


    @Override
    public void showValidateResetCodeResponse(ValidateResetCodeResponse validateResetCodeResponse) {
        setProgressDialog(false);
        if (validateResetCodeResponse.isSuccess()) {
            Intent intent = new Intent(ForgotPwdConfirmationActivity.this, ForgotPwdAddNewActivity.class);
            intent.putExtra("EXTRA_RESET_TOKEN", validateResetCodeResponse.getResetToken());
            startActivity(intent);
            finish();
        } else {
            if (validateResetCodeResponse.isAPIError()) {
                showTopSnackBar(validateResetCodeResponse.getMessage(), getResources().getColor(R.color.sign_up_password_text_color));
            } else {
                showTopSnackBar(validateResetCodeResponse.getMessage(), getResources().getColor(R.color.sign_up_password_text_color));
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
    public void showResetPasswordResponse(BaseServerResponse baseServerResponse) {

    }

    @Override
    public void showMessage(String message) {

    }
}
