package com.example.bookstore.service.dto;

import com.example.bookstore.enums.Format;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BookSearchResponseDTO {

    private Long id;

    private String bookId;

    private String title;

    private LocalDate publishDate;

    private LocalDate firstPublishDate;

    private String description;

    private Format format;

    private String isbn;

    private String edition;

    private Integer pageNumber;

    private BigDecimal price;

    private Long publisherId;

    private Long languageId;
}
