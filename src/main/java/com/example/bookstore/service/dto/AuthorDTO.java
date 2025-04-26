package com.example.bookstore.service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AuthorDTO {

    private Long id;

    @NotBlank
    private String fullName;

    private Boolean isOnGoodreads;

}
