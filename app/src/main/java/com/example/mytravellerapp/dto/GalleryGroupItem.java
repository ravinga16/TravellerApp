package com.example.mytravellerapp.dto;


import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class GalleryGroupItem {
    private boolean isExpanded = true;
    private String date;
    private ArrayList<String> imageGallery;
    private String tourName;
    private String _id;
}
