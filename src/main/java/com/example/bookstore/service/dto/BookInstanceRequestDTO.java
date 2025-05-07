package com.example.bookstore.service.dto;

import com.example.bookstore.enums.BookInstanceStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class BookInstanceRequestDTO {

    @NotBlank
    private Integer copyNumber;

    private Boolean isRentable = false;

    private Boolean isSellable = true;

    private Integer rentCount = 0;

    private BookInstanceStatus status = BookInstanceStatus.AVAILABLE;

    @NotBlank
    private Long bookId;


}
