package com.example.bookstore.service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RentalCreateDTO {

    @NotBlank
    private Long bookInstanceId;

    @NotBlank
    private LocalDate expectedReturnDate;

    @NotBlank
    private Long paymentId;

}
