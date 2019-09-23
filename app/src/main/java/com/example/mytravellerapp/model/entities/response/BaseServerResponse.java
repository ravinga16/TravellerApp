package com.example.mytravellerapp.model.entities.response;


import com.example.mytravellerapp.dto.ModelStateClass;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class BaseServerResponse {
    private int status_code;
    private boolean success;
    private boolean isAPIError;
    public String message;
    public ModelStateClass ModelState;
    private boolean isTokenExpired;
}