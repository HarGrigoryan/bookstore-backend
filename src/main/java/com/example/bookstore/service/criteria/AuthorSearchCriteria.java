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

    @Override
    public PageRequest buildPageRequest() {
        PageRequest pageRequest = super.buildPageRequest();

        return pageRequest.withSort(Sort.by("fullName"));
    }
}
