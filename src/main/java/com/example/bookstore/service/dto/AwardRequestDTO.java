package com.example.bookstore.service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AwardRequestDTO {

    @NotBlank
    private String name;
    private Integer year;

}
