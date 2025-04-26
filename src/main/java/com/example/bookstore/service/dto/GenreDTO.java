package com.example.bookstore.service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class GenreDTO {

    private Long id;

    @NotBlank
    private String name;

}
