package com.example.bookstore.persistance.repository;

import com.example.bookstore.persistance.entity.Publisher;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PublisherRepository extends JpaRepository<Publisher, Long> {
    boolean existsByName(@NotBlank String name);

    @Query("""
       SELECT b.id FROM Publisher p JOIN Book b ON b.publisher = p WHERE p.id = :publisherId
       """)
    List<Long> publishedBookIds(@NotBlank Long publisherId);
}
