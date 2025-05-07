package com.example.bookstore.service.dto;

import com.example.bookstore.enums.AuthorRole;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class AuthorResponseDTO {
    private Long id;

    @NotBlank
    private String fullName;

    private boolean isOnGoodreads;

    private AuthorRole authorRole;
}
