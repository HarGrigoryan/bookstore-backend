package com.example.bookstore.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BookReviewDTO {

    private Float rating;

    private Float likedPercent;

    private Integer bbeScore;

    private Integer bbeVotes;
}
