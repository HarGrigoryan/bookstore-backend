package com.example.bookstore.controller;

import com.example.bookstore.service.PaymentService;
import com.example.bookstore.service.dto.PaymentDTO;
import com.example.bookstore.service.dto.PaymentRequestDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    @PreAuthorize("hasRole('USER') AND #paymentRequestDTO.userId.equals(authentication.principal.userId)")
    public ResponseEntity<PaymentDTO> create(@RequestBody @Valid PaymentRequestDTO paymentRequestDTO) {
        return ResponseEntity.ok(paymentService.pay(paymentRequestDTO));
    }

    @GetMapping("/{id}")
    @PreAuthorize("authentication.principal.userId.equals(@paymentService.getUserByPaymentId(#id).id) OR hasRole('STAFF')")
    public ResponseEntity<PaymentDTO> get(@PathVariable Long id) {
        return ResponseEntity.ok(paymentService.getById(id));
    }


}
