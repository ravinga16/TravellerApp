package com.example.mytravellerapp.ui.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mytravellerapp.R;
import com.example.mytravellerapp.Style.ZoomOutPageTransformer;
import com.example.mytravellerapp.dto.TourSchedule;
import com.example.mytravellerapp.ui.adapters.TourDayDetailPagerAdapter;

import java.util.ArrayList;


public class TourDayDetailOtherFragment extends Fragment {
    private static final String TAG = "TourDayDetailOtherFragm";

    public static String getTAG() {
        return TAG;
    }

    private ViewPager mPager;
    private PagerAdapter pagerAdapter;
    private ArrayList<TourSchedule> tourSchedules = new ArrayList<>();
    private static TourDayDetailOtherFragment tourDayDetailOtherFragment = null;

    public TourDayDetailOtherFragment(ArrayList<TourSchedule> tourSchedules) {
        this.tourSchedules = tourSchedules;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_tour_day_detail_other, container, false);
        tourDayDetailOtherFragment = this;
        mPager = rootView.findViewById(R.id.pager);
        pagerAdapter = new TourDayDetailPagerAdapter(getChildFragmentManager(), getContext(), tourSchedules, TourDayDetailOtherFragment.this);
        mPager.setPageTransformer(true, new ZoomOutPageTransformer());
        mPager.setAdapter(pagerAdapter);
        mPager.setCurrentItem(0);

        return rootView;
    }

}
