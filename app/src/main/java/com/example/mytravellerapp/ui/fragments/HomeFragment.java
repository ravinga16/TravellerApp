package com.example.mytravellerapp.ui.fragments;

import android.content.Intent;
import android.os.Bundle;


import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mytravellerapp.BaseApplication;
import com.example.mytravellerapp.R;
import com.example.mytravellerapp.common.CommonUtils;
import com.example.mytravellerapp.common.constants.ApplicationConstants;
import com.example.mytravellerapp.domain.ToursService;
import com.example.mytravellerapp.domain.ToursServiceImpl;
import com.example.mytravellerapp.dto.TourListItem;
import com.example.mytravellerapp.model.entities.request.LikeRequest;
import com.example.mytravellerapp.model.entities.response.AddInquireResponse;
import com.example.mytravellerapp.model.entities.response.DoLikeResponse;
import com.example.mytravellerapp.model.entities.response.GetPackageResponse;
import com.example.mytravellerapp.model.entities.response.GetTourGalleryResponse;
import com.example.mytravellerapp.model.entities.response.HomeInfoResponse;
import com.example.mytravellerapp.model.entities.response.TourInfoResponse;
import com.example.mytravellerapp.model.entities.response.TourMapDetailsResponse;
import com.example.mytravellerapp.model.entities.response.UploadTourImagesResponse;
import com.example.mytravellerapp.model.rest.BMSService;
import com.example.mytravellerapp.mvp.presenters.Presenter;
import com.example.mytravellerapp.mvp.presenters.ToursPresenter;
import com.example.mytravellerapp.mvp.presenters.ToursPresenterImpl;
import com.example.mytravellerapp.mvp.views.ToursView;
import com.example.mytravellerapp.ui.activities.MainActivity;
import com.example.mytravellerapp.ui.adapters.TourListRecycleAdapter;
import com.example.mytravellerapp.utils.AppScheduler;

import java.util.ArrayList;

public class HomeFragment extends BaseFragment implements ToursView {

    /*check*/
    public static HomeFragment homeFragment = null;
    final String TAG = HomeFragment.this.getClass().getSimpleName();

    public static String getTAG() {
        return "HomeFragment";
    }

    //    private TourListRecycleAdapter tourListAdapter;
    //    private RecyclerView.Adapter tourAdapter;
    private boolean isOnLoadMore = false;
    private final int TAKE_CONSTANT = 10;
    private boolean isPullToRefreshCall = false;
    private int page = 1;
    private int mRemovedPosition;
    private boolean emptyFeed = true;
    private boolean loadData = false;
    private Presenter tourPresenter;
    private LikeRequest likeRequest = new LikeRequest();
    private int likeButtonPosition;
    private boolean isLoadDataSuccess = false;
    private static View rootView;
    private static Presenter presenter;
    private TourListRecycleAdapter adapter;

    //UI
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = null;
        // Inflate the layout for this fragment
        try {
            rootView = inflater.inflate(R.layout.fragment_home, container, false);
            homeFragment = this;
            mRecyclerView = rootView.findViewById(R.id.recyclerView);
            mSwipeRefreshLayout = rootView.findViewById(R.id.swipe_refresh_layout);
//        presenter = new ToursPresenterImpl(getActivity(), new ToursServiceImpl(new BMSService()), new AppScheduler());
//        presenter.attachView(HomeFragment.this);
//        presenter.onCreate();
//            performGetTourInfo();
        } catch (Exception e) {
            Log.e(TAG, "onCreateView: " + e.toString());
        }

