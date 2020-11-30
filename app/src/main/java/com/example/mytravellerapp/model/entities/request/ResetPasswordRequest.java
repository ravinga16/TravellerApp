package com.example.mytravellerapp.model.entities.request;



import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class ResetPasswordRequest {
    private String resetToken;
    private String password;
    private String confirmPassword;
}
