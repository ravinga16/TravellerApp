package com.example.mytravellerapp.dto;


import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class CommentsList {
    private String count;
    private ArrayList<Comment> data;
}
