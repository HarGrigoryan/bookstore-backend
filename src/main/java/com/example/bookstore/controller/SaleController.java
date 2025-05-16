package com.example.bookstore.controller;

import com.example.bookstore.security.dto.SaleResponseDTO;
import com.example.bookstore.service.SaleService;
import com.example.bookstore.service.dto.SaleCreateRequestDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sales")
@RequiredArgsConstructor
public class SaleController {

    private final SaleService saleService;

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('STAFF') OR @saleService.getUserBySaleId(#id).id.equals(authentication.principal.userId)")
    public ResponseEntity<SaleResponseDTO> get(@PathVariable Long id) {
        return ResponseEntity.ok(saleService.getById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('USER') AND authentication.principal.userId.equals(#saleCreateRequestDTO.userId)")
    public ResponseEntity<SaleResponseDTO> create(@RequestBody @Valid SaleCreateRequestDTO saleCreateRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(saleService.create(saleCreateRequestDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        saleService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
