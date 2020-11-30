package com.example.mytravellerapp.dto;



import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class TourDetails {
    private String _id;
    private String tourName;
    private String packagePrice;
    private String numberOfDates;
    private Number visitsCount;
    private Number commentCount;
    private Number likesCount;
    private Number mainLat;
    private Number mainLong;
    private String mapIconImage;
    private String coverImage;
    private List<TourMapPings> mapPings;
    private List<String> imageGallery;
    private List<String> packageInclutions;
    private List<TourSchedule> schedule;
    private boolean isLike;
}
