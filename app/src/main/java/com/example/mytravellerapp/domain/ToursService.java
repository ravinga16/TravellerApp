package com.example.mytravellerapp.domain;

import com.example.mytravellerapp.model.entities.request.InquireRequest;
import com.example.mytravellerapp.model.entities.request.LikeRequest;
import com.example.mytravellerapp.model.entities.response.AddInquireResponse;
import com.example.mytravellerapp.model.entities.response.DoLikeResponse;
import com.example.mytravellerapp.model.entities.response.GetPackageResponse;
import com.example.mytravellerapp.model.entities.response.GetTourGalleryResponse;
import com.example.mytravellerapp.model.entities.response.HomeInfoResponse;
import com.example.mytravellerapp.model.entities.response.TourInfoResponse;
import com.example.mytravellerapp.model.entities.response.TourMapDetailsResponse;
import com.example.mytravellerapp.model.entities.response.UploadTourImagesResponse;

import io.reactivex.Single;
import okhttp3.MultipartBody;

public interface ToursService extends Service {
    Single<HomeInfoResponse> getHomeInfoService(String token, int count, int page);
    Single<DoLikeResponse> doLikeService(String accessToken, LikeRequest likeRequest);
    Single<TourInfoResponse> getTourInfoService(String token, String tourId);
    Single<GetTourGalleryResponse> getTourGalleryService(String accessToken);
    Single<GetPackageResponse> getPackagesService(String accessToken);
    Single<UploadTourImagesResponse> uploadTourImagesService(String accessToken, MultipartBody.Part[] tourImages, String tourID);
    Single<AddInquireResponse> doInquireService(String accessToken, InquireRequest inquireRequest);
    Single<TourMapDetailsResponse> getToursMapDetailsService(String accessToken);


}
