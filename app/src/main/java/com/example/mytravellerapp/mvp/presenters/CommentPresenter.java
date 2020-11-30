package com.example.mytravellerapp.mvp.presenters;


import com.example.mytravellerapp.model.entities.request.CommentRequest;

public interface CommentPresenter extends Presenter {

    void getComments(String tourId, int count, int page);
    void doComment(CommentRequest commentRequest);
}
