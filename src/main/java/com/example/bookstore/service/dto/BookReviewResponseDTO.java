package com.example.bookstore.service.dto;


import com.example.bookstore.persistance.entity.Reviews;
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

    public static BookReviewResponseDTO mapToDTO(Reviews reviews) {
        BookReviewResponseDTO bookReviewResponseDto = new BookReviewResponseDTO();
        bookReviewResponseDto.setId(reviews.getId());
        bookReviewResponseDto.setRating(reviews.getRating());
        bookReviewResponseDto.setLikedPercent(reviews.getLikedPercent());
        bookReviewResponseDto.setBbeScore(reviews.getBbeScore());
        bookReviewResponseDto.setBbeVotes(reviews.getBbeVotes());
        bookReviewResponseDto.setBookId(reviews.getBook().getId());
        return bookReviewResponseDto;
    }
}
