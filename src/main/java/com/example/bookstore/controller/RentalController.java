package com.example.bookstore.controller;

import com.example.bookstore.service.RentalService;
import com.example.bookstore.service.dto.RentalCreateDTO;
import com.example.bookstore.service.dto.RentalDTO;
import com.example.bookstore.service.dto.RentalUpdateDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rentals")
@RequiredArgsConstructor
public class RentalController {

    private final RentalService rentalService;

    //TODO: add security
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<RentalDTO> create(@RequestBody @Valid RentalCreateDTO rentalCreateDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(rentalService.create(rentalCreateDTO));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<RentalDTO> update(@RequestBody @Valid RentalUpdateDTO rentalUpdateDTO, @PathVariable Long id) {
        return ResponseEntity.ok(rentalService.update(rentalUpdateDTO, id));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('STAFF') OR @rentalService.getUserByRentalId(#id).id.equals(authentication.principal.userId)")
    public ResponseEntity<RentalDTO> get(@PathVariable Long id) {
        return ResponseEntity.ok(rentalService.getById(id));
    }

    //In case a rental is made by mistake (or the user changed their mind) and has to be canceled (a support STAFF will handle it)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        rentalService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
