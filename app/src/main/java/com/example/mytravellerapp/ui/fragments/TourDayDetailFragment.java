package com.example.mytravellerapp.ui.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mytravellerapp.R;
import com.example.mytravellerapp.common.constants.DomainConstants;
import com.example.mytravellerapp.dto.TourSchedule;
import com.example.mytravellerapp.ui.activities.BaseActivity;
import com.example.mytravellerapp.ui.utils.BaseBackPressedListener;
import com.squareup.picasso.Picasso;

public class TourDayDetailFragment extends BaseFragment implements BaseBackPressedListener.OnBackPressedListener {

    private static final String TAG = "TourDayDetailFragment";
    private TourSchedule tourSchedule;
    private Fragment mFragment;
    private int day_number; //day1, day2,...
    private static View rootView;
    //ui
    private ImageView dayImage;
    private TextView day_tour_name;
    private TextView text_destination;
    private TextView text_travel_time;
    private TextView text_destination_name;
    private TextView text_extra;
    private TextView no_of_day;
    private Toolbar mToolBar;

    public static TourDayDetailFragment newInstance(TourSchedule tourSchedule, Fragment parentFragment, int dayNumber) {

        TourDayDetailFragment fragment = new TourDayDetailFragment();
        fragment.mFragment = parentFragment;
        fragment.tourSchedule = tourSchedule;
        fragment.day_number = dayNumber;
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_tour_day_detail, container, false);

        dayImage = rootView.findViewById(R.id.featured_image);
        day_tour_name = rootView.findViewById(R.id.day_tour_name);
        text_destination = rootView.findViewById(R.id.text_destination);
        text_travel_time = rootView.findViewById(R.id.text_travel_time);
        text_destination_name = rootView.findViewById(R.id.text_destination_name);
        text_extra = rootView.findViewById(R.id.text_extra);
        no_of_day = rootView.findViewById(R.id.no_of_day);

        if(tourSchedule.getDay_image()!=null){
            Picasso.with(getContext()).load(DomainConstants.TOUR_IMAGE_URL+tourSchedule.getDay_image()).into(dayImage);
        }
        no_of_day.setText((day_number+1)+" PLACE "+(day_number+1)+" DAY");
        day_tour_name.setText(tourSchedule.getScheduleName()+"");
        text_destination.setText(""+tourSchedule.getStartLocation()+" to "+tourSchedule.getEndLocation()+"");
        text_travel_time.setText(""+tourSchedule.getTravellingTime()+""+" drive ");
        text_destination_name.setText(""+tourSchedule.getScheduleName()+"");
        int i = 0;
        String otherVisitDetails = "";
        for(i=0; i<tourSchedule.getOtherVisitingPlaces().size();i++){
            otherVisitDetails+=tourSchedule.getOtherVisitingPlaces().get(i);
            if(i!=tourSchedule.getOtherVisitingPlaces().size()-1){
                otherVisitDetails+=","+"\n";
            }
        }otherVisitDetails+=".";
        text_extra.setText(otherVisitDetails);
        return rootView;
    }


    @Override
    public void doBack() {
        if (mFragment != null) mFragment.getFragmentManager().popBackStack();
    }

    @Override
    public void setUpToolBar() {
        mToolBar = rootView.findViewById(R.id.toolbar);
        View mCustomView = getActivity().getLayoutInflater().inflate(R.layout.custom_actionbar_back, null);
        ImageView btnBack = mCustomView.findViewById(R.id.imgVbackAr);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFragment != null) mFragment.getFragmentManager().popBackStack();
            }
        });
        TextView mTitle = mCustomView.findViewById(R.id.title_text_back);
        mToolBar.addView(mCustomView);
        mTitle.setText(R.string.action_bar_title_summery);
        Toolbar parent = (Toolbar) mCustomView.getParent();
        parent.setPadding(0, 0, 0, 0);//for tab otherwise give space in tab
        parent.setContentInsetsAbsolute(0, 0);
    }


    @Override
    protected void setUpUI() {

    }

    @Override
    protected void initializePresenter() {

    }
}
