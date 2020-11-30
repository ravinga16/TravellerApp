package com.example.mytravellerapp.dto;



import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class NotificationListItem {
    private String _id;
    private String iconImage;
    private String image;
    private String date;
    private String creatorId;
    private String tourId;
    private String action;
    private String androidContent;
    private String mynotificationId;
    private boolean isread;
}
