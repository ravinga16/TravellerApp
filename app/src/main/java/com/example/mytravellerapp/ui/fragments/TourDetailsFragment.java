package com.example.mytravellerapp.ui.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.ToolbarWidgetWrapper;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.example.mytravellerapp.BaseApplication;
import com.example.mytravellerapp.R;
import com.example.mytravellerapp.common.CommonUtils;
import com.example.mytravellerapp.common.constants.ApplicationConstants;
import com.example.mytravellerapp.common.constants.DomainConstants;
import com.example.mytravellerapp.domain.ToursService;
import com.example.mytravellerapp.domain.ToursServiceImpl;
import com.example.mytravellerapp.dto.TourMapPings;
import com.example.mytravellerapp.dto.TourSchedule;
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
import com.example.mytravellerapp.mvp.presenters.ToursPresenter;
import com.example.mytravellerapp.mvp.presenters.ToursPresenterImpl;
import com.example.mytravellerapp.mvp.views.ToursView;
import com.example.mytravellerapp.ui.activities.MainActivity;
import com.example.mytravellerapp.ui.adapters.PackageInclusionAdapter;
import com.example.mytravellerapp.ui.adapters.TourDayDetailsRecycleAdapter;
import com.example.mytravellerapp.utils.AppScheduler;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import lombok.core.Main;


public class TourDetailsFragment extends BaseFragment implements ToursView {

    private static final String TAG = "TourDetailsFragment";
    private static View rootView;
    private TourInfoResponse tourInfo;
    private String imgUrl;
    private LikeRequest likeRequest = new LikeRequest();
    //layout parameters
    private ImageView featured_image;
    private TextView tour_name;
    private TextView no_ofVisits;
    private TextView no_ofLikes;
    private TextView no_ofComments;
    private ImageView btnLike;
    private LinearLayout commentContainer;
    private LinearLayout likesContainer;
    private PackageInclusionAdapter adapter;
    private TourDayDetailsRecycleAdapter adapterTourDayDetailsRecycleAdapter;
    private Toolbar mToolBar;
    private Button btnInquire;
    private ImageButton check_tour_day_detail;
    private static TourDetailsFragment tourDetailsFragment;

    public static String getTAG() {
        return TAG;
    }

    public TourDetailsFragment() {
        //empty constructor
    }

    public static TourDetailsFragment newInstance(TourInfoResponse tourInfoResponse) {
        TourDetailsFragment fragment = new TourDetailsFragment();
        fragment.tourInfo = tourInfoResponse;
//        Bundle args = new Bundle();
//        args.putParcelable(BUNDLE_EXTRA, Parcels.wrap(item));
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_tour_detail, container, false);
        tourDetailsFragment = this;
        featured_image = (ImageView) rootView.findViewById(R.id.featured_image);
        tour_name = (TextView) rootView.findViewById(R.id.tour_name);
        no_ofVisits = (TextView) rootView.findViewById(R.id.no_ofVisits);
        no_ofLikes = (TextView) rootView.findViewById(R.id.no_ofLikes);
        no_ofComments = (TextView) rootView.findViewById(R.id.no_ofComments);
        btnLike = (ImageView) rootView.findViewById(R.id.btn_like);
        commentContainer = (LinearLayout) rootView.findViewById(R.id.comments_container);
        likesContainer = (LinearLayout) rootView.findViewById(R.id.likes_container);
        btnInquire = rootView.findViewById(R.id.btn_inquire);
        check_tour_day_detail = rootView.findViewById(R.id.check_tour_day_detail);
        setData();
        setMapData();
        initiateRecycleViewOne(tourInfo.getTour().getPackageInclutions()); //under popular landmarks
        //initiateRecycleViewTwo(tourInfo.getTour().getSchedule()); //Day 1,2,3... details

