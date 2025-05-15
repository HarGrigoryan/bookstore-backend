package com.example.bookstore.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserSearchResponseDTO {

    private Long id;
    private String firstname;
    private String lastname;
    private String email;
    private Boolean enabled;
    private Instant createdAt;
    private Instant updatedAt;

}
