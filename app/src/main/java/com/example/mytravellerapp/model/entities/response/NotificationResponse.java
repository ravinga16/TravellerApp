package com.example.mytravellerapp.model.entities.response;

import com.example.mytravellerapp.dto.NotificationData;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class NotificationResponse extends BaseServerResponse {
    private NotificationData data;
}
