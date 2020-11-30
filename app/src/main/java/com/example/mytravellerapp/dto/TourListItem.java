package com.example.mytravellerapp.dto;



import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class TourListItem {
    private String _id;
    private String tourName;
    private String packagePrice;
    private String NumberOfDays;
    private int visitsCount;
    private int commentCount;
    private int numberOfPhotos;
    private String coverImage;
    private int likesCount;
    private boolean isLike;
}
