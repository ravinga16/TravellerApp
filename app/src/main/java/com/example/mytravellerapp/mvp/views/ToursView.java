package com.example.mytravellerapp.mvp.views;

import com.example.mytravellerapp.model.entities.response.AddInquireResponse;
import com.example.mytravellerapp.model.entities.response.DoLikeResponse;
import com.example.mytravellerapp.model.entities.response.GetPackageResponse;
import com.example.mytravellerapp.model.entities.response.GetTourGalleryResponse;
import com.example.mytravellerapp.model.entities.response.HomeInfoResponse;
import com.example.mytravellerapp.model.entities.response.TourInfoResponse;
import com.example.mytravellerapp.model.entities.response.TourMapDetailsResponse;
import com.example.mytravellerapp.model.entities.response.UploadTourImagesResponse;

public interface ToursView extends View {
    void showHomeInfoResponse(HomeInfoResponse homeInfoResponse);
    void showDoLikeResponse(DoLikeResponse doLikeResponse);
    void showTourInfoResponse(TourInfoResponse TourInfoResponse);
    void showGetTourGalleryResponse(GetTourGalleryResponse getTourGalleryResponse);
    void showGetPackageResponse(GetPackageResponse getPackageResponse);
    void showUploadTourImagesResponse(UploadTourImagesResponse uploadTourImagesResponse);
    void showInquireResponse(AddInquireResponse addInquireResponse);
    void showGetToursMapDetailsResponse(TourMapDetailsResponse tourMapDetailsResponse);


}
