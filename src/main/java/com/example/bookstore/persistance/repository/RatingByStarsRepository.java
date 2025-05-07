package com.example.bookstore.persistance.repository;

import com.example.bookstore.persistance.entity.Book;
import com.example.bookstore.persistance.entity.RatingByStars;
import com.example.bookstore.service.dto.BookSearchResponseDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingByStarsRepository extends JpaRepository<RatingByStars, Long> {

    List<RatingByStars> findByBook(Book book);

    List<RatingByStars> book(Book book);

    @Query("SELECT new com.example.bookstore.service.dto.BookSearchResponseDTO(" +
            "b.id, b.bookId, b.title, b.publishDate, b.firstPublishDate, b.description, b.format, b.isbn," +
            " b.edition, b.pageNumber,  b.price, b.publisher.id, b.language.id) " +
            "FROM Book b JOIN RatingByStars r ON r.book = b " +
            "WHERE r.star.starNumber = 5 " +
            "ORDER BY (CAST(r.numberOfRatings AS double) / CAST(r.totalNumberOfRatings as double )) desc " +
            "LIMIT :top")
    List<BookSearchResponseDTO> findTopRatedBooks(Integer top);
}
