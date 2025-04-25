package com.example.bookstore.persistance.repository;

import com.example.bookstore.persistance.entity.BookSeries;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface BookSeriesRepository extends JpaRepository<BookSeries, Long> {
}
