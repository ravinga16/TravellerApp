package com.example.mytravellerapp.model.entities.response;


import com.example.mytravellerapp.dto.GalleryGroupItem;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class GetTourGalleryResponse extends BaseServerResponse {
    private List<GalleryGroupItem> tours;
    private int count;
}
