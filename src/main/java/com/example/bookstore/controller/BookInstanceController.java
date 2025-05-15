package com.example.bookstore.controller;

import com.example.bookstore.service.criteria.BookInstanceSearchCriteria;
import com.example.bookstore.service.dto.BookInstanceDTO;
import com.example.bookstore.service.dto.BookInstanceRequestDTO;
import com.example.bookstore.service.BookInstanceService;
import com.example.bookstore.service.dto.PageResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;


@RestController
@RequestMapping("/book-instances")
@RequiredArgsConstructor
public class BookInstanceController {

    private final BookInstanceService bookInstanceService;

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'STAFF')")
    public ResponseEntity<BookInstanceDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(bookInstanceService.getById(id));
    }

    @GetMapping
    @PreAuthorize("hasRole('STAFF')")
    public PageResponseDTO<BookInstanceDTO> getAll(BookInstanceSearchCriteria criteria) {
        return bookInstanceService.getAll(criteria);
    }

    @GetMapping("/{id}/renting-cost")
    @PreAuthorize("hasAnyRole('USER', 'STAFF')")
    public ResponseEntity<BigDecimal> getRentingCost(@PathVariable Long id) {
        return ResponseEntity.ok(bookInstanceService.getRentingCost(id));
    }

    @GetMapping("/{id}/sale-cost")
    @PreAuthorize("hasAnyRole('USER', 'STAFF')")
    public ResponseEntity<BigDecimal> getSaleCost(@PathVariable Long id) {
        return ResponseEntity.ok(bookInstanceService.getSaleCost(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('MANAGER') OR hasPermission('ROLE_STAFF', 'ADD_BOOK_INSTANCE')")
    public ResponseEntity<BookInstanceDTO> create(@RequestBody @Valid BookInstanceRequestDTO bookInstanceRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookInstanceService.create(bookInstanceRequestDTO));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<BookInstanceDTO> update(@PathVariable Long id, @RequestBody @Valid BookInstanceRequestDTO bookInstanceRequestDTO) {
        return ResponseEntity.ok(bookInstanceService.updateById(id, bookInstanceRequestDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER') OR hasPermission('ROLE_STAFF', 'REMOVE_INFORMATION')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        bookInstanceService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
