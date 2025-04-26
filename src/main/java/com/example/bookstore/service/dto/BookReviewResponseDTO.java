package com.example.bookstore.service.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BookReviewResponseDTO {

    private Long id;

    private Float rating;

    private Float likedPercent;

    private Integer bbeScore;

    private Integer bbeVotes;

    private Long bookId;

}
