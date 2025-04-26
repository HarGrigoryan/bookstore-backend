package com.example.bookstore.service.mapper;

import com.example.bookstore.persistance.entity.Reviews;
import com.example.bookstore.service.dto.ReviewsDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ReviewsMapper implements Mapper<Reviews, ReviewsDTO> {
    @Override
    public Reviews dtoToEntity(ReviewsDTO dto) {
        if(dto == null) return null;
        Reviews reviews = new Reviews();
        reviews.setId(dto.getId());
        reviews.setRating(dto.getRating());
        reviews.setLikedPercent(dto.getLikedPercent());
        reviews.setBbeScore(dto.getBbeScore());
        reviews.setBbeVotes(dto.getBbeVotes());
        reviews.setBook(dto.getBook());
        return reviews;
    }

    @Override
    public ReviewsDTO entityToDto(Reviews entity) {
        if (entity == null) return null;
        ReviewsDTO dto = new ReviewsDTO();
        dto.setId(entity.getId());
        dto.setRating(entity.getRating());
        dto.setLikedPercent(entity.getLikedPercent());
        dto.setBbeScore(entity.getBbeScore());
        dto.setBbeVotes(entity.getBbeVotes());
        dto.setBook(entity.getBook());
        return dto;
    }

    @Override
    public List<ReviewsDTO> entityToDto(Iterable<Reviews> entities) {
        List<ReviewsDTO> dtos = new ArrayList<>();
        for (Reviews entity : entities) {
            dtos.add(entityToDto(entity));
        }
        return dtos;
    }

    @Override
    public List<Reviews> dtoToEntity(Iterable<ReviewsDTO> dtos) {
        List<Reviews> reviewsList = new ArrayList<>();
        for (ReviewsDTO dto : dtos) {
            reviewsList.add(dtoToEntity(dto));
        }
        return reviewsList;
    }
}
