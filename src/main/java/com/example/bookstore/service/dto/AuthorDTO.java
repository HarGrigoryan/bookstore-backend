package com.example.bookstore.service.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AuthorDTO {

    private Long id;

    private String fullName;

    private Boolean isOnGoodreads;

}
