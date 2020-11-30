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
import com.example.mytravellerapp.model.rest.BMSService;


import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import okhttp3.MultipartBody;

public class ToursServiceImpl implements ToursService {

    private BMSService bmsService;

    public ToursServiceImpl(BMSService bmsService) {
        super();
        this.bmsService = bmsService;
    }

    @Override
    public Single<HomeInfoResponse> getHomeInfoService(String token, int count, int page) {
        return bmsService.getApi()
                .getHomeInfoAPI(token, 12, 1)
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Single<DoLikeResponse> doLikeService(String accessToken, LikeRequest likeRequest) {
        String contentType="application/json";
        return bmsService.getApi()
                .doLikeAPI(contentType,accessToken, likeRequest)
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Single<TourInfoResponse> getTourInfoService(String token, String tourLocationID) {
        return bmsService.getApi()
                .getTourInfoAPI(token,tourLocationID)
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Single<GetTourGalleryResponse> getTourGalleryService(String accessToken) {
        return bmsService.getApi()
                .getTourGalleryAPI(accessToken)
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Single<GetPackageResponse> getPackagesService(String accessToken) {
        return bmsService.getApi()
                .getPackagesAPI(accessToken)
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Single<UploadTourImagesResponse> uploadTourImagesService(String accessToken, MultipartBody.Part[] surveyImage, String tourID) {
        return bmsService.getApi()
                .uploadTourImagesAPI(accessToken, surveyImage, tourID)
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Single<AddInquireResponse> doInquireService(String accessToken, InquireRequest inquireRequest) {
        return bmsService.getApi()
                .doInquireAPI(accessToken, inquireRequest)
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Single<TourMapDetailsResponse> getToursMapDetailsService(String accessToken) {
        return bmsService.getApi()
                .getToursMapDetailsAPI(accessToken)
                .observeOn(AndroidSchedulers.mainThread());
    }



}
