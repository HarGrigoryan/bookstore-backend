package com.example.bookstore.security.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder(setterPrefix = "with")
public class LoginResponseDTO {

    private Long userId;
    private String username;
    private String accessToken;
    private String refreshToken;
}
