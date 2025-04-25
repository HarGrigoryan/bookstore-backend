package com.example.bookstore.persistance.repository;

import com.example.bookstore.enums.AuthorRole;
import com.example.bookstore.persistance.entity.Book;
import com.example.bookstore.service.criteria.BookSearchCriteria;
import com.example.bookstore.service.dto.BookSearchResponseDto;
import com.example.bookstore.service.projections.AuthorDtoProjection;
import com.example.bookstore.service.projections.BookDtoProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    Book findByBookId(String bookId);

    @Query(value = "SELECT book_id FROM book", nativeQuery = true)
    Set<String> findAllBookId();

    @Query("SELECT new com.example.bookstore.service.projections.AuthorDtoProjection(" +
            "a.id, a.fullName, a.isOnGoodreads, ba.authorRole) " +
            "FROM BookAuthor ba JOIN ba.author a " +
            "WHERE ba.book.id = :id")
    List<AuthorDtoProjection> findAuthors(Long id);

    @Query("SELECT new com.example.bookstore.service.projections.BookDtoProjection(" +
            "b.id, b.bookId, b.title, b.publishDate, b.firstPublishDate, b.description, b.format, b.isbn," +
            "b.language.id, b.publisher.id, b.edition, b.pageNumber, b.price)" +
            "FROM Book b JOIN BookAuthor ba ON b = ba.book " +
            "JOIN BookAward baw ON b = baw.book " +
            "JOIN BookGenre bg ON bg.book = b " +
            "JOIN Publisher p ON b.publisher = p " +
            "WHERE bg.genre.name = :genreName " +
            "AND p.name = :publisherName " +
            "AND baw.award.name = :awardName " +
            "AND ba.author.fullName = :authorName")
    List<BookDtoProjection> getAllBooksByGenreByAuthorByPublisherByAward(String genreName, String publisherName, String awardName, String authorName);

    @Query("SELECT new com.example.bookstore.service.projections.BookDtoProjection(" +
            "b.id, b.bookId, b.title, b.publishDate, b.firstPublishDate, b.description, b.format, b.isbn, " +
            "b.language.id, b.publisher.id, b.edition, b.pageNumber, b.price)"  +
            "FROM Book b JOIN BookAuthor ba ON b = ba.book " +
            "JOIN BookGenre bg ON bg.book = b " +
            "WHERE bg.genre.name = :genreName " +
            "AND ba.author.fullName = :authorName " +
            "AND ba.authorRole = :authorRole")
    List<BookDtoProjection> getAllBooksByAuthorByGenre(String authorName, AuthorRole authorRole, String genreName);

    @Query("""
        SELECT DISTINCT new com.example.bookstore.service.dto.BookSearchResponseDto(
                b.id, b.bookId, b.title, b.publishDate, b.firstPublishDate,
                        b.description, b.format, b.isbn,
                        b.edition, b.pageNumber, b.price, b.publisher.id, b.language.id)
        FROM Book b
        LEFT JOIN b.genres g
        LEFT JOIN b.authors a
        LEFT JOIN b.awards awards
        LEFT JOIN b.bookSeriesList series
        LEFT JOIN b.bookCharacterList c
        WHERE (:#{#criteria.title} IS NULL OR b.title LIKE CONCAT('%', :#{#criteria.title}, '%'))
        AND ( CAST(:#{#criteria.publishDate} AS DATE) = CAST('0001-1-1' AS DATE) OR b.publishDate = CAST(:#{#criteria.publishDate} AS DATE))
        AND ( CAST(:#{#criteria.firstPublishDate} AS DATE)=  CAST('0001-1-1' AS DATE) OR b.firstPublishDate = CAST( :#{#criteria.firstPublishDate} AS DATE))
        AND (:#{#criteria.authorName} IS NULL OR a.author.fullName LIKE CONCAT('%', :#{#criteria.authorName}, '%'))
        AND (:#{#criteria.authorRole} IS NULL OR a.authorRole = :#{#criteria.authorRole} )
        AND (:#{#criteria.format} IS NULL OR b.format = :#{#criteria.format})
        AND (:#{#criteria.isbn} IS NULL OR b.isbn = :#{#criteria.isbn})
        AND (:#{#criteria.languageName} IS NULL OR b.language.language LIKE CONCAT('%', :#{#criteria.languageName}, '%'))
        AND (:#{#criteria.pageNumber} IS NULL OR b.pageNumber =:#{#criteria.pageNumber})
        AND (:#{#criteria.awardName} IS NULL OR awards.award.name LIKE CONCAT('%', :#{#criteria.awardName}, '%'))
        AND (:#{#criteria.awardYear} IS NULL OR awards.award.year = :#{#criteria.awardYear})
        AND (:#{#criteria.publisherName} IS NULL OR b.publisher.name LIKE CONCAT('%', :#{#criteria.publisherName}, '%'))
        AND (:#{#criteria.seriesName} IS NULL OR series.series.name LIKE CONCAT('%', :#{#criteria.seriesName}, '%'))
        AND (:#{#criteria.seriesNumber} IS NULL OR series.seriesNumber LIKE CONCAT('%', :#{#criteria.seriesNumber}, '%'))
        AND (:#{#criteria.genre} IS NULL OR g.genre.name LIKE CONCAT('%', :#{#criteria.genre}, '%'))
        AND (:#{#criteria.characterName} IS NULL OR c.character.fullName LIKE CONCAT('%', :#{#criteria.characterName}, '%'))
        AND (:#{#criteria.characterComment} IS NULL OR c.character.comment LIKE CONCAT('%', :#{#criteria.characterComment}, '%'))
        """)
    Page<BookSearchResponseDto> findAll(BookSearchCriteria criteria, Pageable pageable);
}
