package com.example.mytravellerapp.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mytravellerapp.R;
import com.example.mytravellerapp.common.CommonUtils;
import com.example.mytravellerapp.common.constants.ApplicationConstants;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

public class RegisterEmailActivity extends BaseActivity {
    private static final String TAG = "RegisterEmailActivity";
    public String emailFwd;
    //UI
    private TextInputEditText email;
    private Button next;
    private TextView login;
    private ImageView bg_signup_top;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_email);
        //bind
        login = findViewById(R.id.login);
        email = findViewById(R.id.email);
        next = findViewById(R.id.btn_next);
        bg_signup_top = findViewById(R.id.bg_signup_top);
        //already have an account
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doNext();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterEmailActivity.this, LoginActivity.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
            }
        });
        Picasso.with(getApplicationContext()).load(R.drawable.bg_signup_top).into(bg_signup_top);
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

    private void doNext() {
        if (!CommonUtils.getInstance().isNetworkConnected()) {
            showAlertDialog(ApplicationConstants.WARNING, ApplicationConstants.ERROR_MSG_CONNECTION_LOST);
        } else if (email.getText().toString().isEmpty()) {
            showTopSnackBar(getString(R.string.email_required), getResources().getColor(R.color.sign_up_password_text_color));
        } else {
            performRequest(email.getText().toString());
        }
    }

    private void performRequest(String username) {
        if (CommonUtils.getInstance().isNetworkConnected()) {
            emailFwd = username;
            if (isValidEmail(emailFwd)) {
                Intent intent = new Intent(RegisterEmailActivity.this, RegisterPasswordActivity.class);
                intent.putExtra("emailFwd1", emailFwd);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            } else {
                showTopSnackBar(getString(R.string.email_not_valid), getResources().getColor(R.color.sign_up_password_text_color));
            }
        } else {
            showAlertDialog(ApplicationConstants.WARNING, ApplicationConstants.ERROR_MSG_CONNECTION_LOST);
        }
    }

    private boolean isValidEmail(String email) {
        return email != null && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


}
