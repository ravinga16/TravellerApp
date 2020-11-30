package com.example.mytravellerapp.ui.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mytravellerapp.BaseApplication;
import com.example.mytravellerapp.R;
import com.example.mytravellerapp.common.CommonUtils;
import com.example.mytravellerapp.common.constants.ApplicationConstants;
import com.example.mytravellerapp.domain.ToursService;
import com.example.mytravellerapp.domain.ToursServiceImpl;
import com.example.mytravellerapp.dto.Package;
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
import com.example.mytravellerapp.ui.adapters.GalleryAdapter;
import com.example.mytravellerapp.ui.fragments.GalleryFragment;
import com.example.mytravellerapp.utils.AppScheduler;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pub.devrel.easypermissions.EasyPermissions;

public class UploadPhotosActivity extends BaseActivity implements ToursView {

    private static final String TAG = "UploadPhotosActivity";
    int PICK_IMAGE_MULTIPLE = 1;
    private static final int READ_REQUEST_CODE = 300;
    private static final int REQUEST_CAMERA = 200;
    protected Presenter presenter;
    String imageEncoded;
    List<String> imagesEncodedList;
    private GalleryAdapter galleryAdapter;
    private List<Package> mPackageList = new ArrayList<>();
    private String mSelectedTourId;
    private ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
    private ProgressDialog progressDialog;

