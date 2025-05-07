package com.example.bookstore.service.dto;

import com.example.bookstore.persistance.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegistrationResponseDTO {

    private String accessToken;
    private String refreshToken;
    private Long id;
    private String firstname;
    private String lastname;
    private String email;
    private Long createdAt;
    private Long updatedAt;

    public static UserRegistrationResponseDTO toDTO(User user) {
        UserRegistrationResponseDTO dto = new UserRegistrationResponseDTO();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setFirstname(user.getFirstname());
        dto.setLastname(user.getLastname());
        dto.setCreatedAt(user.getCreatedAt().toEpochMilli());
        dto.setUpdatedAt(user.getUpdatedAt().toEpochMilli());
        return dto;
    }

}
