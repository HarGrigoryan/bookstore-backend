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
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;


@RestController
@RequestMapping("/book-instances")
@RequiredArgsConstructor
public class BookInstanceController {

    private final BookInstanceService bookInstanceService;
    //TODO: Proper security

    @GetMapping("/{id}")
    public ResponseEntity<BookInstanceDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(bookInstanceService.getById(id));
    }

    @GetMapping
    public PageResponseDTO<BookInstanceDTO> getAll(BookInstanceSearchCriteria criteria) {
        return bookInstanceService.getAll(criteria);
    }

    @GetMapping("/{id}/renting-cost")
    public ResponseEntity<BigDecimal> getRentingCost(@PathVariable Long id) {
        return ResponseEntity.ok(bookInstanceService.getRentingCost(id));
    }

    @GetMapping("/{id}/sale-cost")
    public ResponseEntity<BigDecimal> getSaleCost(@PathVariable Long id) {
        return ResponseEntity.ok(bookInstanceService.getSaleCost(id));
    }

    @PostMapping
    public ResponseEntity<BookInstanceDTO> create(@RequestBody @Valid BookInstanceRequestDTO bookInstanceRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookInstanceService.create(bookInstanceRequestDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookInstanceDTO> update(@PathVariable Long id, @RequestBody @Valid BookInstanceRequestDTO bookInstanceRequestDTO) {
        return ResponseEntity.ok(bookInstanceService.updateById(id, bookInstanceRequestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        bookInstanceService.deleteById(id);
        return ResponseEntity.noContent().build();
    }


}