    private LinearLayout btnSelectGallery;
    private LinearLayout btnSelectCamera;
    private GridView gvGallery;
    private ImageView emptyImage;
    private Button btnUpload;
    private Spinner tourSpinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_photos);
        //Bind views
        tourSpinner = findViewById(R.id.sp_select_tour);
        btnSelectGallery = findViewById(R.id.gallery_container);
        btnSelectCamera = findViewById(R.id.camera_container);
        emptyImage = findViewById(R.id.imgNoMedia);
        gvGallery = findViewById(R.id.grid_view);
        btnUpload = findViewById(R.id.btn_upload);
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog(true, null, getString(R.string.image_upload_confirmation_message),
                        getString(R.string.upload), getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(imagesEncodedList != null) {
                                    requestUploadSurvey(imagesEncodedList);
                                } else  {
                                    showAlertDialog(ApplicationConstants.ERROR, getResources().getString(R.string.select_a_photo));
                                }
                            }
                        }, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
            }
        });
        initializePresenter();
        setUpToolBar();
        checkImageStatus(true);
        performGetPackageRequest();


        final String[] perms_camera = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        final String[] perms_gallery = {Manifest.permission.READ_EXTERNAL_STORAGE};

        btnSelectGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (EasyPermissions.hasPermissions(UploadPhotosActivity.this, perms_gallery)) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent,"Select Picture"), PICK_IMAGE_MULTIPLE);
                }else {
                    EasyPermissions.requestPermissions(UploadPhotosActivity.this, getString(R.string.read_file),
                            READ_REQUEST_CODE, perms_gallery);
                }
            }
        });

        btnSelectCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (EasyPermissions.hasPermissions(UploadPhotosActivity.this, perms_camera)) {
                    Intent intent = new Intent();
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
                    intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);
                }else {
                    EasyPermissions.requestPermissions(UploadPhotosActivity.this, getString(R.string.camera_permision_msg),
                            REQUEST_CAMERA, perms_camera);
                }
            }
        });

        //setting the selected tour name
        tourSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                mSelectedTourId = null;
                if (i > 0) {
                    mSelectedTourId = mPackageList.get(i-1).get_id();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Setting up the spinner
        List<String> toursList = new ArrayList<>();
        toursList.add("SELECT YOUR TOUR");
        setTourSpinner(toursList);
    }

    public void initializePresenter() {
        ToursService mToursService = new ToursServiceImpl(new BMSService());
        presenter = new ToursPresenterImpl(this, mToursService, new AppScheduler());
        presenter.attachView(UploadPhotosActivity.this);
        presenter.onCreate();
    }
    protected void setUpToolBar() {
        ActionBar mActionBar = UploadPhotosActivity.this.getSupportActionBar();
        View mCustomView = this.getLayoutInflater().inflate(R.layout.custom_actionbar_back_upload_photos, null);
        TextView title = (TextView)mCustomView.findViewById(R.id.title_text_back);
        title.setText("Upload photos");
        mCustomView.findViewById(R.id.imgVbackAr).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadPhotosActivity.this.finish();
            }
        });
        mActionBar.setCustomView(mCustomView);

        Toolbar parent = (Toolbar) mCustomView.getParent();
        parent.setPadding(0, 0, 0, 0);//for tab otherwise give space in tab
        parent.setContentInsetsAbsolute(0, 0);
    }
    private void checkImageStatus(boolean flag) {
        if (flag) {
            emptyImage.setVisibility(View.VISIBLE);
            gvGallery.setVisibility(View.GONE);
        } else {
            gvGallery.setVisibility(View.VISIBLE);
            emptyImage.setVisibility(View.GONE);
        }
    }

    private void setTourSpinner(List<String> toursList){
        ArrayAdapter<String> allTours = new ArrayAdapter<String>(this,
                R.layout.spinner_item, toursList) {
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setGravity(Gravity.CENTER);
                ((TextView) v).setTypeface(((TextView) v).getTypeface(), Typeface.BOLD_ITALIC);

                return v;
            }


            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                ((TextView) v).setGravity(Gravity.CENTER);
                ((TextView) v).setTypeface(((TextView) v).getTypeface(), Typeface.BOLD_ITALIC);
                return v;
            }
        };
        allTours.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tourSpinner.setAdapter(allTours);
    }

    private void setProgressDialog(boolean isLoading) {
        if (isLoading) {
            if (progressDialog != null){ progressDialog.show();}
            else progressDialog = ProgressDialog.show(UploadPhotosActivity.this,
                    ApplicationConstants.EMPTY_STRING, "loading", true);
        } else {
            if (progressDialog != null && progressDialog.isShowing()) progressDialog.dismiss();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            // When an Image is picked
            if (requestCode == PICK_IMAGE_MULTIPLE && resultCode == RESULT_OK && null != data) {
                // Get the Image from data
                String[] filePathColumn = { MediaStore.Images.Media.DATA };
                imagesEncodedList = new ArrayList<String>();
                if (data.getData() != null) {
                    checkImageStatus(false);

                    Uri selectedImage = data.getData();

                    String wholeID = DocumentsContract.getDocumentId(selectedImage);
                    String id = wholeID.split(":")[1];
                    String sel = MediaStore.Images.Media._ID + "=?";

                    String[] filePath = {MediaStore.Images.Media.DATA};
                    Cursor c = this.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, filePathColumn, sel, new String[]{id}, null);
                    c.moveToFirst();
                    int columnIndex = c.getColumnIndex(filePath[0]);
                    imageEncoded = c.getString(columnIndex);
                    imagesEncodedList.add(imageEncoded);
                    c.close();

//                        ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
                    mArrayUri.add(selectedImage);
                    galleryAdapter = new GalleryAdapter(getApplicationContext(), mArrayUri);
                    gvGallery.setAdapter(galleryAdapter);
                    gvGallery.setVerticalSpacing(gvGallery.getHorizontalSpacing());
                    ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) gvGallery
                            .getLayoutParams();
                    mlp.setMargins(0, gvGallery.getHorizontalSpacing(), 0, 0);

                } else {
                    if (data.getClipData() != null) {
                        checkImageStatus(false);
                        ClipData mClipData = data.getClipData();
//                            ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
                        for (int i = 0; i < mClipData.getItemCount(); i++) {

                            ClipData.Item item = mClipData.getItemAt(i);
                            Uri uri = item.getUri();

                            String wholeID = DocumentsContract.getDocumentId(uri);
                            String id = wholeID.split(":")[1];
                            String sel = MediaStore.Images.Media._ID + "=?";

                            String[] filePath = {MediaStore.Images.Media.DATA};
                            Cursor c = this.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, filePathColumn, sel, new String[]{id}, null);
                            c.moveToFirst();
                            int columnIndex = c.getColumnIndex(filePath[0]);
                            imageEncoded = c.getString(columnIndex);
                            imagesEncodedList.add(imageEncoded);
                            c.close();

                            mArrayUri.add(uri);

                            galleryAdapter = new GalleryAdapter(getApplicationContext(), mArrayUri);
                            gvGallery.setAdapter(galleryAdapter);
                            gvGallery.setVerticalSpacing(gvGallery.getHorizontalSpacing());
                            ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) gvGallery
                                    .getLayoutParams();
                            mlp.setMargins(0, gvGallery.getHorizontalSpacing(), 0, 0);

                        }
                        Log.v("LOG_TAG", "Selected Images" + mArrayUri.get(0).toString());

                    }
                }
                //upload from camera
            }else if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CAMERA && null != data) {
                imagesEncodedList = new ArrayList<String>();
                if (data != null) {
                    checkImageStatus(false);
//                    ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
                    Bundle extras = data.getExtras();
                    Bitmap image = (Bitmap) extras.get("data");
                    Uri tempUri = getImageUri(this, image);

                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    image.compress(Bitmap.CompressFormat.JPEG, 50, bytes);

                    imageEncoded = CommonUtils.getInstance().getPathFromUri(this, tempUri);
                    imagesEncodedList.add(imageEncoded);

                    mArrayUri.add(tempUri);

                    galleryAdapter = new GalleryAdapter(getApplicationContext(), mArrayUri);
                    gvGallery.setAdapter(galleryAdapter);
                    gvGallery.setVerticalSpacing(gvGallery.getHorizontalSpacing());
                    ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) gvGallery
                            .getLayoutParams();
                    mlp.setMargins(0, gvGallery.getHorizontalSpacing(), 0, 0);
                }
            } else {
                checkImageStatus(true);
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            checkImageStatus(true);
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private void requestUploadSurvey(List<String> mArrayUri) {
        MultipartBody.Part[] tourImages = new MultipartBody.Part[mArrayUri.size()];

        for (int index = 0; index < mArrayUri.size(); index++) {
            File file = new File(mArrayUri.get(index));
            RequestBody surveyBody = RequestBody.create(MediaType.parse("image/*"), file);
            tourImages[index] = MultipartBody.Part.createFormData("", file.getName(), surveyBody);
        }

        if(tourImages.length > 0) {
            validateFieldsForIPCam(tourImages);
        }
    }

    private void validateFieldsForIPCam(MultipartBody.Part[] tourImages) {
        if (tourImages == null || tourImages.length == 0 || mSelectedTourId == null ||  mSelectedTourId.isEmpty() ) {
            if (mSelectedTourId == null ||  mSelectedTourId.isEmpty() ) {
                showAlertDialog(ApplicationConstants.ERROR, getResources().getString(R.string.tour_id_is_required));
            } else  {
                showAlertDialog(ApplicationConstants.ERROR, getResources().getString(R.string.select_a_photo));
            }
        } else {
            performUploadGalleryRequest(tourImages, mSelectedTourId);
        }

    }

    //perform requests
    public void performGetPackageRequest() {
        if (CommonUtils.getInstance().isNetworkConnected()) {
            setProgressDialog(true);
            ((ToursPresenter) presenter).getPackages();
        } else {
            showAlertDialog(ApplicationConstants.WARNING, ApplicationConstants.ERROR_MSG_CONNECTION_LOST);
        }
    }

    public void performUploadGalleryRequest(MultipartBody.Part[] tourImages , String tourID) {
        if (CommonUtils.getInstance().isNetworkConnected()) {
            setProgressDialog(true);
            ((ToursPresenter) presenter).uploadTourImages(tourImages, tourID);
        } else {
            showAlertDialog(ApplicationConstants.WARNING, ApplicationConstants.ERROR_MSG_CONNECTION_LOST);
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
        setProgressDialog(false);
        if (getPackageResponse.isSuccess()){
            mPackageList = getPackageResponse.getTours();
            List<String> toursList = new ArrayList<>(); //list of tours available
            toursList.add("SELECT YOUR TOUR");
            for(int i = 0; i < getPackageResponse.getTours().size(); i++){
                toursList.add(getPackageResponse.getTours().get(i).getTourName());
            }
            setTourSpinner(toursList);
        } else {
            if (getPackageResponse.isTokenExpired()) {
                BaseApplication.getBaseApplication().exTokenClearData(this);
                return;
            } else if (getPackageResponse.isAPIError() && getPackageResponse.getMessage() != null) {
                showTopSnackBar(getPackageResponse.getMessage(), getResources().getColor(R.color.sign_up_password_text_color));
            } else {
                showTopSnackBar(getPackageResponse.getMessage(), getResources().getColor(R.color.sign_up_password_text_color));
            }
        }
    }

    @Override
    public void showUploadTourImagesResponse(UploadTourImagesResponse uploadTourImagesResponse) {
        setProgressDialog(false);
        if (uploadTourImagesResponse.isSuccess()) {
            showAlertDialog(false, ApplicationConstants.SUCCESS, getString(R.string.image_upload_success_message),
                    getString(R.string.ok), null, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mSelectedTourId = "";
                            imagesEncodedList.clear();
                            if(GalleryFragment.galleryFragment != null )GalleryFragment.galleryFragment.performInitialRequest();
                            finish();
                        }
                    }, null);
        }
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
