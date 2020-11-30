package com.example.mytravellerapp.ui.activities;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.mytravellerapp.R;
import com.example.mytravellerapp.common.CommonUtils;
import com.example.mytravellerapp.common.constants.ApplicationConstants;
import com.example.mytravellerapp.common.constants.IPreferencesKeys;
import com.example.mytravellerapp.domain.NotificationService;
import com.example.mytravellerapp.domain.NotificationServiceImpl;
import com.example.mytravellerapp.domain.UserService;
import com.example.mytravellerapp.domain.UserServiceImpl;
import com.example.mytravellerapp.dto.NotificationBody;
import com.example.mytravellerapp.dto.TourSchedule;
import com.example.mytravellerapp.model.entities.request.RegisterFcmTokenRequest;
import com.example.mytravellerapp.model.entities.response.AddInquireResponse;
import com.example.mytravellerapp.model.entities.response.BaseServerResponse;
import com.example.mytravellerapp.model.entities.response.DoLikeResponse;
import com.example.mytravellerapp.model.entities.response.GetPackageResponse;
import com.example.mytravellerapp.model.entities.response.GetTourGalleryResponse;
import com.example.mytravellerapp.model.entities.response.HomeInfoResponse;
import com.example.mytravellerapp.model.entities.response.LogOutResponse;
import com.example.mytravellerapp.model.entities.response.LoginResponse;
import com.example.mytravellerapp.model.entities.response.NotificationCountResponse;
import com.example.mytravellerapp.model.entities.response.NotificationResponse;
import com.example.mytravellerapp.model.entities.response.ProfileResponse;
import com.example.mytravellerapp.model.entities.response.ProfileUpdateResponse;
import com.example.mytravellerapp.model.entities.response.RegisterFcmResponse;
import com.example.mytravellerapp.model.entities.response.RegisterResponse;
import com.example.mytravellerapp.model.entities.response.TourInfoResponse;
import com.example.mytravellerapp.model.entities.response.TourMapDetailsResponse;
import com.example.mytravellerapp.model.entities.response.UpdateReadStatusResponse;
import com.example.mytravellerapp.model.entities.response.UploadProfileImageResponse;
import com.example.mytravellerapp.model.entities.response.UploadTourImagesResponse;
import com.example.mytravellerapp.model.entities.response.ValidateResetCodeResponse;
import com.example.mytravellerapp.model.rest.BMSService;
import com.example.mytravellerapp.mvp.presenters.NotificationPresenter;
import com.example.mytravellerapp.mvp.presenters.NotificationPresenterImpl;
import com.example.mytravellerapp.mvp.presenters.Presenter;
import com.example.mytravellerapp.mvp.presenters.UserPresenter;
import com.example.mytravellerapp.mvp.presenters.UserPresenterImpl;
import com.example.mytravellerapp.mvp.views.NotificationView;
import com.example.mytravellerapp.mvp.views.ToursView;
import com.example.mytravellerapp.mvp.views.UserView;
import com.example.mytravellerapp.service.MyFirebaseMessagingService;
import com.example.mytravellerapp.service.SharedPrefManager;
import com.example.mytravellerapp.ui.fragments.FullScreenFragment;
import com.example.mytravellerapp.ui.fragments.GalleryFragment;
import com.example.mytravellerapp.ui.fragments.HomeFragment;
import com.example.mytravellerapp.ui.fragments.NotificationsFragment;
import com.example.mytravellerapp.ui.fragments.ProfileFragment;
import com.example.mytravellerapp.ui.fragments.TourDayDetailOtherFragment;
import com.example.mytravellerapp.ui.fragments.TourGoogleMainMapFragment;
import com.example.mytravellerapp.ui.utils.BaseBackPressedListener;
import com.example.mytravellerapp.utils.AppScheduler;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;


public class MainActivity extends BaseActivity implements UserView, ToursView, NotificationView {

    private static final String TAG = "MainActivity";
    private static ArrayList<Integer> galleryFragmentExpandListStatus;


    private SharedPreferences preferences;
    private ProgressDialog progressDialog;
    private BottomNavigationView bottomNavigation;
    protected Presenter presenter;
    private NotificationBody mNotificationBody;
    protected Presenter mTourPresenter;
    protected Presenter mNotificationPresenter;
    protected BaseBackPressedListener.OnBackPressedListener onBackPressedListener = null;

    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_main);

            bottomNavigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
            bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

            if (findViewById(R.id.fragment_container) != null) {
                loadFragment(new HomeFragment());
            }

            initializePresenter();
            preferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
