package com.example.bookstore.controller;

import com.example.bookstore.service.AuthorService;
import com.example.bookstore.service.criteria.AuthorSearchCriteria;
import com.example.bookstore.service.dto.AuthorCreateDTO;
import com.example.bookstore.service.dto.AuthorDTO;
import com.example.bookstore.service.dto.PageResponseDTO;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/authors")
public class AuthorController {

    private final AuthorService authorService;

    @GetMapping("/top")
    public List<AuthorDTO> getTopPublishedAuthors(@RequestParam Integer top)
    {
        return authorService.getTopPublishedAuthors(top);
    }

    @GetMapping("/{id}/books-count")
    public Integer getNumberOfPublishedBooksById(@PathVariable Long id)
    {
        return authorService.getNumberOfPublishedBooksById(id);
    }

    @GetMapping
    public PageResponseDTO<AuthorDTO> getAuthors(AuthorSearchCriteria authorSearchCriteria)
    {
        return authorService.getAuthors(authorSearchCriteria);
    }


    @GetMapping("/{id}")
    public AuthorDTO getAuthorById(@PathVariable Long id)
    {
        return authorService.getAuthorById(id);
    }

    @PutMapping
    @PreAuthorize("hasAuthority('ROLE_STAFF')")
    public AuthorDTO updateAuthor(@RequestBody AuthorDTO authorDto)
    {
        return authorService.updateAuthor(authorDto);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ROLE_STAFF')")
    public AuthorDTO createAuthor(@RequestBody @Valid AuthorCreateDTO authorCreateDTO)
    {
        return authorService.createAuthor(authorCreateDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public ResponseEntity<String> deleteAuthor(@PathVariable Long id)
    {
        authorService.deleteAuthor(id);
        return ResponseEntity.ok().build();
    }
}
