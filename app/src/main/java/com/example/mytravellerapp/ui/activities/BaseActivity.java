package com.example.mytravellerapp.ui.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.mytravellerapp.R;
import com.example.mytravellerapp.common.constants.ApplicationConstants;
import com.google.android.material.snackbar.Snackbar;

public class BaseActivity extends AppCompatActivity {

    private static final String TAG = "BaseActivity";
    public static BaseActivity baseActivity = null;
    protected Toolbar mToolBar;
    public AlertDialog myAlertDialog;
    public static boolean isAppWentToBg = true;
    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        getActionBarToolbar();
//        initializePresenterForzLogOut();
        baseActivity = this;
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: is called");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: is called");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: is called");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: is called");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart: is called");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    protected void showAlertDialog(String title, String message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(BaseActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setNeutralButton("Ok", defaultDialogClickListener());

        if (!BaseActivity.this.isFinishing()) alertDialog.show();
    }

    protected DialogInterface.OnClickListener defaultDialogClickListener() {
        return new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        };
    }

    protected void showAlertDialog(boolean setCancelable, String title, String message, String positiveBtnTxt, String negativeBtnTxt,
                                   DialogInterface.OnClickListener positiveListener, DialogInterface.OnClickListener negativeListener) {
        if(myAlertDialog != null && myAlertDialog.isShowing()) return;

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(BaseActivity.this);
        alertDialog.setCancelable(setCancelable);

        String titleTxt = (title != null) ? title : "Warning";
        String messageTxt = (message != null) ? message : "";
        alertDialog.setTitle(titleTxt);
        alertDialog.setMessage(messageTxt);

        if(positiveBtnTxt != null) {
            DialogInterface.OnClickListener positiveClickListener = (positiveListener != null) ? positiveListener : defaultDialogClickListener();
            alertDialog.setPositiveButton(positiveBtnTxt, positiveClickListener);
        }

        if(negativeBtnTxt != null) {
            DialogInterface.OnClickListener negativeClickListener = (negativeListener != null) ? negativeListener : defaultDialogClickListener();
            alertDialog.setNeutralButton(negativeBtnTxt, negativeClickListener);
        }

        myAlertDialog = alertDialog.create();
        if (!BaseActivity.this.isFinishing()) myAlertDialog.show();
    }

    protected void showTopSnackBar(String message, int bColor) {
/*        TSnackbar snackbar = TSnackbar.make(findViewById(android.R.id.content), message, TSnackbar.LENGTH_LONG);
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(bColor);
        TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        textView.setGravity(Gravity.CENTER);
        snackbar.show();*/

        Snackbar snack = Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG);
        View snackbarView = snack.getView();
        snackbarView.setBackgroundColor(bColor);
//        TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
//        textView.setTextColor(Color.WHITE);
//        textView.setGravity(Gravity.CENTER_HORIZONTAL);
//        FrameLayout.LayoutParams params =(FrameLayout.LayoutParams)snackbarView.getLayoutParams();
//        params.gravity = Gravity.TOP;
//        snackbarView.setLayoutParams(params);
        snack.show();
    }
    public void closeKeyboard(){
        View view = this.getCurrentFocus();
        if(view != null){
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    protected Toolbar getActionBarToolbar() {
        if (mToolBar == null) {
            mToolBar = (Toolbar) findViewById(R.id.toolbar);
            if (mToolBar != null) {
                setSupportActionBar(mToolBar);
                ActionBar mActionBar = BaseActivity.this.getSupportActionBar();
                mActionBar.setDisplayShowHomeEnabled(false);
                mActionBar.setDisplayShowTitleEnabled(false);
                mActionBar.setDisplayShowCustomEnabled(true);

                // remove previously created actionbar
                mActionBar.invalidateOptionsMenu();

                /** remove actionbar unnecessary left margin */
                mToolBar.setContentInsetsAbsolute(0, 0);
            }
        }
        return mToolBar;
    }



}
