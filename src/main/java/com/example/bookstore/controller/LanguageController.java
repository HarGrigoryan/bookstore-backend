package com.example.bookstore.controller;

import com.example.bookstore.service.LanguageService;
import com.example.bookstore.service.dto.LanguageDTO;
import com.example.bookstore.service.dto.LanguageRequestDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/languages")
@RequiredArgsConstructor
public class LanguageController {

    private final LanguageService languageService;

    @GetMapping("/{id}")
    public LanguageDTO getById(@PathVariable Long id) {
        return languageService.getById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public void deleteById(@PathVariable Long id) {
        languageService.delete(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_STAFF')")
    public LanguageDTO updateById(@PathVariable Long id, @RequestBody @Valid LanguageRequestDTO languageDTO) {
        return languageService.updateById(id, languageDTO);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ROLE_STAFF')")
    public LanguageDTO create(@RequestBody @Valid LanguageRequestDTO languageDTO) {
        return languageService.create(languageDTO);
    }

}