        return rootView;
    }

    @Override
    public void initializePresenter() {
        ToursService mToursService = new ToursServiceImpl(new BMSService());
        presenter = new ToursPresenterImpl(getActivity(), mToursService, new AppScheduler());
        presenter.attachView(TourDetailsFragment.this);
        presenter.onCreate();
    }


    @Override
    public void setUpToolBar() {
        mToolBar = rootView.findViewById(R.id.toolbar);
        View mCustomView = getActivity().getLayoutInflater().inflate(R.layout.custom_actionbar_back, null);
        ImageView btnBack = mCustomView.findViewById(R.id.imgVbackAr);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
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
        commentContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).setFragment(new CommentFragment().newInstance(tourInfo.getTour().get_id(), imgUrl, tourInfo.getTour().getCommentCount().intValue()), CommentFragment.getTAG());
            }
        });
        btnInquire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //load the inquire UI
                ((MainActivity) getContext()).setFragment(new InquireFragment().newInstance(tourInfo), InquireFragment.getTAG());

            }
        });
        likesContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performLike();
            }
        });
        check_tour_day_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getContext()).loadTourDayDetailSlider((ArrayList<TourSchedule>) tourInfo.getTour().getSchedule());
            }
        });
    }


    private void setMapData() {
        final List<TourMapPings> mapPings = tourInfo.getTour().getMapPings();
        /*Map initiation*/
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.frg);  //use SuppoprtMapFragment for using in fragment instead of activity  MapFragment = activity   SupportMapFragment = fragment
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                mMap = mMap;

                int i;
                for (i = 0; i < mapPings.size(); i++) {
                    LatLng tour_position = new LatLng(Double.parseDouble(mapPings.get(i).getLat().toString()), Double.parseDouble(mapPings.get(i).getLng().toString()));
                    mMap.addMarker(new MarkerOptions().position(tour_position).title(mapPings.get(i).getName()));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(tour_position, 9F));
                }

            }
        });
    }

    public void setData() {
        final String imageUrl = DomainConstants.TOUR_IMAGE_URL + tourInfo.getTour().getCoverImage();
        this.imgUrl = imageUrl; //to send in comment fragment
        Log.d(TAG, "setData: ===============>>>>>>>>>>>>>>");
        System.out.println(imageUrl);
        Picasso.with(getContext()).load(imageUrl).resize(500, 400)
                .centerCrop().into(featured_image);
        featured_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> imageList = new ArrayList<>();
                for (String image : tourInfo.getTour().getImageGallery()) {

                    if (DomainConstants.TOUR_IMAGE_URL + image != null) {
                        {
                            imageList.add(DomainConstants.TOUR_IMAGE_URL + image);
                        }
                    } else {
                        imageList.add(DomainConstants.TOUR_IMAGE_URL + tourInfo.getTour().getCoverImage());
                        Toast.makeText(getActivity(), "Image Not Found", Toast.LENGTH_SHORT).show();
                    }
                }
                goToFullScreen(imageList, 0);
            }
        });
        tour_name.setText(tourInfo.getTour().getTourName());
        no_ofVisits.setText("" + tourInfo.getTour().getVisitsCount() + "");
        no_ofLikes.setText("" + tourInfo.getTour().getLikesCount() + "");
        no_ofComments.setText("" + tourInfo.getTour().getCommentCount() + "");
        if (tourInfo.getTour().isLike()) {
            btnLike.setImageResource(R.drawable.ic_thumb_on);
            no_ofLikes.setTextColor(getContext().getResources().getColor(R.color.blue));
        } else {
            btnLike.setImageResource(R.drawable.ic_thumb_off);
            no_ofLikes.setTextColor(getContext().getResources().getColor(R.color.white));
        }
    }

    private void goToFullScreen(ArrayList<String> galleryContents, int position) {
        ((MainActivity) rootView.getContext()).loadScreenSlider(galleryContents, 0);
    }


    public void initiateRecycleViewOne(List<String> packageInclusionList) {
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.package_inclusion_container);
        adapter = new PackageInclusionAdapter(packageInclusionList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
    }

    public void initiateRecycleViewTwo(List<TourSchedule> tourSchedules) {
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.day_detail_container);
        adapterTourDayDetailsRecycleAdapter = new TourDayDetailsRecycleAdapter(tourSchedules, rootView.getContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapterTourDayDetailsRecycleAdapter);
    }

    public void performLike() {
        likeRequest.setTourId(tourInfo.getTour().get_id());
        if (CommonUtils.getInstance().isNetworkConnected()) {
            // if(!isPullToRefreshCall)setProgressDialog(true);
            ((ToursPresenter) presenter).doLike(likeRequest);
        } else {
            showAlertDialog(false, ApplicationConstants.WARNING,
                    ApplicationConstants.ERROR_MSG_CONNECTION_LOST, null);
        }
    }


    @Override
    public void showDoLikeResponse(DoLikeResponse doLikeResponse) {
        if (doLikeResponse.isSuccess()) {
            if(HomeFragment.homeFragment != null){
                HomeFragment.homeFragment.updateLikeCount(tourInfo.getTour().get_id(), doLikeResponse.isLike());
            }
            if ((!isAdded() || !isVisible())) return;
            no_ofLikes.setText(doLikeResponse.getLikeCount() + "");

            if(doLikeResponse.isLike()) {
                btnLike.setImageResource(R.drawable.ic_thumb_on);
                no_ofLikes.setTextColor(this.getResources().getColor(R.color.blue));
            }
            else {
                btnLike.setImageResource(R.drawable.ic_thumb_off);
                no_ofLikes.setTextColor(this.getResources().getColor(R.color.white));
            }
        } else {
            if (doLikeResponse.isTokenExpired()) {
                BaseApplication.getBaseApplication().exTokenClearData(getActivity());
                return;
            } else if (doLikeResponse.isAPIError() && doLikeResponse.getMessage() != null) {
                showTopSnackBar(doLikeResponse.getMessage(), getResources().getColor(R.color.sign_up_password_text_color));
            } else {
                showTopSnackBar(doLikeResponse.getMessage(), getResources().getColor(R.color.sign_up_password_text_color));
            }
        }
    }

    @Override
    public void showHomeInfoResponse(HomeInfoResponse homeInfoResponse) {

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
    public void showMessage(String message) {

    }
}
