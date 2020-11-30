package com.example.mytravellerapp.model.entities.response;


import com.example.mytravellerapp.dto.Package;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class GetPackageResponse extends BaseServerResponse {
    private int count;
    private List<Package> tours;
}
