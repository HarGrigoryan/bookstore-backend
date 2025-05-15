package com.example.bookstore.service.criteria;

import com.example.bookstore.enums.BookInstanceStatus;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class BookInstanceSearchCriteria extends SearchCriteria {

    private Integer instanceNumber;
    private Boolean isRentable;
    private Boolean isSellable;
    private Integer maxRentCount;
    private Integer currentRentCount;
    private BigDecimal maxInitialPrice;
    private BigDecimal maxCurrentPrice;
    private BookInstanceStatus bookInstanceStatus;
    private Long bookId;
    private LocalDateTime createdAt = LocalDateTime.of(1000, 1, 1, 0, 0);
    private LocalDateTime updatedAt =LocalDateTime.of(1000, 1, 1, 0, 0);
    private String sortBy = "book";


    @Override
    public PageRequest buildPageRequest() {
        PageRequest pageRequest = super.buildPageRequest();

        return pageRequest.withSort(Sort.by(sortBy).descending());
    }


}
