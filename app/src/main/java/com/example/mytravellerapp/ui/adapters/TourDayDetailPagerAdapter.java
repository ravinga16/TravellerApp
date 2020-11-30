package com.example.mytravellerapp.ui.adapters;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.mytravellerapp.dto.TourSchedule;
import com.example.mytravellerapp.ui.fragments.TourDayDetailFragment;

import java.util.ArrayList;

public class TourDayDetailPagerAdapter extends FragmentStatePagerAdapter {

    private Context context;
    private Fragment mFragment;
    private ArrayList<TourSchedule> tourSchedules;

    public TourDayDetailPagerAdapter(FragmentManager fm, Context context, ArrayList<TourSchedule> tourScheduleArrayList, Fragment fragment) {
        super(fm);
        this.context = context;
        this.tourSchedules = tourScheduleArrayList;
        this.mFragment = fragment;
    }

    @Override
    public Fragment getItem(int position) {
        return new TourDayDetailFragment().newInstance(tourSchedules.get(position), mFragment, position);
    }

    @Override
    public int getCount() {
        return tourSchedules.size();
    }
}
