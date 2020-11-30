package com.example.mytravellerapp.dto;



import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter


public class TourSchedule {
    private String scheduleName;
    private String day_image;
    private String description;
    private String startLocation;
    private String endLocation;
    private String travellingTime;
    private String hotelName;
    private List<String> otherVisitingPlaces;
}
