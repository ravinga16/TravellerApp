package com.example.mytravellerapp.ui.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;

import com.example.mytravellerapp.BaseApplication;
import com.example.mytravellerapp.R;
import com.example.mytravellerapp.common.CommonUtils;
import com.getHomeInfoexample.mytravellerapp.common.constants.DomainConstants;
import com.example.mytravellerapp.domain.ToursService;
import com.example.mytravellerapp.domain.ToursServiceImpl;
import com.example.mytravellerapp.dto.GalleryGroupItem;
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
import com.example.mytravellerapp.ui.activities.UploadPhotosActivity;
import com.example.mytravellerapp.ui.adapters.GalleryExpandableArrayAdapter;
import com.example.mytravellerapp.utils.AppScheduler;

import java.util.ArrayList;
import java.util.List;

public class GalleryFragment extends BaseFragment implements ToursView {
    private static final String TAG = "GalleryFragment";
    private static Presenter presenter;
    public static GalleryFragment galleryFragment;
    private static View rootView;
    private ExpandableListView expandableListView;
    private ArrayList<String> galleryItemList;
    private GalleryExpandableArrayAdapter galleryExpandableArrayAdapter;
    protected static ArrayList<Integer> expandedPositionList;
    private static String tourName; // gallery container click in home fragment

    public static String getTAG() {
        return TAG;
    }

    public static GalleryFragment newInstance(String tourname) {

        Bundle args = new Bundle();
        tourName = tourname;
        GalleryFragment fragment = new GalleryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_gallery, container, false);

        //UI binding
        expandableListView = rootView.findViewById(R.id.gallery_container);

//        performGetTourGalleryRequest();
        galleryFragment = this;

