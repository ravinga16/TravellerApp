package com.example.mytravellerapp.model.entities.response;

import com.example.mytravellerapp.dto.TourDetails;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class TourInfoResponse extends BaseServerResponse {
    private TourDetails tour;
}
