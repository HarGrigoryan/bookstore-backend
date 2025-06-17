package com.example.bookstore.persistance.repository;

import com.example.bookstore.persistance.entity.Award;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface AwardRepository extends JpaRepository<Award, Long> {

    @Query("""
    SELECT aw FROM Award aw
    WHERE aw.name=:name
    """)
    Optional<Award> findByName(String name);


    @Query("""
        SELECT b.book.id from Award a JOIN a.books b WHERE a.id = :awardId
        """)
    List<Long> publishedBookIds(Long awardId);
}
