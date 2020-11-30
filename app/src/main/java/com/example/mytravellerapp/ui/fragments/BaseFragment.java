package com.example.mytravellerapp.ui.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.mytravellerapp.R;
import com.example.mytravellerapp.mvp.presenters.Presenter;
import com.example.mytravellerapp.ui.activities.MainActivity;
import com.example.mytravellerapp.utils.AppScheduler;
import com.example.mytravellerapp.utils.IScheduler;
import com.google.android.material.snackbar.Snackbar;
import com.kaopiz.kprogresshud.KProgressHUD;

public abstract class BaseFragment extends Fragment {

    private static final String TAG = "BaseFragment";
    protected IScheduler scheduler;
    protected Toolbar mToolBar;
    protected Presenter presenter;
    public static AlertDialog myAlertDialog;
    private KProgressHUD pd = null;
    public static AlertDialog myAlertDialogTwo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scheduler = new AppScheduler();
        initializePresenter();
        if (presenter != null) presenter.onCreate();
        mToolBar = null;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        getActionBarToolbar(view);
        setUpUI();
        setUpToolBar();
    }

    protected abstract void setUpToolBar();

    protected abstract void setUpUI();

    protected abstract void initializePresenter();

    protected Toolbar getActionBarToolbar(View v) {
        mToolBar = (Toolbar) v.findViewById(R.id.toolbar);
        if (mToolBar != null) {
            ((MainActivity) getActivity()).setSupportActionBar(mToolBar);
            mToolBar.setContentInsetsAbsolute(0, 0); /** remove actionbar unnecessary left margin */
        }
        return mToolBar;
    }

    protected void showAlertDialog(boolean setCancelable, String title, String message, String positiveBtnTxt, String negativeBtnTxt,
                                   DialogInterface.OnClickListener positiveListener, DialogInterface.OnClickListener negativeListener) {
        if (myAlertDialog != null && myAlertDialog.isShowing()) return;

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setCancelable(setCancelable);

        String titleTxt = (title != null) ? title : "Warning";
        String messageTxt = (message != null) ? message : "";
        alertDialog.setTitle(titleTxt);
        alertDialog.setMessage(messageTxt);

        if (positiveBtnTxt != null) {
            DialogInterface.OnClickListener positiveClickListener = (positiveListener != null) ? positiveListener : defaultDialogClickListener();
            alertDialog.setPositiveButton(positiveBtnTxt, positiveClickListener);
        }

        if (negativeBtnTxt != null) {
            DialogInterface.OnClickListener negativeClickListener = (negativeListener != null) ? negativeListener : defaultDialogClickListener();
            alertDialog.setNegativeButton(negativeBtnTxt, negativeClickListener);
        }

        myAlertDialog = alertDialog.create();
//        myAlertDialogOne = myAlertDialog;
        if (!getActivity().isFinishing()) alertDialog.show();
    }

    protected void showAlertDialog(boolean setCancelable, String title, String message, DialogInterface.OnClickListener positiveListener) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity())
                .setCancelable(setCancelable)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Ok", positiveListener);
        if (!getActivity().isFinishing()) myAlertDialogTwo = alertDialog.show();
    }

    protected DialogInterface.OnClickListener defaultDialogClickListener() {
        return new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        };
    }

    protected void showTopSnackBar(String message, int bColor) {
        Snackbar snack = Snackbar.make(getActivity().getWindow().getDecorView().findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG);
        View snackbarView = snack.getView();
        snackbarView.setBackgroundColor(bColor);
//        TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
//        textView.setTextColor(Color.WHITE);
//        textView.setGravity(Gravity.CENTER_HORIZONTAL);
        snack.show();
    }

    public void setProgressDialog(boolean isLoading) {
        try {
            if (isLoading) {
                if (pd != null) {
                    pd.show();
                } else {
                    pd = KProgressHUD.create(getActivity())
                            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                            .setCancellable(true)
                            .setLabel("Please wait")
                            .setAnimationSpeed(1)
                            .setDimAmount(0.3f)
                            .show();
                }

//                if (progressDialog != null) progressDialog.show();
//                else progressDialog = ProgressDialog.show(getActivity(), ApplicationConstants.EMPTY_STRING, getString(R.string.please_wait), true);
            } else {
                if (pd != null && pd.isShowing()) pd.dismiss();
//                if (progressDialog != null && progressDialog.isShowing()) progressDialog.dismiss();
            }
        } catch (Exception e) {
            Log.e("BaseFragment", "setProgressDialog: " + e.toString());
        }
    }


    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (presenter != null) presenter.onStop();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (presenter != null) {
            presenter.onStart();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}
