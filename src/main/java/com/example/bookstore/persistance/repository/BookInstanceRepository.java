package com.example.bookstore.persistance.repository;

import com.example.bookstore.persistance.entity.BookInstance;
import com.example.bookstore.service.criteria.BookInstanceSearchCriteria;
import com.example.bookstore.service.dto.BookInstanceDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookInstanceRepository extends JpaRepository<BookInstance, Long> {

    @Query("""
        SELECT b FROM BookInstance b WHERE b.book.id = :bookId AND b.instanceNumber = :copyNumber
        """)
    BookInstance findByCopyNumberAndBook(Integer copyNumber, Long bookId);

    @Query("""
        SELECT r.id FROM BookInstance b INNER JOIN Rental r ON r.bookInstance = b WHERE b.id = :id
        """)
    List<Long> dependentRentals(Long id);

    @Query("""
    SELECT COUNT(r.bookInstance.id)
    FROM Rental r
    WHERE r.bookInstance.id = :id
    """
    )
    Integer getCurrentRentCount(Long id);

    @Query("""
        SELECT new com.example.bookstore.service.dto.BookInstanceDTO(
                b.id, b.instanceNumber, b.isRentable, b.isSellable,
                        b.maxRentCount, b.initialPrice, b.createdAt, b.updatedAt, b.status, b.book.id)
        FROM BookInstance b
        LEFT JOIN b.rentals r
        WHERE (:#{#criteria.bookId} IS NULL OR b.book.id = :#{#criteria.bookId})
        AND (:#{#criteria.instanceNumber} IS NULL OR b.instanceNumber = :#{#criteria.instanceNumber})
        AND (:#{#criteria.isRentable} IS NULL OR b.isRentable = :#{#criteria.isRentable})
        AND (:#{#criteria.isSellable} IS NULL OR b.isSellable = :#{#criteria.isSellable})
        AND (:#{#criteria.maxRentCount} IS NULL OR b.maxRentCount = :#{#criteria.maxRentCount})
        AND (:#{#criteria.maxInitialPrice} IS NULL OR b.initialPrice <= :#{#criteria.maxInitialPrice})
        AND (:#{#criteria.bookInstanceStatus} IS NULL OR b.status = :#{#criteria.bookInstanceStatus})
        AND ( b.createdAt >= :#{#criteria.createdAt})
        AND ( b.updatedAt >= :#{#criteria.updatedAt})
        GROUP BY b
        HAVING(:#{#criteria.currentRentCount} IS NULL OR COUNT(r.bookInstance) = :#{#criteria.currentRentCount})
        AND (:#{#criteria.maxCurrentPrice} IS NULL OR (b.initialPrice <= :#{#criteria.maxCurrentPrice}) OR (b.maxRentCount <> 0 AND  (b.initialPrice - ((b.initialPrice / b.maxRentCount) * COUNT(r.bookInstance))) <= :#{#criteria.maxCurrentPrice}))
        """)
    Page<BookInstanceDTO> findAll(@Param("criteria") BookInstanceSearchCriteria criteria, Pageable pageRequest);

}
