package com.example.mytravellerapp.model.entities.response;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class DoLikeResponse extends BaseServerResponse {
    private List<String> likes = null;
    private  int likeCount;
    private boolean isLike;
}
