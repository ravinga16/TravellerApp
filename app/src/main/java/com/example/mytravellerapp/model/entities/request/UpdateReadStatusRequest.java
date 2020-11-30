package com.example.mytravellerapp.model.entities.request;



import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class UpdateReadStatusRequest {
    private List<String> mynotificationId;
    private boolean isread;
}
