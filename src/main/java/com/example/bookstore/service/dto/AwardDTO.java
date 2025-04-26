package com.example.bookstore.service.dto;

import jakarta.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AwardDTO {

    private Long id;
    @NotBlank
    private String name;
    private Integer year;

}
