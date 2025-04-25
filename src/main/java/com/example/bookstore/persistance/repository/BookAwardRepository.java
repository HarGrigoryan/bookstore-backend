package com.example.bookstore.persistance.repository;

import com.example.bookstore.persistance.entity.BookAward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookAwardRepository extends JpaRepository<BookAward, Long> {
}
