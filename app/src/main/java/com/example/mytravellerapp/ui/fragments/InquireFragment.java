package com.example.mytravellerapp.ui.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mytravellerapp.BaseApplication;
import com.example.mytravellerapp.R;
import com.example.mytravellerapp.common.CommonUtils;
import com.example.mytravellerapp.common.constants.ApplicationConstants;
import com.example.mytravellerapp.common.constants.DomainConstants;
import com.example.mytravellerapp.domain.ToursService;
import com.example.mytravellerapp.domain.ToursServiceImpl;
import com.example.mytravellerapp.model.entities.request.InquireRequest;
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
import com.example.mytravellerapp.utils.AppScheduler;
import com.google.android.gms.dynamic.IFragmentWrapper;
import com.google.android.gms.maps.internal.IMapViewDelegate;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.Calendar;

public class InquireFragment extends BaseFragment implements ToursView {

    private static final String TAG = "InquireFragment";
    //UI
    private Button btnSubmit;
    private ImageView coverImage;
    private LinearLayout dateContainer;
    private EditText mStartDate;
    private EditText paxNumber;
    private TextView textTourName;
    //other
    public static InquireFragment inquireFragment;
    private int mSelectedYear;
    private int mSelectedMonth;
    private int mSelectedDay;
    private TourInfoResponse mItem;

    public static InquireFragment newInstance(TourInfoResponse tourInfoResponse) {
        InquireFragment fragment = new InquireFragment();
        fragment.mItem = tourInfoResponse;
        return fragment;
    }

    public static String getTAG() {
        return TAG;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_inquire, container, false);
        //bind ui
        coverImage = rootView.findViewById(R.id.featured_image);
        dateContainer = rootView.findViewById(R.id.date_container);
        btnSubmit = rootView.findViewById(R.id.btn_submit);
        mStartDate = rootView.findViewById(R.id.date_picker);
        paxNumber = rootView.findViewById(R.id.pax_number);
        textTourName = rootView.findViewById(R.id.text_tour_name);
        return rootView;
    }

    @Override
    protected void setUpToolBar() {
        View mCustomView = getActivity().getLayoutInflater().inflate(R.layout.custom_actionbar_back, null);
        ImageView backbtn = mCustomView.findViewById(R.id.imgVbackAr);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });
        mToolBar.addView(mCustomView);
        Toolbar parent = (Toolbar) mCustomView.getParent();
        parent.setPadding(0, 0, 0, 0);//for tab otherwise give space in tab
        parent.setContentInsetsAbsolute(0, 0);
    }

    @Override
    protected void setUpUI() {
        if (mItem != null) {
            String coverImageUrl = DomainConstants.TOUR_IMAGE_URL + mItem.getTour().getCoverImage();
            Picasso.with(getContext()).load(coverImageUrl).into(coverImage);
            textTourName.setText(mItem.getTour().getTourName()+""+"\n"+mItem.getTour().getNumberOfDates()+" "+"Days");
        }
        //onClick listeners
        dateContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDatePicker();
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateFields();
            }
        });

    }

    @Override
    protected void initializePresenter() {
        ToursService mToursService = new ToursServiceImpl(new BMSService());
        presenter = new ToursPresenterImpl(getActivity(), mToursService, new AppScheduler());
        presenter.attachView(InquireFragment.this);
        presenter.onCreate();
    }

    private void setDatePicker() {
        Calendar mCurrentDate = Calendar.getInstance();
        int mYear = mCurrentDate.get(Calendar.YEAR);
        int mMonth = mCurrentDate.get(Calendar.MONTH);
        int mDay = mCurrentDate.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog mDatePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker datepicker, int selectedYear, int selectedMonth, int selectedDay) {
                mStartDate.setText(new DecimalFormat("00").format(selectedDay) + "-" + new DecimalFormat("00").format(selectedMonth + 1) + "-" + selectedYear);
                mSelectedYear = selectedYear;
                mSelectedMonth = selectedMonth;
                mSelectedDay = selectedDay;
            }
        }, mYear, mMonth, mDay);
        mDatePicker.getDatePicker().setMinDate(mCurrentDate.getTimeInMillis());
        String mybirthday = mStartDate.getText().toString().trim();

        if (mybirthday != null && !mybirthday.isEmpty())
            mDatePicker.updateDate(mSelectedYear, mSelectedMonth, mSelectedDay);
        mDatePicker.show();
    }

    private void validateFields(){
        String startDate = mStartDate.getText().toString().trim();
        String paxNumbers = paxNumber.getText().toString().trim();

        boolean isErrorOccurred = false;
        if (startDate.isEmpty() || paxNumbers.isEmpty() || paxNumbers.equals("0")) {
            if (startDate.isEmpty()) {
                showAlertDialog(true, ApplicationConstants.ERROR, getResources().getString(R.string.start_date_required), null);
                mStartDate.requestFocus();
            } else if (paxNumbers.isEmpty()) {
                showAlertDialog(true, ApplicationConstants.ERROR, getResources().getString(R.string.pax_is_required), null);
                paxNumber.requestFocus();
            } else if (paxNumbers.equals("0")) {
                showAlertDialog(true, ApplicationConstants.ERROR, getResources().getString(R.string.enter_valid_pax_count), null);
                paxNumber.requestFocus();
            }
        } else {
            if (!isErrorOccurred) {
                InquireRequest request = new InquireRequest();
                request.setTourId(mItem.getTour().get_id());
                request.setStartDate(startDate);
                request.setNoPax(paxNumbers);

                performInquireRequest(request);
            }
        }
    }

    public void performInquireRequest(InquireRequest inquireRequest) {
        if (CommonUtils.getInstance().isNetworkConnected()) {
            setProgressDialog(true);
            ((ToursPresenter) presenter).doInquire(inquireRequest);
        } else {
            showAlertDialog(false, ApplicationConstants.WARNING,
                    ApplicationConstants.ERROR_MSG_CONNECTION_LOST, null);
        }
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
        setProgressDialog(false);
        if (addInquireResponse.isSuccess()) {
            showTopSnackBar(addInquireResponse.getMessage(), getResources().getColor(R.color.green));
            getFragmentManager().popBackStack();
        } else {
            if (addInquireResponse.isTokenExpired()) {
                BaseApplication.getBaseApplication().exTokenClearData(getActivity());
                return;
            } else if (addInquireResponse.isAPIError() && addInquireResponse.getMessage() != null) {
                showTopSnackBar(addInquireResponse.getMessage(), getResources().getColor(R.color.sign_up_password_text_color));
            } else {
                showTopSnackBar(addInquireResponse.getMessage(), getResources().getColor(R.color.sign_up_password_text_color));
            }
        }
    }

    @Override
    public void showGetToursMapDetailsResponse(TourMapDetailsResponse tourMapDetailsResponse) {

    }

    @Override
    public void showMessage(String message) {

    }
}
