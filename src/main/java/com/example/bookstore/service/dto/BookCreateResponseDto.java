package com.example.bookstore.service.dto;

import com.example.bookstore.enums.Format;
import jakarta.validation.constraints.NotBlank;
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
public class BookCreateResponseDto {

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

    private String edition;

    private Integer pageNumber;

    private BigDecimal price;

}
