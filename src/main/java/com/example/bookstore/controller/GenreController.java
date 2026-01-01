package com.example.bookstore.controller;

import com.example.bookstore.service.GenreService;
import com.example.bookstore.service.dto.GenreDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/genres")
@RequiredArgsConstructor
public class GenreController {

    private final GenreService genreService;

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'STAFF')")
    public List<GenreDTO> getAll() {
        return genreService.getAll();
    }

}
