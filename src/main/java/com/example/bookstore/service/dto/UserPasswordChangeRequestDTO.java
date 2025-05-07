package com.example.bookstore.service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserPasswordChangeRequestDTO {

    @NotBlank
    private String email;

    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String newPassword;

}
