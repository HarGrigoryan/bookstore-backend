package com.example.bookstore.controller;

import com.example.bookstore.service.PublisherService;
import com.example.bookstore.service.dto.BookSearchResponseDTO;
import com.example.bookstore.service.dto.PublisherDTO;
import jakarta.annotation.security.PermitAll;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/publishers")
@AllArgsConstructor
public class PublisherController {

    PublisherService publisherService;

    @GetMapping("/{id}/books")
    @PreAuthorize("hasAnyRole('STAFF', 'USER')")
    public List<BookSearchResponseDTO> getPublishedBooks(@PathVariable Long id) {
        return publisherService.getPublishedBooks(id);
    }

    @GetMapping
    @PreAuthorize("hasRole('MANAGER') OR hasPermission('ROLE_STAFF', 'ADD_INFORMATION')")
    public List<PublisherDTO> getAllPublishers() {
        return publisherService.getAllPublisher();
    }


    @GetMapping("/{id}")
    @PermitAll
    public PublisherDTO getPublisher(@PathVariable Long id) {
        return publisherService.getPublisherById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('MANAGER') OR hasPermission('ROLE_STAFF', 'ADD_INFORMATION')")
    public PublisherDTO createPublisher(@RequestBody PublisherDTO publisherDto) {
        return publisherService.createPublisher(publisherDto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('STAFF')")
    public PublisherDTO updatePublisher(@PathVariable Long id, @RequestBody PublisherDTO publisherDto) {
        return publisherService.updatePublisher(id, publisherDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('MANAGER') OR hasPermission('ROLE_STAFF', 'REMOVE_INFORMATION')")
    public void deletePublisherById(@PathVariable Long id) {
        publisherService.deletePublisherById(id);
    }

}
