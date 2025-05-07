package com.example.bookstore.service;

import com.example.bookstore.persistance.entity.Book;
import com.example.bookstore.persistance.entity.RatingByStars;
import com.example.bookstore.persistance.repository.BookRepository;
import com.example.bookstore.persistance.repository.RatingByStarsRepository;
import com.example.bookstore.persistance.repository.StarRepository;
import com.example.bookstore.exception.EntityNotFoundException;
import com.example.bookstore.service.dto.BookSearchResponseDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@AllArgsConstructor
public class RatingService {

    private final RatingByStarsRepository ratingByStarsRepository;
    private final BookRepository bookRepository;
    private final StarRepository starRepository;


    public Integer rateBookById(Long bookId, Integer starNumber) {
        Book book = bookRepository.findById(bookId).orElse(null);
        if(book == null) {
            throw new EntityNotFoundException("Book with id '" + bookId + "' not found");
        }
        List<RatingByStars> ratingByStarsList = ratingByStarsRepository.findByBook(book);
        if(ratingByStarsList.isEmpty() || !containsStarRating(ratingByStarsList, starNumber)) {
            RatingByStars ratingByStars = new RatingByStars();
            ratingByStars.setBook(book);
            ratingByStars.setTotalNumberOfRatings(0);
            ratingByStars.setTotalNumberOfRatings(0);
            ratingByStars.setStar(starRepository.findByStarNumber(starNumber));
            ratingByStarsList.add(ratingByStars);
        }
        int numberOfRatings = 0;
        for (RatingByStars ratingByStars : ratingByStarsList) {
            if(ratingByStars.getStar().getStarNumber().equals(starNumber)) {
                numberOfRatings = ratingByStars.getNumberOfRatings() + 1;
                ratingByStars.setNumberOfRatings(numberOfRatings);
            }
            ratingByStars.setTotalNumberOfRatings(ratingByStars.getTotalNumberOfRatings() + 1);
        }
        ratingByStarsRepository.saveAll(ratingByStarsList);
        return numberOfRatings;
    }

    private boolean containsStarRating(List<RatingByStars> ratingByStarsList, Integer starNumber) {
        for (RatingByStars ratingByStars : ratingByStarsList) {
            if(ratingByStars.getStar().getStarNumber().equals(starNumber)) {
                return true;
            }
        }
        return false;
    }

    public List<BookSearchResponseDTO> getTopRatedBooks(Integer top) {
        return ratingByStarsRepository.findTopRatedBooks(top);
    }
}
