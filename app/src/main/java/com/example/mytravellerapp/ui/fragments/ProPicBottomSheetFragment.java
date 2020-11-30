package com.example.mytravellerapp.ui.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.mytravellerapp.R;
import com.example.mytravellerapp.common.CommonUtils;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pub.devrel.easypermissions.EasyPermissions;


public class ProPicBottomSheetFragment extends BottomSheetDialogFragment {

    private static final String TAG = "ProPicBottomSheetFragme";
    private LinearLayout cameraLayout;
    private LinearLayout galleryLayout;
    private LinearLayout cancelLayout;
    private LinearLayout bottomSheet;

    private CircleImageView selectedImageView;
    private Bitmap image;
    private static final int READ_REQUEST_CODE = 300;
    Integer REQUEST_CAMERA = 200, SELECT_FILE = 1;
    String imageEncoded;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.bottom_sheet, container, false);

        //Bind views
        cameraLayout = rootView.findViewById(R.id.cameraLayout);
        galleryLayout = rootView.findViewById(R.id.galleryLayout);
        cancelLayout = rootView.findViewById(R.id.cancelLayout);
        bottomSheet = rootView.findViewById(R.id.bottomSheet);
        //inflating the profile fragment
        View mCustomView = getActivity().getLayoutInflater().inflate(R.layout.fragment_profile, null);
        selectedImageView = mCustomView.findViewById(R.id.profile_image); //circle image
        image = ((BitmapDrawable) selectedImageView.getDrawable()).getBitmap();

        final String[] perms_camera = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        final String[] perms_gallery = {Manifest.permission.READ_EXTERNAL_STORAGE};

        galleryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (EasyPermissions.hasPermissions(getActivity(), perms_gallery)) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_FILE);
                } else {
                    EasyPermissions.requestPermissions(getActivity(), getString(R.string.read_file),
                            READ_REQUEST_CODE, perms_gallery);
                }
            }
        });

        //upload via camera
        cameraLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (EasyPermissions.hasPermissions(getActivity(), perms_camera)) {
                    Intent intent = new Intent();
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
                    intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);
                }else {
                    EasyPermissions.requestPermissions(getActivity(), getString(R.string.camera_permision_msg),
                            REQUEST_CAMERA, perms_camera);
                }
            }
        });
        cancelLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == SELECT_FILE && null != data) {
            if (data.getData() != null) {
                Uri selectedImageUri = null;
                InputStream imageStream = null;
                try {
                    selectedImageUri = data.getData();
                    imageStream = getActivity().getContentResolver().openInputStream(selectedImageUri);
                    imageEncoded = CommonUtils.getInstance().getPathFromUriGallery(getActivity(), selectedImageUri);
                    requestGalleryUploadSurvey(imageStream, selectedImageUri, imageEncoded);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }else if(resultCode == Activity.RESULT_OK && requestCode == REQUEST_CAMERA && null != data){
            if (data != null) {
                Bundle extras = data.getExtras();
                Bitmap image = (Bitmap) extras.get("data");
                Uri tempUri = getImageUri(getActivity(), image);

                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.JPEG, 50, bytes);

                imageEncoded = CommonUtils.getInstance().getPathFromUri(getActivity(), tempUri);
                requestCameraUploadSurvey(image, tempUri, imageEncoded);
            }
        }
        dismiss();
    }

    private void requestGalleryUploadSurvey(InputStream inputStream, Uri profileImgUri, String encodedImage) {
        MultipartBody.Part[] profileImage = new MultipartBody.Part[1];
        File file = new File(encodedImage);
        RequestBody surveyBody = RequestBody.create(MediaType.parse("image/*"), file);
        profileImage[0] = MultipartBody.Part.createFormData("", file.getName(), surveyBody);
        if (profileImage != null) {
            if (ProfileFragment.profileFragment != null)
                ProfileFragment.profileFragment.setGalleryProfileImage(inputStream, profileImgUri, profileImage);
        }
    }
    private void requestCameraUploadSurvey(Bitmap image, Uri tempUri, String encodedImage) {
        MultipartBody.Part[] profileImage = new MultipartBody.Part[1];
        File file = new File(encodedImage);
        RequestBody surveyBody = RequestBody.create(MediaType.parse("image/*"), file);
        profileImage[0] = MultipartBody.Part.createFormData("", file.getName(), surveyBody);
        if (profileImage != null) {
            if (ProfileFragment.profileFragment != null)
                ProfileFragment.profileFragment.setProfileImage(image, tempUri, profileImage);
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
}
