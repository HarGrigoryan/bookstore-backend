package com.example.bookstore.service.dto;

import com.example.bookstore.enums.RoleName;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateDTO {

    @NotBlank
    private String firstname;

    private String lastname;

    @NotBlank
    @Email(message = "Invalid email address")
    private String email;

    @NotBlank
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String temporaryPassword;

    private boolean enabled = true;

    @NotBlank
    private List<RoleName> roles;

}