        expandedPositionList = new ArrayList<>();
        return rootView;
    }

    public void initializePresenter() {
        ToursService mToursService = new ToursServiceImpl(new BMSService());
        presenter = new ToursPresenterImpl(getActivity(), mToursService, new AppScheduler());
        presenter.attachView(GalleryFragment.this);
        presenter.onCreate();
    }

    @Override
    protected void setUpToolBar() {
        View mCustomView = getActivity().getLayoutInflater().inflate(R.layout.custom_actionbar_gallery, null);
        ImageView uploadImage = mCustomView.findViewById(R.id.btn_upload_image);
        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //uploading image from gallery action
                startActivity(new Intent(getActivity(), UploadPhotosActivity.class));
                ((MainActivity)getContext()).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
        mToolBar.addView(mCustomView);
        Toolbar parent = (Toolbar) mCustomView.getParent();
        parent.setPadding(0, 0, 0, 0);//for tab otherwise give space in tab
        parent.setContentInsetsAbsolute(0, 0);
    }

    @Override
    protected void setUpUI() {
        initRecyclerView();
        galleryItemList = new ArrayList<>();
        performGetTourGalleryRequest();
    }
    protected void initRecyclerView() {
//        galleryExpandableArrayAdapter = new GalleryExpandableArrayAdapter(
//                getActivity(), mExpandableListView, new ArrayList<GalleryGroupItem>());
        galleryExpandableArrayAdapter = new GalleryExpandableArrayAdapter(new ArrayList<GalleryGroupItem>(), getContext(),expandableListView);
//        galleryExpandableArrayAdapter.onGroupExpanded(5);
//        galleryExpandableArrayAdapter.expandSelectedItem();
        expandableListView.setAdapter(galleryExpandableArrayAdapter);
    }

    public void performGetTourGalleryRequest() {
        if (CommonUtils.getInstance().isNetworkConnected()) {
            setProgressDialog(true);
            ((ToursPresenter) presenter).getTourGallery();
        }
    }

    @Override
    public void showGetTourGalleryResponse(GetTourGalleryResponse getTourGalleryResponse) {
        setProgressDialog(false);
        if (getTourGalleryResponse.isSuccess()) {
            Log.d(TAG, "showGetTourGalleryResponse: ========>>>>>>>"+getTourGalleryResponse.getCount());
            showImageList(getTourGalleryResponse.getTours());
        } else {
            if (getTourGalleryResponse.isTokenExpired()) {
                BaseApplication.getBaseApplication().exTokenClearData(getActivity());
                return;
            }
        }
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

    public void showImageList(final List<GalleryGroupItem> galleryItems) {
        new Handler(new Handler.Callback() {

            @Override
            public boolean handleMessage(Message msg) {
                int count = 0;
                int groupPosition = 0;
                for (GalleryGroupItem i : galleryItems) {
                    if (i.getTourName().equals(tourName)) {
                        groupPosition = count;
                        break;
                    }
                    count++;
                }

                try {

                    galleryExpandableArrayAdapter.updateData(galleryItems, 0);
//                    galleryExpandableArrayAdapter = new GalleryExpandableArrayAdapter((ArrayList<GalleryGroupItem>) galleryItems, getActivity(), expandableListView);
//                    expandableListView.setAdapter(galleryExpandableArrayAdapter);
                    expandableListView.expandGroup(groupPosition);
                    /*modify part*/
//                    galleryExpandableArrayAdapter.updateData(galleryItems, 0);
//                    expandableListView.expandGroup(groupPosition);

                } catch (Exception e) {
                    e.printStackTrace();
                }

                return false;
            }

        }).sendEmptyMessage(0);

//        int expand_position = 0;
//        for(int i=0; i< galleryItems.size();i++){
//            if(galleryItems.get(i).getTourName().equals(tourName)){
//                expand_position = i;
//            }
//        }
//        galleryExpandableArrayAdapter = new GalleryExpandableArrayAdapter((ArrayList<GalleryGroupItem>) galleryItems, rootView.getContext(), expandableListView);
//        expandableListView.setAdapter(galleryExpandableArrayAdapter);
//        expandableListView.expandGroup(expand_position);
//        /*check whether and group is expanded, and expand if available*/
//        ArrayList<Integer> expandedPositionListStatus = MainActivity.getExpandStatus();
//        if(expandedPositionListStatus.size()!=0){
//            for(int position:expandedPositionListStatus){
//                expandableListView.expandGroup(position);
//            }
//        }

    }

    public void gotoImageFullScreen(ArrayList<String> feedItemList, String imagePath) {
//        goToFullScreen(galleryItemList, getImagePosition(imagePath));
        ArrayList<String> tourImageList = new ArrayList<>(); //image list with the final urls
        for (int i = 0; i < feedItemList.size(); i++) {
            tourImageList.add(DomainConstants.TOUR_IMAGE_URL + feedItemList.get(i));
        }
        goToFullScreen(tourImageList, getImagePosition(tourImageList, imagePath));
    }

    //get the image position, user selected
    private int getImagePosition(ArrayList<String> tourImageList, String url) {
        int position = 0;
        if (tourImageList == null) return position;
        for (int i = 0; i < tourImageList.size(); i++) {
            if (tourImageList.get(i).equals(url)) position = i;
        }
        return position;
    }

    //calling the MainActivity method to get the image slider
    private void goToFullScreen(ArrayList<String> galleryContents, int position) {
        try {
            if (CommonUtils.getInstance().isNetworkConnected()) {
                ((MainActivity) getActivity()).loadScreenSlider(galleryContents, position);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /*-------------------------------*/
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
    public void showMessage(String message) {

    }


    public static void add(int expandedPosition) {
        expandedPositionList.add(expandedPosition);
        MainActivity.addExpandGalleryFragment(expandedPositionList);
    }

    public static void remove(int collapsedPosition) {

        for (int i = 0; i < expandedPositionList.size(); i++) {
            if (expandedPositionList.get(i) == collapsedPosition) {
                expandedPositionList.remove(i);
            }
        }
        MainActivity.addExpandGalleryFragment(expandedPositionList);
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void updateFullScreenGalleryImages(List<String> galleryItems) {
        for (int i = 0; i < galleryItems.size(); i++) {
            galleryItemList.add(DomainConstants.TOUR_IMAGE_URL + galleryItems.get(i));
        }
        if (FullScreenFragment.fullScreenFragment != null)
            FullScreenFragment.fullScreenFragment.updateAdapters(galleryItemList);
    }

    public void performInitialRequest() {
        performGetTourGalleryRequest();
    }

}
