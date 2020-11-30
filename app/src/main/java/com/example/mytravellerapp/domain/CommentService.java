package com.example.mytravellerapp.domain;


import com.example.mytravellerapp.model.entities.request.CommentRequest;
import com.example.mytravellerapp.model.entities.response.AddCommentResponse;
import com.example.mytravellerapp.model.entities.response.GetCommentsResponse;

import io.reactivex.Single;


public interface CommentService extends Service {
    Single<AddCommentResponse> doCommentService(String accessToken, CommentRequest commentRequest);
    Single<GetCommentsResponse> getCommentsService(String accessToken, String tourId, int count, int page);
}
