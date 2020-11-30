package com.example.mytravellerapp.model.entities.response;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class ValidateResetCodeResponse extends BaseServerResponse {
    private String resetToken;
}
