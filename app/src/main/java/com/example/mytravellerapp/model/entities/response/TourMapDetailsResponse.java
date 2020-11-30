package com.example.mytravellerapp.model.entities.response;

import com.example.mytravellerapp.dto.TourMapDetails;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class TourMapDetailsResponse extends BaseServerResponse{
    List<TourMapDetails> tours;
}
