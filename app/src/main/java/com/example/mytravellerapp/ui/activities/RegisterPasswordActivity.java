package com.example.mytravellerapp.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mytravellerapp.R;
import com.example.mytravellerapp.common.CommonUtils;
import com.example.mytravellerapp.common.constants.ApplicationConstants;
import com.example.mytravellerapp.domain.UserService;
import com.example.mytravellerapp.domain.UserServiceImpl;
import com.example.mytravellerapp.model.entities.request.RegistrationRequest;
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

public class RegisterPasswordActivity extends BaseActivity implements UserView {

    private static final String TAG = "RegisterPasswordActivit";
    protected Presenter presenter;
    private ProgressDialog progressDialog;
    String emailFwd;
    private SharedPreferences preferences;

    //UI
    private TextInputEditText password;
    private Button btnCreateProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_password);
        //bind
        password = findViewById(R.id.password);
        btnCreateProfile = findViewById(R.id.create_profile);

        initializePresenter();

        preferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
        emailFwd = getIntent().getExtras().getString("emailFwd1");

        password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) btnCreateProfile.performClick();
                return false;
            }
        });

        btnCreateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doCreateProfile();
            }
        });

    }

    public void initializePresenter() {
        UserService mUserService = new UserServiceImpl(new BMSService());
        presenter = new UserPresenterImpl(RegisterPasswordActivity.this, mUserService, new AppScheduler());
        presenter.attachView(RegisterPasswordActivity.this);
        presenter.onCreate();
    }

    public void setProgressDialog(boolean isLoading) {
        if (isLoading) {
            if (progressDialog != null) progressDialog.show();
            else progressDialog = ProgressDialog.show(RegisterPasswordActivity.this,
                    ApplicationConstants.EMPTY_STRING, "loading", true);
        } else {
            if (progressDialog != null && progressDialog.isShowing()) progressDialog.dismiss();
        }
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

    private void doCreateProfile() {
        if (!CommonUtils.getInstance().isNetworkConnected()) {
            showAlertDialog(ApplicationConstants.WARNING, ApplicationConstants.ERROR_MSG_CONNECTION_LOST);
        } else if (password.getText().toString().isEmpty()) {
            showTopSnackBar(getString(R.string.password_required), getResources().getColor(R.color.sign_up_password_text_color));
        } else if (!isValidPassword(password.getText().toString())) {
            showTopSnackBar(getString(R.string.password_invalid), getResources().getColor(R.color.sign_up_password_text_color));
        } else {
            performRequest(emailFwd, password.getText().toString());
        }
    }

    private void performRequest(String username, String password) {
        if (CommonUtils.getInstance().isNetworkConnected()) {
            setProgressDialog(true);
            RegistrationRequest registrationRequest = new RegistrationRequest();
            registrationRequest.setEmail(username);
            registrationRequest.setPassword(password);
            ((UserPresenter) presenter).doRegister(registrationRequest);
        } else {
            showAlertDialog(ApplicationConstants.WARNING, ApplicationConstants.ERROR_MSG_CONNECTION_LOST);
        }
    }


    private boolean isValidPassword(String password) {
        return password != null && password.length()>=6;
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
        setProgressDialog(false);
        if (registerResponse.isSuccess()) {

            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle(Html.fromHtml("<font color='#2c8e19'> Success ! </font>"));
            alertDialog.setMessage("your account has been created successfully !");
            alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    startActivity(new Intent(RegisterPasswordActivity.this,  LoginActivity.class));
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    finish();
                }
            });
//            showTopSnackBar(registerResponse.getMessage(), getResources().getColor(R.color.green));
            alertDialog.show();

        } else if (registerResponse.isAPIError() && registerResponse.getMessage() != null) {
            if ((registerResponse.getMessage()).equals("Mail exists")){
                AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                alertDialog.setTitle("Error");
                alertDialog.setMessage(registerResponse.getMessage());
                alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(RegisterPasswordActivity.this, RegisterEmailActivity.class));
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        finish();
                    }
                });

                alertDialog.show();
            }
            else {
                AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                alertDialog.setTitle("Error");
                alertDialog.setMessage(registerResponse.getMessage());
                alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(RegisterPasswordActivity.this, LoginActivity.class));
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        finish();
                    }
                });
            }
        } else {
            showTopSnackBar(registerResponse.getMessage(), getResources().getColor(R.color.sign_up_password_text_color));
        }
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
