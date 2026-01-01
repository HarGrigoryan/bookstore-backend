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
import jakarta.annotation.security.PermitAll;
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

    @PermitAll
    @GetMapping("/{id}")
    public BookSearchResponseDTO getBook(@PathVariable Long id) {
        return bookService.getBookById(id);
    }

    @GetMapping("/bookId")
    @PreAuthorize("hasRole('ROLE_STAFF')")
    public BookSearchResponseDTO getBookById(@RequestParam String bookId) {
        return bookService.getBookByBookId(bookId);
    }

    @GetMapping("/{id}/authors")
    @PreAuthorize("hasAnyRole('USER', 'STAFF')")
    public List<AuthorResponseDTO> getAuthors(@PathVariable Long id) {
        return bookService.getAuthors(id);
    }

    @GetMapping("/{id}/characters")
    @PreAuthorize("hasAnyRole('USER', 'STAFF')")
    public List<CharacterDTO> getCharacters(@PathVariable Long id) {
        return bookService.getCharacters(id);
    }

    @GetMapping
    @PermitAll
    public PageResponseDTO<BookSearchResponseDTO> getAll(BookSearchCriteria criteria) {
        return bookService.getAll(criteria);
    }

    @GetMapping("/{id}/cover-image")
    @PermitAll
    public String getImage(@PathVariable Long id, @RequestParam("size") PictureSize pictureSize) {
        return coverImageService.getImage(id, pictureSize);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('STAFF')")
    public BookSearchResponseDTO updateBook(@RequestBody BookUpdateRequestDTO bookUpdateRequestDTO, @PathVariable Long id) {
        return bookService.updateBook(bookUpdateRequestDTO, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('MANAGER') OR hasPermission('ROLE_STAFF', 'REMOVE_BOOK')")
    public void deleteBook(@PathVariable Long id) {
        bookService.deleteBookById(id);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('MANAGER') OR hasPermission('ROLE_STAFF', 'REMOVE_BOOK')")
    public void deleteBooks(@RequestBody List<Long> bookIds) {
        bookService.deleteBooks(bookIds);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('MANAGER') OR hasPermission('ROLE_STAFF', 'ADD_BOOK_DATA')")
    public BookCreateResponseDto createBook(@RequestBody @Valid BookCreateDTO bookCreateDto) {
        return bookService.createBook(bookCreateDto);
    }

    @SneakyThrows
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, path = "/csv")
    @PreAuthorize("hasRole('MANAGER') OR hasPermission('ROLE_STAFF', 'UPLOAD_CSV')")
    public boolean uploadBooks(@RequestParam("file") MultipartFile file) {
        return csvUploadService.uploadCSV(file);
    }

    @PostMapping("/{id}/review")
    @PreAuthorize("hasRole('MANAGER') OR hasPermission('ROLE_STAFF', 'ADD_INFORMATION')")
    public BookReviewResponseDTO reviewBook(@PathVariable Long id, @RequestBody BookReviewDTO bookReviewDto) {
        return bookService.reviewBook(id, bookReviewDto);
    }

    @PostMapping("{id}/rate")
    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Integer rate(@PathVariable Long id, @RequestParam(name = "starNumber") Integer starNumber) {
        return ratingService.rateBookById(id, starNumber);
    }

    @GetMapping("/top-rated")
    @PreAuthorize("hasAnyRole('USER', 'STAFF')")
    public List<BookSearchResponseDTO> getTopRatedBooks(@RequestParam(name="top") Integer top) {
        return ratingService.getTopRatedBooks(top);
    }

}
