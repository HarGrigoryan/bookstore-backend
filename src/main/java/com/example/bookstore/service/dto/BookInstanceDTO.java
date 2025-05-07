package com.example.bookstore.service.dto;

import com.example.bookstore.enums.BookInstanceStatus;
import com.example.bookstore.persistance.entity.BookInstance;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BookInstanceDTO {

    private Long id;

    private Integer copyNumber;

    private Boolean isRentable = false;

    private Boolean isSellable = true;

    private Integer rentCount = 0;

    private BookInstanceStatus status = BookInstanceStatus.AVAILABLE;

    private Long bookId;


    public static BookInstanceDTO toDTO(BookInstance bookInstance) {
        BookInstanceDTO dto = new BookInstanceDTO();
        dto.id = bookInstance.getId();
        dto.copyNumber = bookInstance.getInstanceNumber();
        dto.isRentable = bookInstance.getIsRentable();
        dto.isSellable = bookInstance.getIsSellable();
        dto.rentCount = bookInstance.getRentCount();
        dto.status = bookInstance.getStatus();
        dto.bookId = bookInstance.getBook().getId();
        return dto;
    }
}
