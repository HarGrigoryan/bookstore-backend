package com.example.bookstore.persistance.repository;

import com.example.bookstore.persistance.entity.Publisher;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PublisherRepository extends JpaRepository<Publisher, Long> {
    boolean existsByName(@NotBlank String name);
}
