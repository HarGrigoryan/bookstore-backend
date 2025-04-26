package com.example.bookstore.service.dto;

import com.example.bookstore.enums.Format;
import com.example.bookstore.persistance.entity.Author;
import com.example.bookstore.persistance.entity.Language;
import com.example.bookstore.persistance.entity.Publisher;
import com.example.bookstore.persistance.entity.Reviews;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class BookDTO {

    private Long id;

    @NotBlank
    private String bookId;

    @NotBlank
    private String title;

    private Publisher publisher;

    private LocalDate publishDate;

    private LocalDate firstPublishDate;

    private String description;

    private Format format;

    private Reviews reviews;

    private String isbn;

    private Language language;

    private Author author;

    private String edition;

    private Integer pageNumber;

    private BigDecimal price;

}
