
package com.example.mytravellerapp.ui.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.mytravellerapp.BaseApplication;
import com.example.mytravellerapp.R;
import com.example.mytravellerapp.common.CommonUtils;
import com.example.mytravellerapp.common.constants.ApplicationConstants;
import com.example.mytravellerapp.common.constants.DomainConstants;
import com.example.mytravellerapp.domain.ToursService;
import com.example.mytravellerapp.domain.ToursServiceImpl;
import com.example.mytravellerapp.dto.TourMapDetails;
import com.example.mytravellerapp.model.entities.response.AddInquireResponse;
import com.example.mytravellerapp.model.entities.response.DoLikeResponse;
import com.example.mytravellerapp.model.entities.response.GetPackageResponse;
import com.example.mytravellerapp.model.entities.response.GetTourGalleryResponse;
import com.example.mytravellerapp.model.entities.response.HomeInfoResponse;
import com.example.mytravellerapp.model.entities.response.TourInfoResponse;
import com.example.mytravellerapp.model.entities.response.TourMapDetailsResponse;
import com.example.mytravellerapp.model.entities.response.UploadTourImagesResponse;
import com.example.mytravellerapp.model.rest.BMSService;
import com.example.mytravellerapp.mvp.presenters.ToursPresenter;
import com.example.mytravellerapp.mvp.presenters.ToursPresenterImpl;
import com.example.mytravellerapp.mvp.views.ToursView;
import com.example.mytravellerapp.ui.activities.MainActivity;
import com.example.mytravellerapp.utils.AppScheduler;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class TourGoogleMainMapFragment extends BaseFragment implements
        ToursView,
        OnMapReadyCallback {

    private static final String TAG = "TourGoogleMainMapFragme";

    public static String getTAG() {
        return TAG;
    }

    public static TourGoogleMainMapFragment tourGoogleMainMapFragment;
    private GoogleMap mMap;
    private IconGenerator clusterItemIconGenerator;
    private View clusterSingleItemView;
    private ArrayList<TourMapDetails> tourMapDetails;
    //UI
    private MapView mMapView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = null;
        try {
            // Inflate the layout for this fragment
            rootView = inflater.inflate(R.layout.fragment_tour_google_map, container, false);
            clusterSingleItemView = inflater.inflate(R.layout.single_cluster_marker_view, null);
            clusterItemIconGenerator = new IconGenerator(getContext());

            mMapView = rootView.findViewById(R.id.mapView);
            MapsInitializer.initialize(this.getActivity());
            mMapView.onCreate(savedInstanceState);
            mMapView.getMapAsync(this);
        } catch (Exception e) {
            Log.e(TAG, "onCreateView: " + e.toString());
        }

        return rootView;
    }

    @Override
    protected void setUpToolBar() {
        View mCustomView = getActivity().getLayoutInflater().inflate(R.layout.custom_actionbar_home, null);
        TextView mSubTitle = mCustomView.findViewById(R.id.title_sub);
        TextView mMainTitle = mCustomView.findViewById(R.id.title_main);
        mToolBar.addView(mCustomView);
        mSubTitle.setText((R.string.action_bar_title_tour_map));
        mMainTitle.setText((R.string.action_bar_title_sri_lanka));
        Toolbar parent = (Toolbar) mCustomView.getParent();
        parent.setPadding(0, 0, 0, 0);//for tab otherwise give space in tab
        parent.setContentInsetsAbsolute(0, 0);
    }

    @Override
    protected void setUpUI() {
        performGetTourMapDetailsRequest();
    }

    @Override
    protected void initializePresenter() {
        ToursService mToursService = new ToursServiceImpl(new BMSService());
        presenter = new ToursPresenterImpl(getActivity(), mToursService, new AppScheduler());
        presenter.attachView(TourGoogleMainMapFragment.this);
        presenter.onCreate();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (mMap != null) {
            return;
        }
        mMap = googleMap;
    }

    public void startDemo() {
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.clear();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(7.756677, 80.648336), 7.4f));//7.3f
        //adding markers
        if (tourMapDetails != null && tourMapDetails.size() > 0) {
            final int[] temp = {0};
            temp[0] = 0;
            while (temp[0] < tourMapDetails.size()) {
                final TourMapDetails current_tour = tourMapDetails.get(temp[0]);
                String imgUrl = DomainConstants.TOUR_IMAGE_URL + current_tour.getMapIconImage(); //image ur; for the marker icon

                ImageView imageView = clusterSingleItemView.findViewById(R.id.image_view); //custom marker view
//                ProgressBar progressBar = clusterSingleItemView.findViewById(R.id.image_progress);

                Picasso.with(getContext()).load(imgUrl).resize(60, 60).centerCrop().into(imageView, new com.squareup.picasso.Callback() {

                    @Override
                    public void onSuccess() {
                        temp[0] += 1;
                    }

                    @Override
                    public void onError() {
                    }
                });


                clusterItemIconGenerator.setContentView(clusterSingleItemView);
                Bitmap icon = clusterItemIconGenerator.makeIcon();

                MarkerOptions markerOptions = new MarkerOptions();//adding the marker info
                markerOptions.position(setPosition(current_tour.getMainLat(), current_tour.getMainLong())).title(current_tour.getTourName()).snippet("Hello World").icon(BitmapDescriptorFactory.fromBitmap(icon));

                mMap.addMarker(markerOptions);//adding the marker to the map
                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        performTourDetailFragmentLoad(marker.getTitle());
                        return true;
                    }
                });
            }
            setProgressDialog(false);
        } else {
            Log.e(TAG, "startDemo: Null object: tourMapDetailList");
        }
    }

    public LatLng setPosition(String lat, String lng) {
        Double d_lat = Double.parseDouble(lat);
        Double d_lng = Double.parseDouble(lng);

        return new LatLng(d_lat, d_lng);
    }

    public void performTourDetailFragmentLoad(String locationName) {
        for (int i = 0; i < tourMapDetails.size(); i++) {
            if (locationName.equals(tourMapDetails.get(i).getTourName())) {
                performGetTour(tourMapDetails.get(i).get_id());
            }
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

    public void performGetTourMapDetailsRequest() {
        if (CommonUtils.getInstance().isNetworkConnected()) {
            setProgressDialog(true);
            ((ToursPresenter) presenter).getTourMapDetails();
        } else {
            showAlertDialog(false, ApplicationConstants.WARNING,
                    ApplicationConstants.ERROR_MSG_CONNECTION_LOST, null);
        }
    }

    @Override
    public void showTourInfoResponse(TourInfoResponse tourInfoResponse) {
        setProgressDialog(false);
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
    public void showHomeInfoResponse(HomeInfoResponse homeInfoResponse) {

    }

    @Override
    public void showDoLikeResponse(DoLikeResponse doLikeResponse) {

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
        setProgressDialog(false);
        if ((!isAdded() || !isVisible())) return;
        if (tourMapDetailsResponse.isSuccess()) {
            if (tourMapDetailsResponse.getTours() != null && tourMapDetailsResponse.getTours().size() > 0) {
                //get the positions to indicate
                tourMapDetails = (ArrayList<TourMapDetails>) tourMapDetailsResponse.getTours();
                startDemo();
            }
        } else {
            if (tourMapDetailsResponse.isTokenExpired()) {
                BaseApplication.getBaseApplication().exTokenClearData(getActivity());
                return;
            } else if (tourMapDetailsResponse.isAPIError() && tourMapDetailsResponse.getMessage() != null) {
                showTopSnackBar(tourMapDetailsResponse.getMessage(), getResources().getColor(R.color.sign_up_password_text_color));
            } else {
                showTopSnackBar(tourMapDetailsResponse.getMessage(), getResources().getColor(R.color.sign_up_password_text_color));

            }
        }
    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public void onStart() {
        super.onStart();
        if (mMapView != null) mMapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mMapView != null) mMapView.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mMapView != null) mMapView.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mMapView != null) mMapView.onPause();
//        if (BaseFragment.myAlertDialog != null && BaseFragment.myAlertDialog.isShowing())
//            BaseFragment.myAlertDialog.dismiss();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mMapView != null) mMapView.onStop();
    }

}
