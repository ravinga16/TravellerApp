package com.example.mytravellerapp.model.entities.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class NotificationCountResponse extends BaseServerResponse {
    private String unRead_notification_count;
}
