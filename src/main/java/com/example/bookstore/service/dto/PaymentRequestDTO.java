package com.example.bookstore.service.dto;

import com.example.bookstore.enums.PaymentCurrency;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PaymentRequestDTO {

    @NotBlank
    private Long userId;

    @NotBlank
    private PaymentCurrency currency;

    @NotBlank
    private BigDecimal amount;

    @NotBlank
    // In the full-scale application this would be handled differently
    private String paymentInformation;
}
