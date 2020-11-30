package com.example.mytravellerapp.dto;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class Person implements ClusterItem {
    public final String name;
    public final String profilePhoto;
    private final LatLng mPosition;
    private final String tourID;

    public Person(LatLng position, String name, String pictureResource, String tourID) {
        this.name = name;
        profilePhoto = pictureResource;
        mPosition = position;
        this.tourID = tourID;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public String getSnippet() {
        return null;
    }


    public String getTourID() {
        return tourID;
    }
}
