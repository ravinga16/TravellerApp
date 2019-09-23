package com.example.mytravellerapp.model.entities.response;



import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class LoginResponse extends BaseServerResponse {

    private String token;
    private String userId;
    private String email;
}
