package com.example.mytravellerapp.model.entities.response;


import com.example.mytravellerapp.dto.CommentsList;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter


public class GetCommentsResponse extends BaseServerResponse {
    private CommentsList comments;
}
