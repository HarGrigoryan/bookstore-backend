package com.example.bookstore.controller;

import com.example.bookstore.enums.PictureSize;
import com.example.bookstore.service.BookService;
import com.example.bookstore.service.CSVUploadService;
import com.example.bookstore.service.CoverImageService;
import com.example.bookstore.service.RatingService;
import com.example.bookstore.service.criteria.BookSearchCriteria;
import com.example.bookstore.service.dto.*;
import com.example.bookstore.service.dto.AuthorResponseDTO;
import com.example.bookstore.service.dto.BookUpdateRequestDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;
    private final CSVUploadService csvUploadService;
    private final CoverImageService coverImageService;
    private final RatingService ratingService;

    @GetMapping("/{id}")
    public BookSearchResponseDTO getBook(@PathVariable Long id) {
        return bookService.getBookById(id);
    }

    @GetMapping("/bookId")
    @PreAuthorize("hasAnyAuthority('ROLE_STAFF')")
    public BookSearchResponseDTO getBookById(@RequestParam String bookId) {
        return bookService.getBookByBookId(bookId);
    }

    @GetMapping("/{id}/authors")
    public List<AuthorResponseDTO> getAuthors(@PathVariable Long id) {
        return bookService.getAuthors(id);
    }

    @GetMapping
    public PageResponseDTO<BookSearchResponseDTO> getAll(BookSearchCriteria criteria) {
        return bookService.getAll(criteria);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_STAFF')")
    public BookSearchResponseDTO updateBook(@RequestBody BookUpdateRequestDTO bookUpdateRequestDTO, @PathVariable Long id) {
        return bookService.updateBook(bookUpdateRequestDTO, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public void deleteBook(@PathVariable Long id) {
        bookService.deleteBookById(id);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public void deleteBooks(@RequestBody List<Long> bookIds) {
        bookService.deleteBooks(bookIds);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ROLE_STAFF')")
    public BookCreateResponseDto createBook(@RequestBody @Valid BookCreateDTO bookCreateDto) {
        return bookService.createBook(bookCreateDto);
    }

    @SneakyThrows
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, path = "/csv")
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public boolean uploadBooks(@RequestParam("file") MultipartFile file ) {
        return csvUploadService.uploadCSV(file);
    }

    @PostMapping("/{id}/review")
    @PreAuthorize("hasAuthority('ROLE_STAFF')")
    public BookReviewResponseDTO reviewBook(@PathVariable Long id, @RequestBody BookReviewDTO bookReviewDto) {
        return bookService.reviewBook(id, bookReviewDto);
    }

    @GetMapping("/{id}/cover-image")
    public String getImage(@PathVariable Long id, @RequestParam("size") PictureSize pictureSize) {
        return coverImageService.getImage(id, pictureSize);
    }

    @PostMapping("{id}/rate")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Integer rate(@PathVariable Long id, @RequestParam(name = "starNumber") Integer starNumber) {
        return ratingService.rateBookById(id, starNumber);
    }

    @GetMapping("/top-rated")
    public List<BookSearchResponseDTO> getTopRatedBooks(@RequestParam(name="top") Integer top) {
        return ratingService.getTopRatedBooks(top);
    }

}
