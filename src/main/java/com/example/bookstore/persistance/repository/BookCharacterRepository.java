package com.example.bookstore.persistance.repository;

import com.example.bookstore.persistance.entity.BookCharacter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookCharacterRepository extends JpaRepository<BookCharacter, Long> {
}
