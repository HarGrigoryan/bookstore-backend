package com.example.bookstore.service.dto;

import com.example.bookstore.enums.Format;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BookUpdateRequestDTO {

    private Long id;

    @NotBlank
    private String bookId;

    @NotBlank
    private String title;

    private LocalDate publishDate;

    private LocalDate firstPublishDate;

    private String description;

    private Format format;

    private String isbn;

    private Long languageId;

    private Long publisherId;

    private String edition;

    private Integer pageNumber;

    private BigDecimal price;

}
