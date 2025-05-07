package com.example.bookstore.service.criteria;

import com.example.bookstore.enums.BookInstanceStatus;
import com.example.bookstore.enums.RentalStatus;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@Getter
@Setter
public class BookInstanceSearchCriteria extends SearchCriteria {

    private Integer instanceNumber;
    private Boolean isRentable;
    private Boolean isSellable;
    private Integer rentCount;
    private BookInstanceStatus bookInstanceStatus;
    private Long bookId;
    private RentalStatus rentalStatus;
    private String sortBy = "instance_number";

    @Override
    public PageRequest buildPageRequest() {
        PageRequest pageRequest = super.buildPageRequest();

        return pageRequest.withSort(Sort.by(sortBy).descending());
    }


}
