package com.example.bookstore.controller;

import com.example.bookstore.service.LanguageService;
import com.example.bookstore.service.dto.LanguageDTO;
import com.example.bookstore.service.dto.LanguageRequestDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/languages")
@RequiredArgsConstructor
public class LanguageController {

    private final LanguageService languageService;

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'STAFF')")
    public LanguageDTO getById(@PathVariable Long id) {
        return languageService.getById(id);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'STAFF')")
    public List<LanguageDTO> getAll() {
        return languageService.getAll();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ROLE_MANAGER') OR hasPermission('ROLE_STAFF', 'REMOVE_INFORMATION')")
    public void deleteById(@PathVariable Long id) {
        languageService.delete(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_STAFF')")
    public LanguageDTO updateById(@PathVariable Long id, @RequestBody @Valid LanguageRequestDTO languageDTO) {
        return languageService.updateById(id, languageDTO);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ROLE_MANAGER') OR hasPermission('ROLE_STAFF', 'ADD_INFORMATION')")
    public LanguageDTO create(@RequestBody @Valid LanguageRequestDTO languageDTO) {
        return languageService.create(languageDTO);
    }

}
