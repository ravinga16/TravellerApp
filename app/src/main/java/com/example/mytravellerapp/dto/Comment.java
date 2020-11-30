package com.example.mytravellerapp.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class Comment {
    private String _id;
    private String date;
    private String description;
    private String reply;
    private UserProfile user;
}
