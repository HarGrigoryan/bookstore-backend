package com.example.bookstore.persistance.repository;

import com.example.bookstore.persistance.entity.BookCoverImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookCoverImageRepository extends JpaRepository<BookCoverImage, Long> {
}
