package com.example.mytravellerapp.model.entities.response;

import com.example.mytravellerapp.dto.TourListItem;


import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class HomeInfoResponse extends BaseServerResponse {
    private ArrayList<TourListItem> tours;
}
