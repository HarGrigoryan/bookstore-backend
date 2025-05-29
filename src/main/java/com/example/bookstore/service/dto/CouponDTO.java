package com.example.bookstore.service.dto;

import com.example.bookstore.persistance.entity.Coupon;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Getter
@Setter
public class CouponDTO {

    private Long id;

    private String code;

    private BigDecimal discountPercentage;

    private String description;

    private LocalDate startDate;

    private LocalDate endDate;

    private Integer usageLimit;

    private Integer usageLimitPerUser;

    private Boolean isActive;

    private Instant createdAt;

    private Instant updatedAt;

    public static CouponDTO toDTO(Coupon coupon) {
        CouponDTO dto = new CouponDTO();
        dto.setId(coupon.getId());
        dto.setCode(coupon.getCode());
        dto.setDiscountPercentage(coupon.getDiscountPercentage());
        dto.setDescription(coupon.getDescription());
        dto.setStartDate(coupon.getStartDate());
        dto.setEndDate(coupon.getEndDate());
        dto.setUsageLimit(coupon.getUsageLimit());
        dto.setUsageLimitPerUser(coupon.getUsageLimitPerUser());
        dto.setIsActive(coupon.getIsActive());
        dto.setCreatedAt(coupon.getCreatedAt());
        dto.setUpdatedAt(coupon.getUpdatedAt());
        return dto;
    }

}
