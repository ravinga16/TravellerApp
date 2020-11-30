package com.example.mytravellerapp.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mytravellerapp.R;
import com.example.mytravellerapp.common.CommonUtils;
import com.example.mytravellerapp.common.constants.ApplicationConstants;
import com.example.mytravellerapp.domain.NotificationService;
import com.example.mytravellerapp.domain.NotificationServiceImpl;
import com.example.mytravellerapp.domain.ToursService;
import com.example.mytravellerapp.domain.ToursServiceImpl;
import com.example.mytravellerapp.dto.NotificationListItem;
import com.example.mytravellerapp.model.entities.request.UpdateReadStatusRequest;
import com.example.mytravellerapp.model.entities.response.AddInquireResponse;
import com.example.mytravellerapp.model.entities.response.DoLikeResponse;
import com.example.mytravellerapp.model.entities.response.GetPackageResponse;
import com.example.mytravellerapp.model.entities.response.GetTourGalleryResponse;
import com.example.mytravellerapp.model.entities.response.HomeInfoResponse;
import com.example.mytravellerapp.model.entities.response.NotificationCountResponse;
import com.example.mytravellerapp.model.entities.response.NotificationResponse;
import com.example.mytravellerapp.model.entities.response.TourInfoResponse;
import com.example.mytravellerapp.model.entities.response.TourMapDetailsResponse;
import com.example.mytravellerapp.model.entities.response.UpdateReadStatusResponse;
import com.example.mytravellerapp.model.entities.response.UploadTourImagesResponse;
import com.example.mytravellerapp.model.rest.BMSService;
import com.example.mytravellerapp.mvp.presenters.NotificationPresenter;
import com.example.mytravellerapp.mvp.presenters.NotificationPresenterImpl;
import com.example.mytravellerapp.mvp.presenters.Presenter;
import com.example.mytravellerapp.mvp.presenters.ToursPresenter;
import com.example.mytravellerapp.mvp.presenters.ToursPresenterImpl;
import com.example.mytravellerapp.mvp.views.NotificationView;
import com.example.mytravellerapp.mvp.views.ToursView;
import com.example.mytravellerapp.ui.activities.MainActivity;
import com.example.mytravellerapp.ui.adapters.NotificationsListRecycleAdapter;
import com.example.mytravellerapp.utils.AppScheduler;

import java.util.ArrayList;
import java.util.List;


public class NotificationsFragment extends BaseFragment implements ToursView, NotificationView {

    private static final String TAG = "NotificationsFragment";
    public static NotificationsFragment notificationsFragment;
    private NotificationsListRecycleAdapter mNotificationsListRecycleAdapter;
    private final int TAKE_CONSTANT = 10;
    private boolean isPullToRefreshCall = false;
    private int page = 1;
    //UI elements
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView recyclerView;
    private Presenter mTourPresenter;

    public static String getTAG() {
        return TAG;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_notifications, container, false);

        //Bind views
        mSwipeRefreshLayout = rootView.findViewById(R.id.swipe_refresh_layout);
        recyclerView = rootView.findViewById(R.id.notification_list);

        if (mTourPresenter != null) mTourPresenter.onCreate();

