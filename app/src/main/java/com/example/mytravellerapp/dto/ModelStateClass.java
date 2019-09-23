package com.example.mytravellerapp.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ModelStateClass {
    private List<String> info;

    @SerializedName("model.ConfirmPassword")
    private List<String> modelConfirmPassword;
}
