package com.example.mytravellerapp.mvp.views;


import com.example.mytravellerapp.model.entities.response.AddCommentResponse;
import com.example.mytravellerapp.model.entities.response.AddReplyResponse;
import com.example.mytravellerapp.model.entities.response.GetCommentsResponse;

public interface CommentView extends View {
    void showAddCommentResponse(AddCommentResponse addCommentResponse);
    void showAddReplyResponse(AddReplyResponse addReplyResponse);
    void showGetCommentsResponse(GetCommentsResponse getCommentsResponse);
}
