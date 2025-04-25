package com.example.bookstore.persistance.repository;

import com.example.bookstore.persistance.entity.Star;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StarRepository extends JpaRepository<Star, Long> {
    @Query("SELECT s FROM Star s WHERE s.starNumber = :starNumner")
    Star findByStarNumber(Integer starNumber);
}
