package com.example.bookstore.controller;

import com.example.bookstore.service.PublisherService;
import com.example.bookstore.service.dto.BookSearchResponseDTO;
import com.example.bookstore.service.dto.PublisherDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/publishers")
@AllArgsConstructor
//TODO: Improve Security
public class PublisherController {

    PublisherService publisherService;

    @GetMapping("/{id}/books")
    @PreAuthorize("permitAll()")
    public List<BookSearchResponseDTO> getPublishedBooks(@PathVariable Long id)
    {
        return publisherService.getPublishedBooks(id);
    }

    @GetMapping("/{id}")
    public PublisherDTO getPublisher(@PathVariable Long id) {
        return publisherService.getPublisherById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('ROLE_STAFF')")
    public PublisherDTO createPublisher(@RequestBody PublisherDTO publisherDto) {
        return publisherService.createPublisher(publisherDto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_STAFF')")
    public PublisherDTO updatePublisher(@PathVariable Long id, @RequestBody PublisherDTO publisherDto) {
        return publisherService.updatePublisher(id, publisherDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER')")
    public void deletePublisherById(@PathVariable Long id) {
        publisherService.deletePublisherById(id);
    }

}
