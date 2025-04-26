package com.example.bookstore.service.dto;

import com.example.bookstore.persistance.entity.Book;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ReviewsDTO {

    private Long id;

    private Float rating;

    private Float likedPercent;

    private Integer bbeScore;

    private Integer bbeVotes;

    private Book book;
}
