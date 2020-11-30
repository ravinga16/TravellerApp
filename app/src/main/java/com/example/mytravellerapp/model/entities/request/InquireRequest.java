package com.example.mytravellerapp.model.entities.request;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class InquireRequest {
    private String tourId;
    private String startDate;
    private String noPax;
}
