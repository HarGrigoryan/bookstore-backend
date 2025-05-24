package com.example.bookstore.security.dto;

import com.example.bookstore.persistance.entity.Sale;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Setter
@Getter
public class SaleResponseDTO {

    private Long id;

    private Long bookInstanceId;

    private Instant soldAt;

    private Long paymentId;

    public static SaleResponseDTO toDTO(Sale sale) {
        SaleResponseDTO dto = new SaleResponseDTO();
        dto.setId(sale.getId());
        dto.setBookInstanceId(sale.getBookInstance().getId());
        dto.setSoldAt(sale.getSoldAt());
        dto.setPaymentId(sale.getPayment().getId());
        return dto;
    }
}
