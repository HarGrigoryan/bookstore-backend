package com.example.bookstore.service.dto;

import com.example.bookstore.persistance.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {

    private Long id;
    private String firstname;
    private String lastname;
    private String email;
    private Long createdAt;
    private Long updatedAt;

    public static UserDTO toDTO(User user) {
        final UserDTO userDTO = new UserDTO();

        userDTO.setId(user.getId());
        userDTO.setFirstname(user.getFirstname());
        userDTO.setLastname(user.getLastname());
        userDTO.setEmail(user.getEmail());
        userDTO.setCreatedAt(user.getCreatedAt().toEpochMilli());
        userDTO.setUpdatedAt(user.getUpdatedAt().toEpochMilli());

        return userDTO;
    }
}
