package com.example.bookstore.persistance.repository;

import com.example.bookstore.persistance.entity.Language;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LanguageRepository extends JpaRepository<Language, Long> {
    @Query("""
            SELECT b.id FROM Language l INNER JOIN Book b ON l = b.language WHERE l.id = :id
            """)
    List<Long> dependentBookIds(Long id);

    @Query("""
        SELECT l FROM Language l WHERE l.language = :name
        """)
    Language findByLanguage(@NotBlank String name);
}
