package com.example.mytravellerapp.ui.fragments;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mytravellerapp.BaseApplication;
import com.example.mytravellerapp.R;
import com.example.mytravellerapp.common.CommonUtils;
import com.example.mytravellerapp.common.constants.ApplicationConstants;
import com.example.mytravellerapp.common.constants.DomainConstants;
import com.example.mytravellerapp.domain.UserService;
import com.example.mytravellerapp.domain.UserServiceImpl;
import com.example.mytravellerapp.dto.UserProfile;
import com.example.mytravellerapp.model.entities.request.UpdateMyDetailRequest;
import com.example.mytravellerapp.model.entities.response.BaseServerResponse;
import com.example.mytravellerapp.model.entities.response.LogOutResponse;
import com.example.mytravellerapp.model.entities.response.LoginResponse;
import com.example.mytravellerapp.model.entities.response.ProfileResponse;
import com.example.mytravellerapp.model.entities.response.ProfileUpdateResponse;
import com.example.mytravellerapp.model.entities.response.RegisterFcmResponse;
import com.example.mytravellerapp.model.entities.response.RegisterResponse;
import com.example.mytravellerapp.model.entities.response.UploadProfileImageResponse;
import com.example.mytravellerapp.model.entities.response.ValidateResetCodeResponse;
import com.example.mytravellerapp.model.rest.BMSService;
import com.example.mytravellerapp.mvp.presenters.Presenter;
import com.example.mytravellerapp.mvp.presenters.UserPresenter;
import com.example.mytravellerapp.mvp.presenters.UserPresenterImpl;
import com.example.mytravellerapp.mvp.views.UserView;
import com.example.mytravellerapp.ui.activities.BaseActivity;
import com.example.mytravellerapp.utils.AppScheduler;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.squareup.picasso.Picasso;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MultipartBody;

public class ProfileFragment extends BaseFragment implements UserView {
    private static final String TAG = "ProfileFragment";
    public static ProfileFragment profileFragment;
    private static Presenter presenter;
    private String userId;
    private String profileImageUrl = null;
    private ImageLoader imageLoader;
    private ProfileResponse profileResponseObject;
    private InputStream mImageStream;
    private String imageURI;
    private Bitmap mProfImage;

    private ImageView editProfile;
    private CircleImageView proPic;
    private TextView profileName;
    private TextView profileContactNo;
    private TextView profileEmail;
    private LinearLayout profileLayout;
    private LinearLayout editProfileLayout;
    private EditText firstName;
    private EditText lastName;
    private EditText contactNo;
    private TextView buttonSignOut;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    public ProfileFragment() {
        this.imageLoader = ImageLoader.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        editProfile =  rootView.findViewById(R.id.btn_edit);
        proPic = rootView.findViewById(R.id.profile_image);
        profileLayout = rootView.findViewById(R.id.profile);
        editProfileLayout = rootView.findViewById(R.id.edit_profile);
        profileName = rootView.findViewById(R.id.profile_name);
        profileContactNo = rootView.findViewById(R.id.profile_contact_no);
        profileEmail = rootView.findViewById(R.id.profile_email);
        firstName = rootView.findViewById(R.id.first_name);
        lastName = rootView.findViewById(R.id.last_name);
        contactNo = rootView.findViewById(R.id.contact_no);
        buttonSignOut = rootView.findViewById(R.id.btn_signout);

        profileFragment = this;
        return rootView;
    }


    @Override
    public void initializePresenter() {
        UserService mUserService = new UserServiceImpl(new BMSService());
        presenter = new UserPresenterImpl(getActivity(), mUserService, new AppScheduler());
        presenter.attachView(ProfileFragment.this);
        presenter.onCreate();
    }

    @Override
    protected void setUpToolBar() {
        View mCustomView = getActivity().getLayoutInflater().inflate(R.layout.custom_actionbar_profile, null);
        mToolBar.addView(mCustomView);
        Toolbar parent = (Toolbar) mCustomView.getParent();
        parent.setPadding(0, 0, 0, 0);//for tab otherwise give space in tab
        parent.setContentInsetsAbsolute(0, 0);
    }