        notificationsFragment = this;
        return rootView;
    }

    @Override
    protected void setUpToolBar() {
        View mCustomView = getActivity().getLayoutInflater().inflate(R.layout.custom_actionbar_notifications, null);
        mToolBar.addView(mCustomView);
        Toolbar parent = (Toolbar) mCustomView.getParent();
        parent.setPadding(0, 0, 0, 0);//for tab otherwise give space in tab
        parent.setContentInsetsAbsolute(0, 0);

    }

    @Override
    protected void setUpUI() {
        initRecyclerView();
        performGetNotification();
        performGetNotificationCountRequest();
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isPullToRefreshCall = true;
                resetRecyclerView();
                setSwipeLayoutLoading(true);
                performGetNotification();
            }
        });
    }

    @Override
    protected void initializePresenter() {
        NotificationService mNotificationService = new NotificationServiceImpl(new BMSService());
        presenter = new NotificationPresenterImpl(getActivity(), mNotificationService, new AppScheduler());
        presenter.attachView(NotificationsFragment.this);
        presenter.onCreate();

        ToursService mToursService = new ToursServiceImpl(new BMSService());
        mTourPresenter = new ToursPresenterImpl(getActivity(), mToursService, new AppScheduler());
        mTourPresenter.attachView(NotificationsFragment.this);
        mTourPresenter.onCreate();

    }

    private void setSwipeLayoutLoading(final boolean isLoading) {
        if (mSwipeRefreshLayout == null) return;
        if (isLoading) {
            mSwipeRefreshLayout.post(new Runnable() { // show refreshlayout progress
                @Override
                public void run() {
                    try {
                        if (mSwipeRefreshLayout != null && !mSwipeRefreshLayout.isRefreshing())
                            mSwipeRefreshLayout.setRefreshing(true);
                    } catch (Exception e) {
                        Log.e(TAG, "setSwipeLayoutLoading: " + e.toString());
                    }
                }
            });
        } else {
            mSwipeRefreshLayout.post(new Runnable() { // show refreshlayout progress
                @Override
                public void run() {
                    try {
                        if (mSwipeRefreshLayout != null && mSwipeRefreshLayout.isRefreshing())
                            mSwipeRefreshLayout.setRefreshing(false);
                    } catch (Exception e) {
                        Log.e(TAG, "setSwipeLayoutLoading: " + e.toString());
                    }
                }
            });
        }
    }

    protected void initRecyclerView() {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mLayoutManager.setAutoMeasureEnabled(true);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
//        setUpItemTouchHelper();
//        setUpAnimationDecoratorHelper();


        mNotificationsListRecycleAdapter = new NotificationsListRecycleAdapter(new ArrayList<NotificationListItem>(), getContext());
        recyclerView.setAdapter(mNotificationsListRecycleAdapter);
    }
    private void resetRecyclerView() {
        mNotificationsListRecycleAdapter.updateData(null, 1);
        page = 1;
    }

    public void changeReadStatus(List<NotificationListItem> notifications){
        List<String> readNotificationList = new ArrayList<>();

        for(NotificationListItem data : notifications){
            if(!data.isIsread()){
                readNotificationList.add(data.getMynotificationId());
            }
        }

        if(readNotificationList.size() > 0){
            UpdateReadStatusRequest request = new UpdateReadStatusRequest();
            request.setIsread(true);
            request.setMynotificationId(readNotificationList);
            performUpdateNotificationRequest(request);
        }

    }

    public void performGetTour(String tourId) {
        if (CommonUtils.getInstance().isNetworkConnected()) {
            setProgressDialog(true);
            ((ToursPresenter) mTourPresenter).getTourInfo(tourId);
        } else {
            showAlertDialog(false, ApplicationConstants.WARNING,
                    ApplicationConstants.ERROR_MSG_CONNECTION_LOST, null);
        }
    }


    private void performGetNotification() {
        if (CommonUtils.getInstance().isNetworkConnected()) {
            setProgressDialog(true);
            ((NotificationPresenter) presenter).getNotification(TAKE_CONSTANT, page);
        } else {
            showAlertDialog(false, ApplicationConstants.WARNING,
                    ApplicationConstants.ERROR_MSG_CONNECTION_LOST, null);
        }
    }

    private void performUpdateNotificationRequest(UpdateReadStatusRequest updateReadStatusRequest) {
        if (CommonUtils.getInstance().isNetworkConnected()) {
            ((NotificationPresenter) presenter).updateNotificationReadStatus(updateReadStatusRequest);
        } else {
            showAlertDialog(false, ApplicationConstants.WARNING,
                    ApplicationConstants.ERROR_MSG_CONNECTION_LOST, null);
        }
    }

    private void performGetNotificationCountRequest() {
        if (CommonUtils.getInstance().isNetworkConnected()) {
            ((NotificationPresenter) presenter).getNotificationCount();
        } else {
            showAlertDialog(false, ApplicationConstants.WARNING,
                    ApplicationConstants.ERROR_MSG_CONNECTION_LOST, null);
        }
    }

    @Override
    public void showNotificationCountResponse(NotificationCountResponse notificationCountResponse) {
        if (notificationCountResponse.isSuccess() &&
                notificationCountResponse.getUnRead_notification_count() != null &&
                !notificationCountResponse.getUnRead_notification_count().equals("0")) {

            int count = Integer.parseInt(notificationCountResponse.getUnRead_notification_count());
            String countText = "0";
            if(count < 10) countText = "0" + count;
            else countText = "" + count;
            ((MainActivity)getActivity()).removeBadge();
            ((MainActivity)getActivity()).showBadge( countText);
        } else {
            ((MainActivity)getActivity()).removeBadge();
        }
    }

    @Override
    public void showNotificationResponse(NotificationResponse notificationResponse) {
        setProgressDialog(false);
        setSwipeLayoutLoading(false);
        if(notificationResponse.isSuccess()){
            if (notificationResponse.getData() != null && notificationResponse.getData().getNotifications() != null && notificationResponse.getData().getNotifications().size() > 0) {
                changeReadStatus(notificationResponse.getData().getNotifications());
//                ((MainActivity)getActivity()).removeBadge();
                if (isPullToRefreshCall) {
                    isPullToRefreshCall = false;
                    mNotificationsListRecycleAdapter.updateData(null, 1);
                }
                mNotificationsListRecycleAdapter.updateData(notificationResponse.getData().getNotifications(), 0);
            }
        }

    }

    @Override
    public void showUpdateNotificationReadStatusResponse(UpdateReadStatusResponse updateReadStatusResponse) {
        if(updateReadStatusResponse.isSuccess()){
            performGetNotificationCountRequest();
        }
    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public void showHomeInfoResponse(HomeInfoResponse homeInfoResponse) {

    }

    @Override
    public void showDoLikeResponse(DoLikeResponse doLikeResponse) {

    }

    @Override
    public void showTourInfoResponse(TourInfoResponse tourInfoResponse) {
        setProgressDialog(false);
        if (tourInfoResponse.isSuccess()) {
            ((MainActivity)getActivity()).addFragment(new TourDetailsFragment().newInstance(tourInfoResponse), TourDetailsFragment.getTAG());
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
}
