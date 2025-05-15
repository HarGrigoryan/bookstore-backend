package com.example.bookstore.persistance.repository;

import com.example.bookstore.persistance.entity.Book;
import com.example.bookstore.service.criteria.BookSearchCriteria;
import com.example.bookstore.service.dto.BookSearchResponseDTO;
import com.example.bookstore.service.dto.AuthorResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    Book findByBookId(String bookId);

    @Query(value = "SELECT book_id FROM book", nativeQuery = true)
    Set<String> findAllBookId();

    @Query("SELECT new com.example.bookstore.service.dto.AuthorResponseDTO(" +
            "a.id, a.fullName, a.isOnGoodreads, ba.authorRole) " +
            "FROM BookAuthor ba JOIN ba.author a " +
            "WHERE ba.book.id = :id")
    List<AuthorResponseDTO> findAuthors(Long id);

    @Query("""
    SELECT DISTINCT new com.example.bookstore.service.dto.BookSearchResponseDTO(
        b.id,
        b.bookId,
        b.title,
        b.publishDate,
        b.firstPublishDate,
        b.description,
        b.format,
        b.isbn,
        b.edition,
        b.pageNumber,
        b.price,
        p.id,
        l.id
    )
    FROM Book b
      LEFT JOIN b.genres bg
      LEFT JOIN bg.genre g
      LEFT JOIN b.authors ba
      LEFT JOIN ba.author a
      LEFT JOIN b.awards baw
      LEFT JOIN baw.award aw
      LEFT JOIN b.bookSeriesList bsl
      LEFT JOIN bsl.series s
      LEFT JOIN b.bookCharacterList bcl
      LEFT JOIN bcl.character c
      LEFT JOIN b.publisher p
      LEFT JOIN b.language l
    WHERE
      ( :#{#criteria.title} IS NULL OR b.title LIKE CONCAT('%', :#{#criteria.title}, '%') )
      AND ( :#{#criteria.authorName} IS NULL OR LOWER(a.fullName) LIKE LOWER(CONCAT('%', :#{#criteria.authorName}, '%')) )
      AND ( :#{#criteria.authorRole} IS NULL OR ba.authorRole = :#{#criteria.authorRole} )
      AND ( :#{#criteria.format}  IS NULL OR b.format = :#{#criteria.format} )
      AND ( :#{#criteria.isbn} IS NULL OR b.isbn = :#{#criteria.isbn} )
      AND ( :#{#criteria.price} IS NULL OR b.price = :#{#criteria.price} )
      AND (:#{#criteria.edition} IS NULL OR LOWER(b.edition) = LOWER(:#{#criteria.edition}))
      AND ( CAST(:#{#criteria.publishDate} AS DATE) = CAST('0001-1-1' AS DATE) OR b.publishDate = CAST(:#{#criteria.publishDate} AS DATE))
      AND ( CAST(:#{#criteria.firstPublishDate} AS DATE) =  CAST('0001-1-1' AS DATE) OR b.firstPublishDate = CAST( :#{#criteria.firstPublishDate} AS DATE))
      AND ( :#{#criteria.languageName} IS NULL OR LOWER(l.language)  LIKE LOWER(CONCAT('%', :#{#criteria.languageName}, '%')) )
      AND ( :#{#criteria.pageNumber}  IS NULL OR b.pageNumber = :#{#criteria.pageNumber} )
      AND ( :#{#criteria.awardName}   IS NULL OR LOWER(aw.name) LIKE LOWER(CONCAT('%', :#{#criteria.awardName}, '%')) )
      AND ( :#{#criteria.awardYear}   IS NULL OR aw.year = :#{#criteria.awardYear} )
      AND ( :#{#criteria.publisherName} IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :#{#criteria.publisherName}, '%')) )
      AND ( :#{#criteria.seriesName}  IS NULL OR LOWER(s.name) LIKE LOWER(CONCAT('%', :#{#criteria.seriesName}, '%')) )
      AND ( :#{#criteria.seriesNumber} IS NULL OR bsl.seriesNumber LIKE CONCAT('%', :#{#criteria.seriesNumber}, '%') )
      AND ( :#{#criteria.genre}       IS NULL OR LOWER(g.name) LIKE LOWER(CONCAT('%', :#{#criteria.genre}, '%')) )
      AND ( :#{#criteria.characterName}    IS NULL OR LOWER(c.fullName) LIKE LOWER(CONCAT('%', :#{#criteria.characterName}, '%')) )
      AND ( :#{#criteria.characterComment} IS NULL OR LOWER(c.comment) LIKE LOWER(CONCAT('%', :#{#criteria.characterComment}, '%')) )
""")
    Page<BookSearchResponseDTO> findAll(
            @Param("criteria") BookSearchCriteria criteria,
            Pageable pageable
    );


}