    @Override
    protected void setUpUI() {
        performGetProfileDetails();
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editProfile.getTag() == null || editProfile.getTag() == getActivity().getResources().getString(R.string.btn_edit_stat_unselected)) {
                    editProfile.setImageResource(R.drawable.ic_save_blue_24dp);
                    editProfile.setTag(getActivity().getResources().getString(R.string.btn_edit_stat_selected));
                    changeEditButtonStatus(); //visiblity handle in layouts
                    setUserDetails(profileResponseObject, true);
                } else {
                    saveUserDetailAlert();
                }
            }
        });
        proPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProPicBottomSheetFragment bottomSheetFragment = new ProPicBottomSheetFragment();
                bottomSheetFragment.show(getFragmentManager(), bottomSheetFragment.getTag());
            }
        });
        buttonSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog(true, null, getString(R.string.logout_message),
                        getString(R.string.logout), getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                performGetLogout();

                            }
                        }, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
            }
        });
    }

    private void setUserDetails(ProfileResponse profileResponse, boolean isEdit) {
        if (profileResponse != null) {
            String imgUrl = DomainConstants.PROFILE_IMAGE_URL + profileResponse.getUser().getProfileImage();
            this.profileImageUrl = imgUrl;
//            Picasso.with(getContext()).load(imgUrl).resize(400, 300)
//                    .centerCrop().into(proPic);
            imageLoader.displayImage(imgUrl, proPic);
//            Picasso.with(getContext()).load(imgUrl).into(proPic);
        }
        if (!isEdit) {
            profileName.setText(profileResponse.getUser().getFName() + "" + profileResponse.getUser().getLName());
            profileContactNo.setText("" + profileResponse.getUser().getMobileNO() + "");
            profileEmail.setText("" + profileResponse.getUser().getEmail() + "");
        } else {
            if (profileResponse.getUser().getFName() != null && !profileResponse.getUser().getFName().isEmpty())
                firstName.setText(new StringBuilder().append(profileResponse.getUser().getFName()));
            if (profileResponse.getUser().getLName() != null && !profileResponse.getUser().getLName().isEmpty())
                lastName.setText(new StringBuilder().append(profileResponse.getUser().getLName()));
            if (profileResponse.getUser().getMobileNO() != null && !profileResponse.getUser().getMobileNO().isEmpty())
                contactNo.setText(profileResponse.getUser().getMobileNO());
        }

    }

    public void saveUserDetailAlert() {
        showAlertDialog(false, ApplicationConstants.SAVE_MY_DETAILS, ApplicationConstants.SAVE_MY_DETAILS_MESSAGE,
                getActivity().getResources().getString(R.string.save), getActivity().getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        performUpdateProfileDetails(firstName.getText().toString(), lastName.getText().toString(), contactNo.getText().toString());
                        editProfile.setTag(getActivity().getResources().getString(R.string.btn_edit_stat_unselected));
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        editProfile.setTag(getActivity().getResources().getString(R.string.btn_edit_stat_unselected));
                        changeEditButtonStatus();
                        setUserDetails(profileResponseObject, false);
                    }
                });
    }

    /*edit profile layout visibility change*/
    private void changeEditButtonStatus() {
        if (editProfile.getTag() == getActivity().getResources().getString(R.string.btn_edit_stat_selected)) {
            editProfile.setImageResource(R.drawable.ic_save_blue_24dp);
            profileLayout.setVisibility(View.GONE);
            editProfileLayout.setVisibility(View.VISIBLE);
        } else {
            if (editProfile.getTag() == getActivity().getResources().getString(R.string.btn_edit_stat_unselected)) {
                editProfile.setImageResource(R.drawable.ic_edit_blue_24dp);
                editProfileLayout.setVisibility(View.GONE);
                profileLayout.setVisibility(View.VISIBLE);
            }
        }
    }

    //upload profile image via gallery
    public void setGalleryProfileImage(InputStream imageStream, Uri selectedImageUri, MultipartBody.Part[] profileImage) {
        mImageStream = imageStream;
        imageURI = selectedImageUri.toString();
        performGetUploadProfileImage(profileImage);
    }

    //upload via camera
    public void setProfileImage(Bitmap image, Uri selectedImageUri, MultipartBody.Part[] profileImage) {
        mProfImage = image;
        imageURI = selectedImageUri.toString();
        performGetUploadProfileImage(profileImage);
    }
    public void performGetProfileDetails() {
        if (CommonUtils.getInstance().isNetworkConnected()) {
            ((UserPresenter) presenter).getProfileDetails(userId);
        } else {
//            showAlertDialog(false, ApplicationConstants.WARNING,
//                    ApplicationConstants.ERROR_MSG_CONNECTION_LOST, null);
        }
    }

    public void performUpdateProfileDetails(String fName, String lName, String contactNumber) {
        if (CommonUtils.getInstance().isNetworkConnected()) {
            if (firstName.getText().toString().isEmpty()) {
                showTopSnackBar(getString(R.string.first_name_required), getResources().getColor(R.color.sign_up_password_text_color));
            } else if (lastName.getText().toString().isEmpty()) {
                showTopSnackBar(getString(R.string.last_name_required), getResources().getColor(R.color.sign_up_password_text_color));
            } else if (contactNo.getText().toString().isEmpty()) {
                showTopSnackBar(getString(R.string.phn_num_required), getResources().getColor(R.color.sign_up_password_text_color));
            } else {
                setProgressDialog(true);
                UpdateMyDetailRequest updateMyDetailRequest = new UpdateMyDetailRequest();
                UserProfile jsonObject = profileResponseObject.getUser();
                if (jsonObject.getFName() != null && jsonObject.getLName() != null && jsonObject.getMobileNO() != null) {
                    if (!profileResponseObject.getUser().getFName().equals(fName))
                        updateMyDetailRequest.setFName(fName);
                    if (!profileResponseObject.getUser().getLName().equals(lName))
                        updateMyDetailRequest.setLName(lName);
                    if (!profileResponseObject.getUser().getMobileNO().equals(contactNo))
                        updateMyDetailRequest.setMobileNO(contactNumber);
                } else {
                    updateMyDetailRequest.setFName(fName);
                    updateMyDetailRequest.setLName(lName);
                    updateMyDetailRequest.setMobileNO(contactNumber);
                }
                ((UserPresenter) presenter).doUpdateProfile(updateMyDetailRequest, userId);
            }
        } else {
        }
    }

    public void performGetLogout() {
        if (CommonUtils.getInstance().isNetworkConnected()) {
            ((UserPresenter) presenter).doLogOut();
            setProgressDialog(true);
        } else {
            showAlertDialog(false, ApplicationConstants.WARNING,
                    ApplicationConstants.ERROR_MSG_CONNECTION_LOST, null);
        }
    }

    public void performGetUploadProfileImage(MultipartBody.Part[] profileImage) {
        if (CommonUtils.getInstance().isNetworkConnected()) {
            setProgressDialog(true);
            ((UserPresenter) presenter).doUploadProfileImage(profileImage, userId);
        } else {
            showAlertDialog(false, ApplicationConstants.WARNING,
                    ApplicationConstants.ERROR_MSG_CONNECTION_LOST, null);
        }
    }

    @Override
    public void showProfileResponse(ProfileResponse profileResponse) {
        if (profileResponse.isSuccess()) {
            setUserDetails(profileResponse, false);
            profileResponseObject = profileResponse;
        } else {
            if (profileResponse.isTokenExpired()) {
                BaseApplication.getBaseApplication().exTokenClearData(getActivity());
                return;
            }
        }
    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public void showLoginResponse(LoginResponse loginResponse) {

    }

    @Override
    public void showUpdateProfileResponse(ProfileUpdateResponse profileUpdateResponse) {
        setProgressDialog(false);
        changeEditButtonStatus();
        performGetProfileDetails();
    }

    @Override
    public void showLogOutResponse(LogOutResponse logOutResponse) {
        setProgressDialog(false);
        if (logOutResponse.isSuccess()) {
            BaseApplication.getBaseApplication().logOut(getContext());
        }else {
            if (logOutResponse.isAPIError() && logOutResponse.getMessage() != null) {
                showTopSnackBar(logOutResponse.getMessage(), getResources().getColor(R.color.sign_up_password_text_color));
            } else {
                showTopSnackBar(logOutResponse.getMessage(), getResources().getColor(R.color.sign_up_password_text_color));
            }
        }
    }

    @Override
    public void showUploadProfileImageResponse(UploadProfileImageResponse uploadProfileImageResponse) {
        setProgressDialog(false);
        if(uploadProfileImageResponse.isSuccess()){
            if(uploadProfileImageResponse.getData() != null) profileResponseObject.getUser().setProfileImage(uploadProfileImageResponse.getData());
            if (mProfImage != null)  {
                proPic.setImageBitmap(mProfImage);
                mProfImage = null;
            }
            if (mImageStream != null) {
                proPic.setImageBitmap(BitmapFactory.decodeStream(mImageStream));
                mImageStream = null;
            }
        }else {
            mProfImage = null;
            mImageStream = null;
            if (uploadProfileImageResponse.isTokenExpired()) {
                BaseApplication.getBaseApplication().exTokenClearData(getActivity());
                return;
            } else if (uploadProfileImageResponse.isAPIError() && uploadProfileImageResponse.getMessage() != null) {
                showTopSnackBar(uploadProfileImageResponse.getMessage(), getResources().getColor(R.color.sign_up_password_text_color));
            } else {
                showTopSnackBar(uploadProfileImageResponse.getMessage(), getResources().getColor(R.color.sign_up_password_text_color));
            }
        }
    }

    @Override
    public void showRegisterFcmResponse(RegisterFcmResponse registerFcmResponse) {

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

}
