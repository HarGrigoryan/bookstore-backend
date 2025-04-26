package com.example.bookstore.service.parser;

import com.example.bookstore.service.dto.ReviewsDTO;

public class ReviewsParser
{

    public ReviewsDTO parseReviews(String rating, String likedPercent,
                                   String bbeScore, String bbeVotes){
        ReviewsDTO dto = new ReviewsDTO();
        try {
            if(!rating.trim().isEmpty())
                dto.setRating(Float.parseFloat(rating.trim()));
            else
                dto.setRating(null);
            if(!likedPercent.isEmpty())
                dto.setLikedPercent(Float.parseFloat(likedPercent.trim()));
            else
                dto.setLikedPercent(null);
            if(!bbeScore.trim().isEmpty())
                dto.setBbeScore(Integer.parseInt(bbeScore.trim()));
            else
                dto.setBbeScore(null);
            if(!bbeVotes.trim().isEmpty())
                dto.setBbeVotes(Integer.parseInt(bbeVotes.trim()));
            else
                dto.setBbeVotes(null);
        }catch (NumberFormatException e){
            System.out.println("Error in parsing reviews: " + e.getMessage());
        }
        return dto;
    }

}
