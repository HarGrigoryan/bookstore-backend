package com.example.bookstore.persistance.repository;

import com.example.bookstore.persistance.entity.Series;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeriesRepository extends JpaRepository<Series, Long> {

    @Query("""
       SELECT bs.book.id FROM  BookSeries bs WHERE bs.series.id = :id
       """)
    List<Long> dependentBookIds(Long id);

    @Query("""
        SELECT s FROM Series s WHERE s.name = :name
        """)
    Series findByName(@NotBlank String name);
}
