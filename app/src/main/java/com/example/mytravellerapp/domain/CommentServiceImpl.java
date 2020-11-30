package com.example.mytravellerapp.domain;


import com.example.mytravellerapp.model.entities.request.CommentRequest;
import com.example.mytravellerapp.model.entities.response.AddCommentResponse;
import com.example.mytravellerapp.model.entities.response.GetCommentsResponse;
import com.example.mytravellerapp.model.rest.BMSService;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;


public class CommentServiceImpl implements CommentService {

    private BMSService bmsService;

    public CommentServiceImpl(BMSService bmsService) {
        super();
        this.bmsService = bmsService;
    }


    @Override
    public Single<AddCommentResponse> doCommentService(String accessToken, CommentRequest commentRequest) {
        return bmsService.getApi()
                .doCommentAPI(accessToken, commentRequest)
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Single<GetCommentsResponse> getCommentsService(String accessToken, String tourId, int count, int page) {
        return bmsService.getApi()
                .getCommentsAPI(accessToken, tourId,count, page)
                .observeOn(AndroidSchedulers.mainThread());
    }
}
