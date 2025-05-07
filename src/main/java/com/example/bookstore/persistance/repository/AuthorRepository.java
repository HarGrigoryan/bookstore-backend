package com.example.bookstore.persistance.repository;

import com.example.bookstore.persistance.entity.Author;
import com.example.bookstore.service.criteria.AuthorSearchCriteria;
import com.example.bookstore.service.dto.AuthorDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

    @Query("SELECT a, COUNT(ba.author) as book_count " +
            "FROM Author a " +
            "INNER JOIN a.books ba " +
            "GROUP BY a.id " +
            "ORDER BY book_count desc " +
            "LIMIT :top")
    List<Author> findTopPublishedAuthors(Integer top);

    @Query("SELECT COUNT(ba.author) FROM BookAuthor ba WHERE ba.author.id = :id")
    Integer getNumberOfPublishedBooksById(Long id);

    Optional<Author> findAuthorById(Long id);

    Optional<Author> findAuthorByFullName(String fullName);

    @Query("""
        SELECT b.book.id from Author a JOIN a.books b WHERE a.id = :id
        """)
    List<Long> publishedBookIds(Long id);

    @Query("""
        SELECT DISTINCT new com.example.bookstore.service.dto.AuthorDTO(
                a.id, a.fullName, a.isOnGoodreads
                )
        FROM Author a
        WHERE (:#{#authorSearchCriteria.fullName} IS NULL OR LOWER(a.fullName) LIKE LOWER(CONCAT('%',:#{#authorSearchCriteria.fullName},'%')) )
        AND (:#{#authorSearchCriteria.isOnGoodreads} IS NULL OR a.isOnGoodreads = :#{#authorSearchCriteria.isOnGoodreads})
        """)
    Page<AuthorDTO> findAll(AuthorSearchCriteria authorSearchCriteria, Pageable pageable);
}