//            LocalBroadcastManager.getInstance(this).registerReceiver(mHandler, new IntentFilter("com.example.mytravellerapp_FCM-MESSAGE"));

            galleryFragmentExpandListStatus = new ArrayList<>();
            performGetNotificationCountRequest();
            registerFCMToken();

            /*broadcast receive from fireBaseInstance method*/
            broadcastReceiver = new BroadcastReceiver(){
                @Override
                public void onReceive(Context context, Intent intent) {
                    pushNotificationReceived();
                }
            };
            registerReceiver(broadcastReceiver, new IntentFilter(MyFirebaseMessagingService.TOKEN_NOTIFICATION));

        } catch (Exception ex) {
            Log.e(TAG, "onCreate: " + ex.toString());
        }
    }

    public void initializePresenter() {
        UserService mUserService = new UserServiceImpl(new BMSService());
        presenter = new UserPresenterImpl(MainActivity.this, mUserService, new AppScheduler());
        presenter.attachView(MainActivity.this);
        presenter.onCreate();

        NotificationService mNotificationService = new NotificationServiceImpl(new BMSService());
        mNotificationPresenter = new NotificationPresenterImpl(MainActivity.this, mNotificationService, new AppScheduler());
        mNotificationPresenter.attachView(MainActivity.this);
        mNotificationPresenter.onCreate();
    }

    private void registerFCMToken() {
        registerReceiver(broadcastReceiver, new IntentFilter(MyFirebaseMessagingService.TOKEN_BROADCAST));
        final boolean isRegisterFcm = preferences.getBoolean(IPreferencesKeys.IS_REGISTER_FCM, false);
        //      FCM token Register when onNewToken method call
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String fcmToken = SharedPrefManager.getInstance(MainActivity.this).getDeviceToken();
                if (!isRegisterFcm) {
                    RegisterFcmTokenRequest request = new RegisterFcmTokenRequest();
                    request.setFcmtoken(fcmToken);
                    performRegisterFcmRequest(request);
                }
            }
        };

//        FCM token Register when new user Login
        if (SharedPrefManager.getInstance(this).getDeviceToken() != null && !isRegisterFcm) {
            String fcmToken = SharedPrefManager.getInstance(MainActivity.this).getDeviceToken();
            RegisterFcmTokenRequest request = new RegisterFcmTokenRequest();
            request.setFcmtoken(fcmToken);

            performRegisterFcmRequest(request);
        }
    }

    private void pushNotificationReceived(){
        performGetNotificationCountRequest();
    }


    public static void addExpandGalleryFragment(ArrayList<Integer> expandList) {
        galleryFragmentExpandListStatus = expandList;
    }

    public static ArrayList<Integer> getExpandStatus() {
        return galleryFragmentExpandListStatus;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    loadFragment(new HomeFragment());
                    return true;
                case R.id.navigation_map:
                    loadFragment(new TourGoogleMainMapFragment());
                    return true;
                case R.id.navigation_gallery:
                    loadFragment(new GalleryFragment());
                    return true;
                case R.id.navigation_notifications:
                    loadFragment(new NotificationsFragment());
                    return true;
                case R.id.navigation_profile:
                    loadFragment(new ProfileFragment());
                    return true;
            }
            return false;
        }
    };

    public void selectSpecificTab(int tabId) {
        switch (tabId) {
            case 0:
                bottomNavigation.setSelectedItemId(R.id.navigation_home);
                break;
            case 1:
                bottomNavigation.setSelectedItemId(R.id.navigation_map);
                break;
            case 2:
                bottomNavigation.setSelectedItemId(R.id.navigation_gallery);
                break;
            case 3:
                bottomNavigation.setSelectedItemId(R.id.navigation_notifications);
                break;
            case 4:
                bottomNavigation.setSelectedItemId(R.id.navigation_profile);
                break;
        }
    }


    public void setFragment(Fragment fragment, String TAG) {
        if (fragment == null) return;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left);
        transaction.replace(R.id.fragment_container, fragment, TAG);
        if (TAG != null) transaction.addToBackStack(TAG);
        transaction.commitAllowingStateLoss();
    }

    public void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
//        transaction.addToBackStack(null);
        transaction.commit();
    }


    public void addFragment(Fragment fragment, String TAG) {
        if (fragment == null) return;
        FragmentTransaction fragTransaction = getSupportFragmentManager().beginTransaction();
        fragTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left);
