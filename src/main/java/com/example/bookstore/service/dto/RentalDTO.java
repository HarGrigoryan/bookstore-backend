package com.example.bookstore.service.dto;

import com.example.bookstore.enums.RentalStatus;
import com.example.bookstore.persistance.entity.Rental;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RentalDTO {

    private Long id;

    private Long bookInstanceId;

    private Long userId;

    private LocalDate expectedReturnDate;

    private LocalDate actualReturnDate;

    private RentalStatus status;

    private Long paymentId;

    public static RentalDTO mapToDTO(Rental rental) {
        RentalDTO dto = new RentalDTO();
        dto.setId(rental.getId());
        dto.setBookInstanceId(rental.getBookInstance().getId());
        dto.setUserId(rental.getUser().getId());
        dto.setExpectedReturnDate(rental.getExpectedReturnDate());
        dto.setActualReturnDate(rental.getActualReturnDate());
        dto.setStatus(rental.getStatus());
        dto.setPaymentId(rental.getPayment().getId());
        return dto;
    }

}
