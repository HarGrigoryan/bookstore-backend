package com.example.bookstore.service.criteria;


import com.example.bookstore.enums.AuthorRole;
import com.example.bookstore.enums.Format;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.time.LocalDate;

@Setter
@Getter

public class BookSearchCriteria extends SearchCriteria {

    private String title;
    private LocalDate publishDate = LocalDate.of(1,1,1);
    private LocalDate firstPublishDate =  LocalDate.of(1,1,1);
    private String authorName;
    private AuthorRole authorRole;
    private Format format;
    private String isbn;
    private String languageName;
    private String edition;
    private Integer pageNumber;
    private BigDecimal price;
    private String genre;
    private String publisherName;
    private String settingName;
    private String seriesName;
    private String seriesNumber;
    private String characterName;
    private String characterComment;
    private String awardName;
    private Integer awardYear;
    private String sortBy = "title";

    @Override
    public PageRequest buildPageRequest() {
        PageRequest pageRequest = super.buildPageRequest();
        if(getSortDirection().equals(SortDirection.ASC))
            return pageRequest.withSort(
                    Sort.by(sortBy).ascending()
            );
        return pageRequest.withSort(
                Sort.by(sortBy).descending());
    }

}