//        fragTransaction.setCustomAnimations(R.anim.slide_in_left, 0, 0, R.anim.slide_out_left );
        fragTransaction.add(R.id.fragment_container, fragment, TAG);
        fragTransaction.addToBackStack(TAG);
        fragTransaction.commitAllowingStateLoss();
    }

    public void loadScreenSlider(ArrayList<String> imageList, int currentPostion) {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, FullScreenFragment.newInstance(imageList, currentPostion)).addToBackStack(null).commit();
    }

    public void loadTourDayDetailSlider(ArrayList<TourSchedule> tourScheduleArrayList){
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, new TourDayDetailOtherFragment(tourScheduleArrayList)).addToBackStack(null).commit();
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        if (onBackPressedListener != null) onBackPressedListener.doBack();
        else super.onBackPressed();
//        if (onBackPressedListener != null) onBackPressedListener.doBack();
//        else super.onBackPressed();
    }

    public void setOnBackPressedListener(BaseBackPressedListener.OnBackPressedListener onBackPressedListener) {
        this.onBackPressedListener = onBackPressedListener;
    }

    @Override
    protected void onStop() {
        super.onStop();
        addExpandGalleryFragment(new ArrayList<Integer>());
    }

    public void showBadge(String value) {
            BottomNavigationItemView itemView = bottomNavigation.findViewById(R.id.navigation_notifications);
            View badge = LayoutInflater.from(this).inflate(R.layout.layout_news_badge, bottomNavigation, false);

            TextView text = badge.findViewById(R.id.badge_text_view);
            text.setText(value);
            itemView.addView(badge);

    }
    public  void removeBadge() {
        BottomNavigationItemView itemView = bottomNavigation.findViewById(R.id.navigation_notifications);
        if (itemView.getChildCount() == 3) {
            itemView.removeViewAt(2);
        }
    }

    public void performGetNotificationCountRequest() {
        if (CommonUtils.getInstance().isNetworkConnected()) {
//            setProgressDialog(true);
            ((NotificationPresenter) mNotificationPresenter).getNotificationCount();
        } else {
            showAlertDialog(ApplicationConstants.WARNING, ApplicationConstants.ERROR_MSG_CONNECTION_LOST);
        }
    }

    private void performRegisterFcmRequest(RegisterFcmTokenRequest registerFcmTokenRequest) {
        if (CommonUtils.getInstance().isNetworkConnected()) {
//            setProgressDialog(true);
            ((UserPresenter) presenter).doRegisterFcmToken(registerFcmTokenRequest);
        } else {
            showAlertDialog(ApplicationConstants.WARNING, ApplicationConstants.ERROR_MSG_CONNECTION_LOST);
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
            removeBadge();
            showBadge(countText);
        } else {
            removeBadge();
        }
    }

    @Override
    public void showNotificationResponse(NotificationResponse notificationResponse) {


    }

    @Override
    public void showUpdateNotificationReadStatusResponse(UpdateReadStatusResponse updateReadStatusResponse) {

    }

    @Override
    public void showHomeInfoResponse(HomeInfoResponse homeInfoResponse) {

    }

    @Override
    public void showDoLikeResponse(DoLikeResponse doLikeResponse) {

    }

    @Override
    public void showTourInfoResponse(TourInfoResponse TourInfoResponse) {

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
    public void showLoginResponse(LoginResponse loginResponse) {

    }

    @Override
    public void showProfileResponse(ProfileResponse profileResponse) {

    }

    @Override
    public void showUpdateProfileResponse(ProfileUpdateResponse profileUpdateResponse) {

    }

    @Override
    public void showLogOutResponse(LogOutResponse logOutResponse) {

    }

    @Override
    public void showUploadProfileImageResponse(UploadProfileImageResponse uploadProfileImageResponse) {

    }

    @Override
    public void showRegisterFcmResponse(RegisterFcmResponse registerFcmResponse) {
        preferences.edit().putBoolean(IPreferencesKeys.IS_REGISTER_FCM, true).apply();
        if (registerFcmResponse.isSuccess()) {

        }
    }

    @Override
    public void showRegisterResponse(RegisterResponse registerResponse) {

    }

    @Override
    public void showForgotPasswordResponse(BaseServerResponse baseServerResponse) {

    }

    @Override
    public void showResetPasswordResponse(BaseServerResponse baseServerResponse) {

    }

    @Override
    public void showValidateResetCodeResponse(ValidateResetCodeResponse validateResetCodeResponse) {

    }

    @Override
    public void showMessage(String message) {

    }
}
