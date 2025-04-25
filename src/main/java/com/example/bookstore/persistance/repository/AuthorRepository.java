package com.example.bookstore.persistance.repository;

import com.example.bookstore.persistance.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

    @Query("SELECT a, COUNT(ba.author) as book_count " +
            "FROM Author a " +
            "INNER JOIN a.books ba " +
            "GROUP BY a.id " +
            "ORDER BY book_count desc " +
            "LIMIT :top")
    List<Author> findTopPublishedAuthors(Integer top);

    @Query("SELECT COUNT(ba.author) FROM BookAuthor ba WHERE ba.author.id = :id")
    Integer getNumberOfPublishedBooksById(Long id);

}
