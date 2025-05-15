package com.example.bookstore.controller;

import com.example.bookstore.security.dto.SaleResponseDTO;
import com.example.bookstore.service.SaleService;
import com.example.bookstore.service.dto.SaleCreateRequestDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sales")
@RequiredArgsConstructor
public class SaleController {

    private final SaleService saleService;

    @GetMapping("/{id}")
    public ResponseEntity<SaleResponseDTO> get(@PathVariable Long id) {
        return ResponseEntity.ok(saleService.getById(id));
    }

    @PostMapping
    public ResponseEntity<SaleResponseDTO> create(@RequestBody @Valid SaleCreateRequestDTO saleCreateRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(saleService.create(saleCreateRequestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        saleService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
