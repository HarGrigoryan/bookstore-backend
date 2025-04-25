package com.example.bookstore.persistance.repository;

import com.example.bookstore.persistance.entity.Book;
import com.example.bookstore.persistance.entity.Reviews;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewsRepository extends JpaRepository<Reviews, Long> {

    @Query("SELECT r FROM Reviews r WHERE r.book = :book")
    Reviews findByBook(Book book);
}
