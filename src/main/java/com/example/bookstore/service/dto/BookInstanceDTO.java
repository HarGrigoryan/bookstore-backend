package com.example.bookstore.service.dto;

import com.example.bookstore.enums.BookInstanceStatus;
import com.example.bookstore.persistance.entity.BookInstance;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BookInstanceDTO {

    private Long id;

    private Integer instanceNumber;

    private Boolean isRentable;

    private Boolean isSellable;

    private Integer maxRentCount;

    private BigDecimal price;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private BookInstanceStatus status = BookInstanceStatus.AVAILABLE;

    private Long bookId;


    public static BookInstanceDTO toDTO(BookInstance bookInstance) {
        BookInstanceDTO dto = new BookInstanceDTO();
        dto.id = bookInstance.getId();
        dto.instanceNumber = bookInstance.getInstanceNumber();
        dto.isRentable = bookInstance.getIsRentable();
        dto.isSellable = bookInstance.getIsSellable();
        dto.maxRentCount = bookInstance.getMaxRentCount();
        dto.status = bookInstance.getStatus();
        dto.bookId = bookInstance.getBook().getId();
        dto.createdAt = bookInstance.getCreatedAt();
        dto.updatedAt = bookInstance.getUpdatedAt();
        dto.price = bookInstance.getInitialPrice();
        return dto;
    }
}
