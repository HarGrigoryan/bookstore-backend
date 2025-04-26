package com.example.bookstore.service.dto;

import com.example.bookstore.enums.AuthorRole;
import com.example.bookstore.enums.Format;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BookCreateDTO {
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

    private List<Long> authorIds;

    private List<AuthorRole> authorRoles;

    private List<Long> genreIds;

    private List<Long> settingIds;

    private List<Long> characterIds;

    private List<Long> awardIds;

    private Long seriesId;

    private String seriesNumber;

    private String coverImageURL;
}
