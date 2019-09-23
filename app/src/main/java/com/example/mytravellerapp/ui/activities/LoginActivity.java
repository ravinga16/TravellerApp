package com.example.mytravellerapp.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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
import com.example.mytravellerapp.model.entities.response.LoginResponse;
import com.example.mytravellerapp.model.rest.BMSService;
import com.example.mytravellerapp.mvp.presenters.Presenter;
import com.example.mytravellerapp.mvp.presenters.UserPresenter;
import com.example.mytravellerapp.mvp.presenters.UserPresenterImpl;
import com.example.mytravellerapp.mvp.views.UserView;
import com.example.mytravellerapp.utils.AppScheduler;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements UserView {

    final String TAG = LoginActivity.this.getClass().getSimpleName();

    private BMSService bmsService = new BMSService();
    private TextView textview_repsonse;

    private String send_email;
    private String send_password;
    private boolean success;
    private Button btn_login;
    private EditText email;
    private EditText password;

//    @BindView(R.id.btn_login)Button btn_login;
//    @BindView(R.id.email)EditText email;
//    @BindView(R.id.password)EditText password;

    private ProgressDialog progressDialog;
    protected Presenter presenter;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_login);

            btn_login = (Button)findViewById(R.id.btn_login);
            email = (EditText)findViewById(R.id.email);
            password = (EditText)findViewById(R.id.password);

            btn_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    doLogin();
                }
            });
//            ButterKnife.bind(this);

            initializePresenter();
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

    private void doLogin(){
        Log.i("LoginActivity","doLogin()*********************");
        send_email = email.getText().toString();
        send_password = password.getText().toString();
        performRequest(send_email, send_password);
    }

    private void performRequest(String username, String password) {
        if (CommonUtils.getInstance().isNetworkConnected()) {
            setProgressDialog(true);
            LoginRequest loginRequest = new LoginRequest();
            loginRequest.setEmail(username);
            loginRequest.setPassword(password);
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


//    private void callLoginAPI(String email, String password){
//        Log.i("LoginActivity","login()*********************");
//        textview_repsonse = findViewById(R.id.response);
////        LoginRequest loginRequest  = new LoginRequest(email, password); //hard code value
//        LoginRequest loginRequest = new LoginRequest("chamith@gmail.com", "123456");
//        UserServiceImpl userService = new UserServiceImpl();
//        Call<LoginResponse> call = bmsService.getApi().doLoginAPI(loginRequest);
//        call.enqueue(new Callback<LoginResponse>() {
//            @Override
//            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
//                if(!response.isSuccessful()){
//                    textview_repsonse.setText("Code:"+ response.code());
//                    success = false;
//                }else{
//                    success = true;
//                    LoginResponse loginResponse = response.body();
//                    String content = "";
//                    content += "Message:"+loginResponse.getMessage()+"\n";
//                    content += "Email:"+loginResponse.getEmail()+"\n";
//                    content += "Token:"+loginResponse.getToken()+"\n";
//                    content += "UserId:"+loginResponse.getUserId()+"\n"+"\n";
//
//                    textview_repsonse.setText(content);
//                    System.out.println(success);
//                    showSuccessResponse();
//                }
//            }
//
//
//            @Override
//            public void onFailure(Call<LoginResponse> call, Throwable t) {
//                textview_repsonse.setText(t.getMessage());
//            }
//        });
//
//    }

    private void showSuccessResponse(){
//        Intent intent = new Intent(this, DisplayMessageActivity.class);
//        startActivity(intent);
    }


    @Override
    public void showLoginResponse(LoginResponse loginResponse) {
        setProgressDialog(false);
        if (loginResponse.isSuccess()) {
            System.out.println("=======>>>>>>>> Success " + loginResponse.getToken());
//            saveObjectToSharedPreferences(IPreferencesKeys.ACCESS_TOKEN, loginResponse.getToken());
//            saveObjectToSharedPreferences(IPreferencesKeys.USER_ID, loginResponse.getUserId());
//            startActivity(new Intent(SignUpActivity.this, MainActivity.class));
//            finish();
        } else {

            System.out.println("=======>>>>>>>> ERROR " + loginResponse.getMessage());
//            if (loginResponse.isAPIError()) {
//                showTopSnackBar(loginResponse.getMessage(), getResources().getColor(R.color.sign_up_password_text_color));
//            } else {
//                showTopSnackBar(loginResponse.getMessage(), getResources().getColor(R.color.sign_up_password_text_color));
//            }
        }
    }

    @Override
    public void showMessage(String message) {

    }
}
