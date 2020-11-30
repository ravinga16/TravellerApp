package com.example.mytravellerapp.ui.utils;


import androidx.fragment.app.FragmentActivity;

public class BaseBackPressedListener {

    private final FragmentActivity activity;

    public BaseBackPressedListener(FragmentActivity activity) {
        this.activity = activity;
    }

    /** fragment on back pressed interface */
    public interface OnBackPressedListener {
        void doBack();
    }
}
