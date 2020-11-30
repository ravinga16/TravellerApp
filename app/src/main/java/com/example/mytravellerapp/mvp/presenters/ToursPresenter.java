package com.example.mytravellerapp.mvp.presenters;



import com.example.mytravellerapp.model.entities.request.InquireRequest;
import com.example.mytravellerapp.model.entities.request.LikeRequest;

import okhttp3.MultipartBody;

public interface ToursPresenter extends Presenter {
    void getHomeInfo(int count, int page);
    void doLike(LikeRequest likeRequest);
    void getTourInfo(String tourId);
    String getId();
    void getTourGallery();
    void getPackages();
    void uploadTourImages(MultipartBody.Part[] tourImages, String tourID);
    void doInquire(InquireRequest inquireRequest);
    void getTourMapDetails();

}
