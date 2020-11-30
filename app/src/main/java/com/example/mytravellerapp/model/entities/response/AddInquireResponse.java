package com.example.mytravellerapp.model.entities.response;
import com.example.mytravellerapp.dto.Inquire;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class AddInquireResponse extends BaseServerResponse {
    private Inquire inquire;
}
