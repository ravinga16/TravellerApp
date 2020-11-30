package com.example.mytravellerapp.model.entities.request;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class CommentRequest {
    private String tourId;
    private String description;
}
