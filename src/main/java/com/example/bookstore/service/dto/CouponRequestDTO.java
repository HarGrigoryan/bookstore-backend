package com.example.bookstore.service.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class CouponRequestDTO {

    @NotBlank
    private String code;

    @NotBlank
    @Max(100)
    private BigDecimal discountPercentage;

    private String description;

    private LocalDate startDate;

    private LocalDate endDate;

    private Integer usageLimit;

    private Integer usageLimitPerUser;

    private Boolean isActive;

}
