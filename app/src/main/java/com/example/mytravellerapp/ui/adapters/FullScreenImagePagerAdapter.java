package com.example.mytravellerapp.ui.adapters;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.mytravellerapp.ui.fragments.FullScreenImageFragment;

import java.util.ArrayList;

public class FullScreenImagePagerAdapter extends FragmentStatePagerAdapter {
    private ArrayList<String> imageList;
    private Context context;
    private Fragment mFragment;

    public FullScreenImagePagerAdapter(FragmentManager fm, Context mContext, ArrayList<String> imageUrlList, Fragment fragment) {
        super(fm);
        this.imageList = imageUrlList;
        this.context = mContext;
        this.mFragment = fragment;
    }

    @Override
    public Fragment getItem(int position) {
        return new FullScreenImageFragment().newInstance(imageList.get(position), mFragment);
    }

    @Override
    public int getCount() {
        return imageList.size();
    }
}
