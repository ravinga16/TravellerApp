package com.example.mytravellerapp.model.entities.response;



import com.example.mytravellerapp.dto.UserProfile;



import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class ProfileResponse extends BaseServerResponse {
        public UserProfile user;
}
