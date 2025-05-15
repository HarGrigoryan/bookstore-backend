package com.example.bookstore.service.dto;

import com.example.bookstore.enums.BookInstanceStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class BookInstanceRequestDTO {

    @NotBlank
    private Integer copyNumber;

    private Boolean isRentable = false;

    private Boolean isSellable = true;

    private Integer maxRentCount = 0;

    private BookInstanceStatus status = BookInstanceStatus.AVAILABLE;

    @NotBlank
    private BigDecimal initialPrice;

    @NotBlank
    private Long bookId;


}
