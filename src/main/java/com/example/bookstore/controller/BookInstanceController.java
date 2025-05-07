package com.example.bookstore.controller;

import com.example.bookstore.service.criteria.BookInstanceSearchCriteria;
import com.example.bookstore.service.dto.BookInstanceDTO;
import com.example.bookstore.service.dto.BookInstanceRequestDTO;
import com.example.bookstore.service.dto.PageResponseDTO;
import com.example.bookstore.service.registry.BookInstanceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/book-instances")
@PreAuthorize("hasAuthority('ROLE_STAFF')")
@RequiredArgsConstructor
public class BookInstanceController {

    private final BookInstanceService bookInstanceService;
    //TODO: Proper security

    @GetMapping("/{id}")
    public ResponseEntity<BookInstanceDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(bookInstanceService.getById(id));
    }

    //TODO: Finish and test
    /*@GetMapping
    public PageResponseDTO<BookInstanceDTO> getAll(BookInstanceSearchCriteria criteria) {
        return bookInstanceService.getAll(criteria);
    }*/

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
