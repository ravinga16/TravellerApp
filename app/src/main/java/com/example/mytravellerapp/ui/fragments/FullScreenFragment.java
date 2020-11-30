package com.example.mytravellerapp.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.mytravellerapp.R;
import com.example.mytravellerapp.Style.ZoomOutPageTransformer;
import com.example.mytravellerapp.ui.adapters.FullScreenImagePagerAdapter;

import java.util.ArrayList;


public class FullScreenFragment extends Fragment {
    private static final String TAG = "FullScreenFragment";

    public static String getTAG() {
        return TAG;
    }

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPager;
    private PagerAdapter pagerAdapter;
    private ArrayList<String> imageUrlList;

    private int currentPosition ;
    public static FullScreenFragment fullScreenFragment = null;
    private FullScreenImagePagerAdapter fullScreenImagePagerAdapter;
    private ArrayList<String> urlArrayList = new ArrayList<>();
//    public static FullScreenFragment getInstance(ArrayList<String> imageList, int currentPostion){
//        FullScreenFragment fullScreenFragment = new FullScreenFragment();
//        fullScreenFragment.imageUrlList = imageList;
//        fullScreenFragment.currentPosition = currentPostion;
//        return fullScreenFragment;
//    }

    public static FullScreenFragment newInstance(ArrayList<String> imageList, int currentPosition) {
        Bundle args = new Bundle();
        args.putStringArrayList("imageList", imageList);
        args.putInt("currentPosition", currentPosition);
        FullScreenFragment fragment = new FullScreenFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_full_screen, container, false);
        fullScreenFragment = this;
        imageUrlList = getArguments().getStringArrayList("imageList");
        currentPosition = getArguments().getInt("currentPosition");

        mPager = rootView.findViewById(R.id.pager);
        pagerAdapter = new FullScreenImagePagerAdapter(getChildFragmentManager(),rootView.getContext(), imageUrlList, FullScreenFragment.this);
        mPager.setPageTransformer(true, new ZoomOutPageTransformer());
        mPager.setAdapter(pagerAdapter);
        mPager.setCurrentItem(currentPosition);
        return rootView;
    }

    public void updateAdapters(ArrayList<String> urlList) {
        if(fullScreenImagePagerAdapter != null) {
            urlArrayList = urlList;
            fullScreenImagePagerAdapter.notifyDataSetChanged();
        }
    }
}
