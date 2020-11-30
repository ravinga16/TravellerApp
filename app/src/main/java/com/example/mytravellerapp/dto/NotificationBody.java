package com.example.mytravellerapp.dto;



import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class NotificationBody implements Serializable {
    private String _id;
    private String creatorId;
    private String tourId;
    private String date;
    private String action;
    private String content;
    private String andoridContent;
    private String image;

}
