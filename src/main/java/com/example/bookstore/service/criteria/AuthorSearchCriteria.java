package com.example.bookstore.service.criteria;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@Setter
@Getter
public class AuthorSearchCriteria extends SearchCriteria{

    private String fullName;
    private Boolean isOnGoodreads;
    private Long bookId;

    private static final String sortBy = "fullName";

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
