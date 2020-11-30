package com.example.mytravellerapp.dto;


import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class NotificationData {
    private String count;
    private List<NotificationListItem> notifications;
}
