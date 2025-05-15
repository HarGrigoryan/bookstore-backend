package com.example.bookstore.controller;

import com.example.bookstore.service.PaymentService;
import com.example.bookstore.service.dto.PaymentDTO;
import com.example.bookstore.service.dto.PaymentRequestDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentDTO> create(@RequestBody @Valid PaymentRequestDTO paymentRequestDTO) {
        return ResponseEntity.ok(paymentService.pay(paymentRequestDTO));
    }

}
