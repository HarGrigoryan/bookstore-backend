package com.example.bookstore.service.dto;

import com.example.bookstore.enums.PaymentStatus;
import com.example.bookstore.persistance.entity.Payment;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
public class PaymentDTO {

    private Long paymentId;

    private Long userId;

    private BigDecimal amount;

    private PaymentStatus status;

    private String transactionReference;

    private Instant paymentStarted;

    private Instant paymentLastUpdated;

    public static PaymentDTO toDTO(Payment payment) {
        PaymentDTO dto = new PaymentDTO();
        dto.setPaymentId(payment.getId());
        dto.setUserId(payment.getUser().getId());
        dto.setAmount(payment.getAmount());
        dto.setStatus(payment.getStatus());
        dto.setTransactionReference(payment.getTransactionReference());
        dto.setPaymentStarted(payment.getCreatedAt());
        dto.setPaymentLastUpdated(payment.getUpdatedAt());
        return dto;
    }

}
