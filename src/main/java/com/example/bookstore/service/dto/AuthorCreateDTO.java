package com.example.bookstore.service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AuthorCreateDTO {

    @NotBlank
    private String fullName;

    private Boolean isOnGoodreads;

}