        return rootView;
    }


    @Override
    protected void setUpToolBar() {
        View mCustomView = getActivity().getLayoutInflater().inflate(R.layout.custom_actionbar_home, null);
        mToolBar.addView(mCustomView);
        Toolbar parent = (Toolbar) mCustomView.getParent();
        parent.setPadding(0, 0, 0, 0);//for tab otherwise give space in tab
        parent.setContentInsetsAbsolute(0, 0);
    }

    @Override
    protected void setUpUI() {
        initRecyclerView();
        performGetTourInfo();
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isPullToRefreshCall = true;
//                mSwipeRefreshLayout.setRefreshing(true);
                performGetTourInfo();
            }
        });
    }

    @Override
    public void initializePresenter() {
        ToursService mToursService = new ToursServiceImpl(new BMSService());
        presenter = new ToursPresenterImpl(getActivity(), mToursService, new AppScheduler());
        presenter.attachView(HomeFragment.this);
        presenter.onCreate();

    }

    protected void initRecyclerView() {
        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mLayoutManager.setAutoMeasureEnabled(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(false);

        adapter = new TourListRecycleAdapter(new ArrayList<TourListItem>(), getContext());
        mRecyclerView.setAdapter(adapter);
    }

    public void updateLikeCount(String id, boolean isLiked){
        adapter.updateLikeCountById(id, isLiked);
    }

    public void performGetTourInfo() {
        if (CommonUtils.getInstance().isNetworkConnected()) {
            setProgressDialog(true);
//            ToursPresenter presenter = new ToursPresenterImpl(getActivity(), new ToursServiceImpl(new BMSService()), new AppScheduler());
            ((ToursPresenter) presenter).getHomeInfo(10, 1);
        } else {
            showAlertDialog(false, ApplicationConstants.WARNING,
                    ApplicationConstants.ERROR_MSG_CONNECTION_LOST, null);
        }
    }

    public void performLike(String tour_id, int item_position) {
        likeButtonPosition = item_position;
        likeRequest.setTourId(tour_id);
        if (CommonUtils.getInstance().isNetworkConnected()) {
            ((ToursPresenter) presenter).doLike(likeRequest);
        }

    }

    public void performGetTour(String tourId) {
        if (CommonUtils.getInstance().isNetworkConnected()) {
            setProgressDialog(true);
            ((ToursPresenter) presenter).getTourInfo(tourId);
        } else {
            showAlertDialog(false, ApplicationConstants.WARNING,
                    ApplicationConstants.ERROR_MSG_CONNECTION_LOST, null);
        }
    }

    /*not working when the parameter passed*/
    @Override
    public void showHomeInfoResponse(HomeInfoResponse homeInfoResponse) {
        setProgressDialog(false);
        mSwipeRefreshLayout.setRefreshing(false);
        if (homeInfoResponse.isSuccess()) {
            adapter.updateData(homeInfoResponse.getTours(), 0);

        } else {
            if (homeInfoResponse.isTokenExpired()) {
                BaseApplication.getBaseApplication().exTokenClearData(getActivity());
                return;
            }
        }

    }

    @Override
    public void showDoLikeResponse(DoLikeResponse doLikeResponse) {
        if (doLikeResponse.isSuccess()) {
            adapter.updateLikeCount(likeButtonPosition, doLikeResponse.isLike());
        } else {
            if (doLikeResponse.isTokenExpired()) {
                BaseApplication.getBaseApplication().exTokenClearData(getActivity());
                return;
            }
        }

    }

    @Override
    public void showTourInfoResponse(TourInfoResponse tourInfoResponse) {
        setProgressDialog(false);
//        mSwipeRefreshLayout.setRefreshing(false);
        if (tourInfoResponse.isSuccess()) {
            ((MainActivity) getContext()).addFragment(new TourDetailsFragment().newInstance(tourInfoResponse), HomeFragment.getTAG());
        } else {
            if (tourInfoResponse.isTokenExpired()) {
                BaseApplication.getBaseApplication().exTokenClearData(getActivity());
                return;
            }
        }
    }

    @Override
    public void showGetTourGalleryResponse(GetTourGalleryResponse getTourGalleryResponse) {

    }

    @Override
    public void showGetPackageResponse(GetPackageResponse getPackageResponse) {

    }

    @Override
    public void showUploadTourImagesResponse(UploadTourImagesResponse uploadTourImagesResponse) {

    }

    @Override
    public void showInquireResponse(AddInquireResponse addInquireResponse) {

    }

    @Override
    public void showGetToursMapDetailsResponse(TourMapDetailsResponse tourMapDetailsResponse) {

    }

    @Override
    public void showMessage(String message) {

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

    @Override
    public void onDestroy() {
        homeFragment = null;
        super.onDestroy();
    }
}

