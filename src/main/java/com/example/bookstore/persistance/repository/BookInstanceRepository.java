package com.example.bookstore.persistance.repository;

import com.example.bookstore.persistance.entity.BookInstance;
import com.example.bookstore.service.criteria.BookInstanceSearchCriteria;
import com.example.bookstore.service.dto.BookInstanceDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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

    //TODO: Implement
/*
    @Query("""

        """)
    Page<BookInstanceDTO> findAll(BookInstanceSearchCriteria criteria, Pageable pageRequest);
*/

}
