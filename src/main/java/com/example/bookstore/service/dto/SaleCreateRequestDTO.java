package com.example.bookstore.service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SaleCreateRequestDTO {

    @NotBlank
    private Long bookInstanceId;

    @NotBlank
    private Long userId;

    @NotBlank
    private Long paymentId;
}
